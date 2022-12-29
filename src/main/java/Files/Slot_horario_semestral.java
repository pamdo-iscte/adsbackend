package Files;

import java.io.Serializable;

public class Slot_horario_semestral implements Serializable {

    private String id;
    private String text;
    private String start;
    private String end;
    private String backColor;

    private String informacao_detalhada;

    private String turno;


    public Slot_horario_semestral(String id, String text, String start, String end, String backColor, String informacao_detalhada, String turno) {
        this.id = id;
        this.text = text;
        this.start = start;
        this.end = end;
        this.backColor = backColor;
        this.informacao_detalhada = informacao_detalhada;
        this.turno = turno;
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
                ", turno='" + turno + '\'' +
                '}';
    }
}
