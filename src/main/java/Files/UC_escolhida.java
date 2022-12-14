package Files;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UC_escolhida {

    @JsonProperty("horas")
    private String horas;

    @JsonProperty("dias")
    private String dias;
    @JsonProperty("datas")
    private List<String> datas;

    @JsonProperty("horas_repetidas")
    private List<String> horas_repetidas;

    @JsonProperty("turno")
    private String turno;

    @JsonProperty("unidade_de_execucao")
    private String unidade_de_execucao;

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

    public List<String> getDatas() {
        return datas;
    }

    public List<String> getHoras_repetidas() {
        return horas_repetidas;
    }

}
