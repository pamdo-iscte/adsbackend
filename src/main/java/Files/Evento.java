package Files;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class Evento {
    private Date data;
    //Algumas avaliacoes (i.e. projetos) tem data final != data inicial
    private Date data_final;

    private int numero_de_alunos;
    private String[] cursos;
    private String unidade;
    private String hora_inicio;
    private String hora_fim;
    
    public Evento(Date data, Date data_final, int numero_de_alunos,
                  String[] cursos, String unidade,
                  String hora_inicio, String hora_fim) {
        this.data = data;
        this.data_final = data_final;
        this.numero_de_alunos = numero_de_alunos;
        this.cursos = cursos;
        this.unidade = unidade;
        this.hora_inicio = hora_inicio;
        this.hora_fim = hora_fim;
    }

    public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getData_final() {
		return data_final;
	}

	public void setData_final(Date data_final) {
		this.data_final = data_final;
	}

	public int getNumero_de_alunos() {
        return numero_de_alunos;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public String getHora_fim() {
        return hora_fim;
    }

    @Override
	public String toString() {
		return "Evento [data=" + data + ", data_final=" + data_final + ", numero_de_alunos=" + numero_de_alunos
				+ ", cursos=" + Arrays.toString(cursos) + ", unidade=" + unidade + ", hora_inicio=" + hora_inicio
				+ ", hora_fim=" + hora_fim + "]";
	}
}
