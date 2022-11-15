
package Files;

public class Curso {
    private String value;
    private String label;

    public Curso(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}

