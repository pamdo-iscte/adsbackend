package Files;

import java.util.List;

public class ReceiveClasses {

    private List<Convert_Aula_CSV_to_JSON> aulas;
    private List<Slot_horario_semestral> slots;

    private String num;

    public List<Convert_Aula_CSV_to_JSON> getAulas() {
        return aulas;
    }

    public String getNum() {
        return num;
    }

    public List<Slot_horario_semestral> getSlots() {
        return slots;
    }
}
