package Files;

import com.fasterxml.jackson.databind.MappingIterator;

import com.google.gson.Gson;

import java.io.File;
import java.util.List;
import java.util.Map;


public class Convert_Aula_CSV_to_JSON {


    private String curso;
    private String unidade_de_execucao;
    private String turno;
    private String turma;
    private String dia_da_semana;
    private String hora_inicio;
    private String hora_fim;
    private String data;
    private String sala;

    public Convert_Aula_CSV_to_JSON(String curso, String unidade_de_execucao, String turno, String turma, String dia_da_semana,
                                    String hora_inicio, String hora_fim, String data, String sala) {
        this.curso = curso;
        this.unidade_de_execucao = unidade_de_execucao;
        this.turno = turno;
        this.turma = turma;
        this.dia_da_semana = dia_da_semana;
        this.hora_inicio = hora_inicio;
        this.hora_fim = hora_fim;
        this.data = data;
        this.sala = sala;
    }


    @Override
    public String toString() {
        return "Convert_Aula_CSV_to_JSON{" +
                "curso='" + curso + '\'' +
                ", unidade_de_execucao='" + unidade_de_execucao + '\'' +
                ", turno='" + turno + '\'' +
                ", turma='" + turma + '\'' +
                ", dia_da_semana='" + dia_da_semana + '\'' +
                ", hora_inicio='" + hora_inicio + '\'' +
                ", hora_fim='" + hora_fim + '\'' +
                ", data='" + data + '\'' +
                ", sala='" + sala + '\'' +
                '}';
    }


}
