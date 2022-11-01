package com.backendads.backendads.controller;

import Files.MetodosdeAvaliacao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@RestController
public class HelloController {

    @GetMapping("/fixe")
    public String index() {
        Class test = MetodosdeAvaliacao.class;
        Method[] methods = test.getDeclaredMethods();
        String[] array= new String[methods.length];
        for (int i=0; i<array.length;i++ ){
            array[i]=methods[i].getName();
        }
        return array[0];
    }
}