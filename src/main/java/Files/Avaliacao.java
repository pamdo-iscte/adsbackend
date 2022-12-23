package Files;

import java.util.Date;
import java.util.List;

public class Avaliacao extends Evento {
    private String codigo;
    private String tipo;
    private String epoca;
    private String nome;
    private boolean requer_inscricao_previa;
    private String periodo_inscricao;
    private List<Sala> salas;
    private String estado_pedido_sala;

    private int capacidade_salas;


    public Avaliacao(Date data, Date data_final, int numero_de_alunos,
                     String[] cursos, String unidade,
                     String hora_inicio, String hora_fim, String[] line) {
        super(data, data_final, numero_de_alunos, cursos, unidade, hora_inicio, hora_fim);
        this.codigo = line[0];
        this.tipo = line[3];
        this.epoca = line[4];
        this.nome = line[5];
        this.requer_inscricao_previa= !line[6].equals("Não");
        this.periodo_inscricao = line[7];
        this.estado_pedido_sala = line[10];
    }
    
    public List<Sala> getSalas() {
		return salas;
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
}
