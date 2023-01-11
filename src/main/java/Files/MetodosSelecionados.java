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

    @JsonProperty("checkbox")
    private boolean checkbox;


    @JsonProperty("num")
    private int num;

    public int getNum() {
        return num;
    }
    public boolean isCheckbox() {
        return checkbox;
    }

    public List<String> getAulas() {
        return aulas;
    }

    public List<String> getAvaliacoes() {
        return avaliacoes;
    }

}
