package com.backendads.backendads.controller;

import Files.*;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;


@RestController
public class HelloController {
    public static List<OrderLine> readObjectsFromCsv(File file) throws IOException {
        CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        try (MappingIterator<OrderLine> mappingIterator = csvMapper.readerFor(OrderLine.class).with(bootstrap).readValues(file)) {
            return mappingIterator.readAll();
        }
    }

    public static void writeAsJson(List<OrderLine> data, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, data);
    }
    @GetMapping("/teste")
    public String fixe() throws IOException {
        File input = new File("Livro1.csv");
        File output = new File("data.json");
        List<OrderLine> data = readObjectsFromCsv(input);
        writeAsJson(data, output);
        return new Gson().toJson(data);
    }
    @GetMapping("/cena")
    public String cena() {
       Curso a= new Curso("ola","adu");
       Curso b= new Curso("ola","adu");
       List<Curso> x= new ArrayList<>();
       x.add(a);x.add(b);
       return new Gson().toJson(x);
    }

    @GetMapping("/get")
    public String index() {
        StringBuilder result = new StringBuilder();
        Class<MetodosParaAulas> test = MetodosParaAulas.class;
        Class<MetodosdeAvaliacao> ava = MetodosdeAvaliacao.class;
        Method[] methods = test.getDeclaredMethods();
        Method[] m = ava.getDeclaredMethods();
        String name = "";
        result.append("\nMétodos para aulas: \n");
        for (Method m1 : methods) {
            name = m1.getName().replace("_"," ");
            name = name.substring(0,1).toUpperCase() + name.substring(1);
            result.append(name).append("; ");
        }
        result.append("\nMétodos para avaliações: \n");
        for (Method m2 : m) {
            name = m2.getName().replace("_"," ");
            name = name.substring(0,1).toUpperCase() + name.substring(1);
            result.append(name).append("; ");
        }
        return result.toString();
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