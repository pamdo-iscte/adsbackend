package Files;

public class Slot_horario_semestral {

    private String id;
    private String text;
    private String start;
    private String end;
    private String backColor;

    public Slot_horario_semestral(String id, String text, String start, String end, String backColor) {
        this.id = id;
        this.text = text;
        this.start = start;
        this.end = end;
        this.backColor = backColor;
    }

    @Override
    public String toString() {
        return "Slot_horario_semestral{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", backColor='" + backColor + '\'' +
                '}';
    }
}
