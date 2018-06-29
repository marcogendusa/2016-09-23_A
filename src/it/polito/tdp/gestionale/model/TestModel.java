package it.polito.tdp.gestionale.model;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();
		model.creaGrafo();
		System.out.println(model.getStatistiche().toString());
	}

}
