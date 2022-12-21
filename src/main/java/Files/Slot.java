package org.example;

import java.util.List;

public class Slot {

	String data;
	String hora_inicio;
	String hora_final;
	List<Evento> eventos;
	List<Sala> salas_livres;
	//List<Sala> salas_ocupadas;
	
	public Slot(String data, String hora_inicio, String hora_final, List<Evento> eventos, List<Sala> salas_livres) {
		
		this.data = data;
		this.hora_inicio = hora_inicio;
		this.hora_final = hora_final;
		this.eventos = eventos;
		this.salas_livres = salas_livres;
	}

	@Override
	public String toString() {
		return "Slot [data=" + data + ", hora_inicio=" + hora_inicio + ", hora_final=" + hora_final + ", eventos="
				+ eventos + ", salas_livres=" + salas_livres + "]";
	}
	
	
	
	
	
}
