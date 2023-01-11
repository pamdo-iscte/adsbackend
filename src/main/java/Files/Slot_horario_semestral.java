package Files;

import java.io.Serializable;
import java.util.Calendar;

public class Slot_horario_semestral implements Serializable {

    private String id;
    private String text;
    private String start;
    private String end;
    private String backColor;

    private String informacao_detalhada;

    private String turno;

    private Calendar cal = null;

    private String dia_da_sem;

    public Slot_horario_semestral(String id, String text, String start, String end, String backColor, String informacao_detalhada, String turno, String dia_da_sem) {
        this.id = id;
        this.text = text;
        this.start = start;
        this.end = end;
        this.backColor = backColor;
        this.informacao_detalhada = informacao_detalhada;
        this.turno = turno;
        this.dia_da_sem = dia_da_sem;
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
                ", cal=" + cal +
                ", dia_da_sem='" + dia_da_sem + '\'' +
                '}';
    }

    public Calendar getCalendar() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTurno() {
        return turno;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public String getDia_da_sem() {
        return dia_da_sem;
    }
}
