package Files;

public class Slot_horario_semestral {

    private String id;
    private String text;
    private String start;
    private String end;
    private String backColor;

    private String informacao_detalhada;


    public Slot_horario_semestral(String id, String text, String start, String end, String backColor, String informacao_detalhada) {
        this.id = id;
        this.text = text;
        this.start = start;
        this.end = end;
        this.backColor = backColor;
        this.informacao_detalhada = informacao_detalhada;
    }

    @Override
    public String toString() {
        return "Slot_horario_semestral{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", backColor='" + backColor + '\'' +
                ", informacao_detalhada='" + informacao_detalhada + '\'' +
                '}';
    }
}
