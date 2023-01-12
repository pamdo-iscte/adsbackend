package Files;

public class Convert_metrica_JSON {

    int aulas_nao_alocadas;
    int aulas_sobrelotadas;
    int estudantes_em_sobrelotadas;
    int sobrelotação_media;
    int lugares_desperdicado;
    int aulas_mal_atribuidas;
    int mudanca_sala;
    int mudanca_edificio;

    String file_aulas;
    String file_avaliacoes;
    String nome;

    public Convert_metrica_JSON(int aulas_nao_alocadas, int aulas_sobrelotadas, int estudantes_em_sobrelotadas, int sobrelotação_media, int lugares_desperdicado, int aulas_mal_atribuidas, int mudanca_sala, int mudanca_edificio) {
        this.aulas_nao_alocadas = aulas_nao_alocadas;
        this.aulas_sobrelotadas = aulas_sobrelotadas;
        this.estudantes_em_sobrelotadas = estudantes_em_sobrelotadas;
        this.sobrelotação_media = sobrelotação_media;
        this.lugares_desperdicado = lugares_desperdicado;
        this.aulas_mal_atribuidas = aulas_mal_atribuidas;
        this.mudanca_sala = mudanca_sala;
        this.mudanca_edificio = mudanca_edificio;
    }


    @Override
    public String toString() {
        return "Convert_metrica_JSON{" +
                "aulas_nao_alocadas=" + aulas_nao_alocadas +
                ", aulas_sobrelotadas=" + aulas_sobrelotadas +
                ", estudantes_em_sobrelotadas=" + estudantes_em_sobrelotadas +
                ", sobrelotação_media=" + sobrelotação_media +
                ", lugares_desperdicado=" + lugares_desperdicado +
                ", aulas_mal_atribuidas=" + aulas_mal_atribuidas +
                ", mudanca_sala=" + mudanca_sala +
                ", mudanca_edificio=" + mudanca_edificio +
                ", file_aulas='" + file_aulas + '\'' +
                ", file_avaliacoes='" + file_avaliacoes + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }

    public void setFile_avaliacoes(String file_avaliacoes) {
        this.file_avaliacoes = file_avaliacoes;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setFile_aulas(String file_aulas) {
        this.file_aulas = file_aulas;
    }

    public int getAulas_nao_alocadas() {
        return aulas_nao_alocadas;
    }

    public int getAulas_sobrelotadas() {
        return aulas_sobrelotadas;
    }

    public int getEstudantes_em_sobrelotadas() {
        return estudantes_em_sobrelotadas;
    }

    public int getSobrelotação_media() {
        return sobrelotação_media;
    }

    public int getLugares_desperdicado() {
        return lugares_desperdicado;
    }

    public int getAulas_mal_atribuidas() {
        return aulas_mal_atribuidas;
    }

    public int getMudanca_sala() {
        return mudanca_sala;
    }

    public int getMudanca_edificio() {
        return mudanca_edificio;
    }
}
