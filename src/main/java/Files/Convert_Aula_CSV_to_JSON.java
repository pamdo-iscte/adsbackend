package Files;


import java.util.Calendar;
import java.util.Date;

public class Convert_Aula_CSV_to_JSON {


    private final String curso;
    private final String unidade_de_execucao;
    private final String turno;
    private final String turma;
    private final String dia_da_semana;
    private final String data;
    private final String hora_inicio;
    private final String hora_fim;
    private final String sala;

    public Convert_Aula_CSV_to_JSON(String curso, String unidade_de_execucao, String turno, String turma, String dia_da_semana,
                                    String hora_inicio, String hora_fim, String data, String sala) {
        this.curso = curso;
        this.unidade_de_execucao = unidade_de_execucao;
        this.turno = turno;
        this.turma = turma;
        this.dia_da_semana = dia_da_semana;
        this.hora_inicio = hora_inicio;
        this.hora_fim = hora_fim;
        if (!data.equals("")) {
            String[] data_fields = data.split("/");
            this.data = data_fields[2] + "/" + data_fields[1] + "/" + data_fields[0];
        } else this.data=data;

        this.sala = sala;
    }


    @Override
    public String toString() {
        return "Convert_Aula_CSV_to_JSON{" +
                "curso='" + curso + '\'' +
                ", unidade_de_execucao='" + unidade_de_execucao + '\'' +
                ", turno='" + turno + '\'' +
                ", turma='" + turma + '\'' +
                ", dia_da_semana='" + dia_da_semana + '\'' +
                ", data='" + data + '\'' +
                ", hora_inicio='" + hora_inicio + '\'' +
                ", hora_fim='" + hora_fim + '\'' +
                ", sala='" + sala + '\'' +
                '}';
    }
}
