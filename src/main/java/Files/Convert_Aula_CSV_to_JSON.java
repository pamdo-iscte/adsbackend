package Files;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Convert_Aula_CSV_to_JSON implements Serializable {


    private final String curso;
    private final String unidade_de_execucao;
    private final String turno;
    private final String turma;
    private final String dia_da_semana;

    private final String data;
    private String hora_inicio;
    private String hora_fim;
    private final String sala;

    private String dias;

    private String horas;

    private List<String> datas = new ArrayList<>();
    private List<String> horas_repetidas = new ArrayList<>();

    public Convert_Aula_CSV_to_JSON(String curso, String unidade_de_execucao, String turno, String turma, String dia_da_semana,
                                    String hora_inicio, String hora_fim, String data, String sala,String dias) {
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
        this.dias = dias;
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
                ", dias='" + dias + '\'' +
                ", horas='" + horas + '\'' +
                ", datas=" + datas +
                '}';
    }

    public String getData() {
        return data;
    }


    public String getUnidade_de_execucao() {
        return unidade_de_execucao;
    }

    public String getTurno() {
        return turno;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public String getHora_fim() {
        return hora_fim;
    }

    public String getSala() {
        return sala;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public void setHora_fim(String hora_fim) {
        this.hora_fim = hora_fim;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getDia_da_semana() {
        return dia_da_semana;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public void addDatas(String data) {
        this.datas.add(data);
    }
    public void addHoras(String hora) {
        this.horas_repetidas.add(hora);
    }

    public Boolean compareHoras(String horas, Boolean inicio_ou_fim) {
        String[] horas_fields = horas.split(":");
        int horas_int = Integer.parseInt(horas_fields[0]);
         if (inicio_ou_fim) {
             String[] hora_inicio_fields = this.hora_inicio.split(":");

             int hora_inicio_int = Integer.parseInt(hora_inicio_fields[0]);

             if (hora_inicio_int < horas_int) return true;
             else return hora_inicio_int == horas_int && Integer.parseInt(hora_inicio_fields[1]) < Integer.parseInt(horas_fields[1]);
         } else {
             String[] hora_fim_fields = this.hora_fim.split(":");

             int hora_fim_int = Integer.parseInt(hora_fim_fields[0]);

             if (hora_fim_int < horas_int) return true;
             else return hora_fim_int == horas_int && Integer.parseInt(hora_fim_fields[1]) < Integer.parseInt(horas_fields[1]);
         }
    }

    public List<String> getDatas() {
        return datas;
    }

    public List<String> getHoras_repetidas() {
        return horas_repetidas;
    }

    public String getCurso() {
        return curso;
    }
}
