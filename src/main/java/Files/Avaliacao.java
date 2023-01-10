package Files;

import Files.Evento;
import Files.Sala;

import java.util.Date;
import java.util.List;

public class Avaliacao extends Evento {
    public String codigo;
    public String tipo;
    public String epoca;
    public String nome;
    public String requer_inscricao_previa;
    public String periodo_inscricao;
    public List<Sala> salas;
    public String estado_pedido_sala;
    public String curso;
    public String unidade;

    private int capacidade_salas;


    public Avaliacao(Date data, Date data_final, int numero_de_alunos,
                     String[] cursos, String unidade,
                     String hora_inicio, String hora_fim, String[] line) {
        super(data, data_final, numero_de_alunos, cursos, unidade, hora_inicio, hora_fim);
        this.codigo = line[0];
        this.tipo = line[3];
        this.epoca = line[4];
        this.nome = line[5];
        this.unidade = line[1];
        this.requer_inscricao_previa= line[6];
        this.periodo_inscricao = line[7];
        this.estado_pedido_sala = line[10];
        this.curso = line[2];
    }


    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }

    public String getEstado_pedido_sala() {
        return estado_pedido_sala;
    }

    public int getCapacidade_salas() {
        return capacidade_salas;
    }

    public void setCapacidade_salas(int capacidade_salas) {
        this.capacidade_salas = capacidade_salas;
    }


	public List<Sala> getSalas() {
		return salas;
	}
}
