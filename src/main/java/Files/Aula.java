package Files;

import Files.Evento;
import Files.Sala;

import java.util.Date;

public class Aula extends Evento {


	String turno;
	String unidade_de_execucao;
	String dia_semana;
	String curso;
	String hora_inicial;
	String hora_final;
	int inscritos;
	Sala sala;
	String dia;
	String turma;
	String turnoInscricoesSuperior;
	String turnoCapacidadeSuperior;
	
	String caracteristica;
	String professor;

	String professor;


	public Aula(Date data, Date data_final, int numero_de_alunos, String[] cursos, String unidade, String hora_inicio, String hora_fim, String[] line) {
		super(data, data_final, numero_de_alunos, cursos, unidade, hora_inicio, hora_fim);
		this.turma = line[3];
		this.inscritos = Integer.parseInt(line[4]);
		this.caracteristica = line[11];
		this.curso = line[0];
		this.turno = line[2];
		this.dia = line[10];
		this.turnoCapacidadeSuperior = line[5];
		this.turnoInscricoesSuperior = line[6];
		this.unidade_de_execucao  =line[1];
		this.dia_semana = line[7];
		
	}
	public Aula(Date data, Date data_final, int numero_de_alunos, String[] cursos, String unidade, String hora_inicio, String hora_fim, String[] line,Sala sala) {
		super(data, data_final, numero_de_alunos, cursos, unidade, hora_inicio, hora_fim);
		this.turma = line[3];
		this.inscritos = Integer.parseInt(line[4]);
		this.caracteristica = line[11];
		this.curso = line[0];
		this.turno = line[2];
		this.dia = line[10];
		this.turnoCapacidadeSuperior = line[5];
		this.turnoInscricoesSuperior = line[6];
		this.unidade_de_execucao  =line[1];
		this.dia_semana = line[7];
		this.sala=sala;

	}


	public String getProfessor() {
		return professor;
	}


	public void setProfessor(String professor) {
		this.professor = professor;
	}


	public String getTurno() {
		return turno;
	}


	public void setTurno(String turno) {
		this.turno = turno;
	}


	public String getUnidade_de_execucao() {
		return unidade_de_execucao;
	}


	public void setUnidade_de_execucao(String unidade_de_execucao) {
		this.unidade_de_execucao = unidade_de_execucao;
	}


	public String getDia_semana() {
		return dia_semana;
	}


	public void setDia_semana(String dia_semana) {
		this.dia_semana = dia_semana;
	}


	public String getCurso() {
		return curso;
	}


	public void setCurso(String curso) {
		this.curso = curso;
	}


	public String getHora_inicial() {
		return hora_inicial;
	}


	public void setHora_inicial(String hora_inicial) {
		this.hora_inicial = hora_inicial;
	}


	public String getHora_final() {
		return hora_final;
	}


	public void setHora_final(String hora_final) {
		this.hora_final = hora_final;
	}


	public int getInscritos() {
		return inscritos;
	}


	public void setInscritos(int inscritos) {
		this.inscritos = inscritos;
	}


	public Sala getSala() {
		return sala;
	}


	public void setSala(Sala sala) {
		this.sala = sala;
	}


	public String getCaracteristica() {
		return caracteristica;
	}


	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}
}

