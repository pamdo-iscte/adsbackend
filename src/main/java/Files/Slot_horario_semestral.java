package Files;

public class Slot_horario_semestral {

    private String id;
    private String text;
    private String start;
    private String end;

    public Slot_horario_semestral(String id, String text, String start, String end) {
        this.id = id;
        this.text = text;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "Slot_horario_semestral{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
