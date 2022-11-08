package Files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MetodosSelecionados {

    @JsonProperty("metodos_aulas")
    private String metodos_aulas;

    @JsonProperty("metodos_avaliacoes")
    private String metodos_avaliacoes;

    private List<String> list_metodos_aulas = new ArrayList<>();
    private List<String> list_metodos_avaliacoes = new ArrayList<>();

    public MetodosSelecionados() {
    }

    public MetodosSelecionados(String metodos_aulas, String metodos_avaliacoes) {
        for (String s : metodos_aulas.split(";")) {
            add_method_to_list(s,Boolean.TRUE);
        }
        for (String s : metodos_avaliacoes.split(";")) {
            add_method_to_list(s,Boolean.FALSE);
        }
    }

    private void add_method_to_list(String s, Boolean b) {
        s = s.substring(0,1).toLowerCase() + s.substring(1);
        s = s.replace(" ","_");
        if (b) list_metodos_aulas.add(s);
        else list_metodos_avaliacoes.add(s);
    }

    public List<String> getList_metodos_aulas() {
        return list_metodos_aulas;
    }

    public String getMetodos_aulas() {
        return metodos_aulas;
    }

    public String getMetodos_avaliacoes() {
        return metodos_avaliacoes;
    }

    public List<String> getList_metodos_avaliacoes() {
        return list_metodos_avaliacoes;
    }
}
