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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
public class HelloController {
    private FuncoesAuxiliares aux = new FuncoesAuxiliares();

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
        List<Convert_Aula_CSV_to_JSON> aulas = aux.get_Dias_da_semana(aux.getAulas());
        return new Gson().toJson(aulas);
    }


    @PostMapping("/post")
    public ResponseEntity<?> readPost(@RequestBody MetodosSelecionados json_metodos) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        MetodosSelecionados metodos = new MetodosSelecionados(json_metodos.getMetodos_aulas(),json_metodos.getMetodos_avaliacoes());
        for (String n : metodos.getList_metodos_aulas()) {
            System.out.println(n);
        }
        for (String m : metodos.getList_metodos_avaliacoes())
            System.out.println(m);
        aux.invoke_method("presevar_caracteristica_da_aula");
        return new ResponseEntity<>("CHEGOU", HttpStatus.OK);
    }

}