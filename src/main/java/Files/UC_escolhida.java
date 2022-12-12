package Files;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UC_escolhida {

    @JsonProperty("horas")
    private String horas;

    @JsonProperty("dias")
    private String dias;
    @JsonProperty("datas")
    private String datas;

    @JsonProperty("horas_repetidas")
    private String horas_repetidas;

    @JsonProperty("turno")
    private String turno;

    @JsonProperty("unidade_de_execucao")
    private String unidade_de_execucao;

    @JsonProperty("data_de_hoje")
    private String data_de_hoje;

    @JsonProperty("dia_da_sem_de_hoje")
    private int dia_da_sem_de_hoje;

    public String getHoras() {
        return horas;
    }

    public String getDias() {
        return dias;
    }

    public String getTurno() {
        return turno;
    }

    public String getUnidade_de_execucao() {
        return unidade_de_execucao;
    }

    public String getDatas() {
        return datas;
    }

    public String getHoras_repetidas() {
        return horas_repetidas;
    }

    public String getData_de_hoje() {
        return data_de_hoje;
    }

    public int getDia_da_sem_de_hoje() {
        return dia_da_sem_de_hoje;
    }
}
