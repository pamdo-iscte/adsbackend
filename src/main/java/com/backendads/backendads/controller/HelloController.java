package com.backendads.backendads.controller;

import Files.*;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestController
public class HelloController {
    private final String file_horarios_1_sem = "ADS - Horários 1º sem 2022-23.csv";

    @GetMapping("/cena")
    public String cena() {
        Curso a= new Curso("ola","adu");
        Curso b= new Curso("ola","adu");
        List<Curso> x= new ArrayList<>();
        x.add(a);x.add(b);
        return new Gson().toJson(x);
    }
    @GetMapping("/get_metodos")
    public String index() {
        List<String> result = new ArrayList<>();
        Class<MetodosParaAulas> test = MetodosParaAulas.class;
        Class<MetodosdeAvaliacao> ava = MetodosdeAvaliacao.class;
        Method[] methods = test.getDeclaredMethods();
        Method[] m = ava.getDeclaredMethods();
        String name = "";
        result.add("\nMétodos para aulas: \n");
        for (Method m1 : methods) {
            name = m1.getName().replace("_"," ");
            name = name.substring(0,1).toUpperCase() + name.substring(1);
            result.add(name);
        }
        result.add("\nMétodos para avaliações: \n");
        for (Method m2 : m) {
            name = m2.getName().replace("_"," ");
            name = name.substring(0,1).toUpperCase() + name.substring(1);
            result.add(name);
        }

        String json = new Gson().toJson(result);
        return json;
    }

    @GetMapping("/get_aluno_professor")
    public String aulas() {
        List<Convert_Aula_CSV_to_JSON> all_aulas = new ArrayList<>();
        try {

            FileReader filereader = new FileReader(file_horarios_1_sem);

            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            boolean first_line = true;


            while ((nextRecord = csvReader.readNext()) != null) {
                if (first_line) {
                    first_line = false;
                } else {
                    String[] line = nextRecord[0].split(";");
                    String curso = line[0].replace("'",",");

                    String sala = "";
                    String data ="";
                    if (line.length >=13) sala = line[12];
                    if (line.length >= 11) data = line[10];
                    Convert_Aula_CSV_to_JSON convert = new Convert_Aula_CSV_to_JSON(curso,line[1],line[2],line[3],line[7],
                            line[8],line[9],data,sala);

                    all_aulas.add(convert);
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new Gson().toJson(all_aulas);
    }

    @PostMapping("/post")
    public ResponseEntity<?> readPost(@RequestBody MetodosSelecionados json_metodos) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        MetodosSelecionados metodos = new MetodosSelecionados(json_metodos.getMetodos_aulas(),json_metodos.getMetodos_avaliacoes());
        for (String n : metodos.getList_metodos_aulas()) {
            System.out.println(n);
        }
        for (String m : metodos.getList_metodos_avaliacoes())
            System.out.println(m);
        invoke_method("presevar_caracteristica_da_aula");
        return new ResponseEntity<>("CHEGOU", HttpStatus.OK);
    }

    //Apenas para testar
    public void invoke_method(String name) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<MetodosParaAulas> class_metodos_aulas = MetodosParaAulas.class;
        Object t = class_metodos_aulas.newInstance();
        Method[] metodos = class_metodos_aulas.getDeclaredMethods();
        for (Method m: metodos) {
            if (m.getName().equals(name)) {
                System.out.println(m.invoke(t,1));
            }
        }
    }
}