package Files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MetodosSelecionados {

    @JsonProperty("aulas")
    private List<String> aulas;

    @JsonProperty("avaliacoes")
    private List<String> avaliacoes;

//    private void add_method_to_list(String s, Boolean b) {
//        s = s.substring(0,1).toLowerCase() + s.substring(1);
//        s = s.replace(" ","_");
//        if (b) list_metodos_aulas.add(s);
//        else list_metodos_avaliacoes.add(s);
//    }

    public List<String> getAulas() {
        return aulas;
    }

    public List<String> getAvaliacoes() {
        return avaliacoes;
    }

}
