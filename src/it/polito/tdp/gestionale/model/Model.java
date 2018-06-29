package it.polito.tdp.gestionale.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.gestionale.db.DidatticaDAO;

public class Model {

	private List<Corso> corsi;
	private List<Studente> studenti;
	private DidatticaDAO dao;
	private Graph<Nodo, DefaultEdge> grafo;
	private Map<Integer, Studente> mapStudenti;

	public Model() {
		this.dao = new DidatticaDAO();
		this.corsi = dao.getTuttiICorsi();
		this.studenti = dao.getTuttiStudenti();
		this.mapStudenti = new HashMap<>();
		
		for(Studente s: this.studenti) {
			this.mapStudenti.put(s.getMatricola(), s);
		}
		
		for(Corso c: corsi) {
			dao.getStudentiIscrittiAlCorso(c, mapStudenti);
		}
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		Graphs.addAllVertices(this.grafo, corsi);
		Graphs.addAllVertices(this.grafo, studenti);
		
		for(Corso c: corsi) {
			for(Studente s: c.getStudenti())
				grafo.addEdge(c, s);
		}
		
	}
	
	public Map<Corso, Integer> getStatistiche() {
		Map<Corso, Integer> map = new TreeMap<Corso, Integer>();
		for(Corso c: corsi) {
				map.put(c, Graphs.neighborListOf(grafo, c).size());
		}
		return map;
	}
	
	
	public List<Integer> getStatCorsi() {

		List<Integer> statCorsi = new ArrayList<Integer>();

		// Inizializzo la struttura dati dove salvare le statistiche
		for (int i = 0; i < corsi.size() + 1; i++) {
			statCorsi.add(0);
		}

		// Aggiorno le statistiche
		for (Studente studente : studenti) {
			int ncorsi = Graphs.neighborListOf(grafo, studente).size();
			int counter = statCorsi.get(ncorsi);
			counter++;
			statCorsi.set(ncorsi, counter);
		}

		return statCorsi;
	}
	
	
}
