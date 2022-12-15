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
import java.nio.file.Path;
import java.util.*;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
public class HelloController {
    private final String primeiro_dia_de_aulas = "2022/09/12";
    private FuncoesAuxiliares aux = new FuncoesAuxiliares();
    private Calendar primeiro_dia_de_aulas_cal = aux.setCalendar(Calendar.getInstance(),primeiro_dia_de_aulas.split("/"));

    private List<String> colors = Arrays.asList("#1cceb1", "#97fca3", "#5d8ce9","#6cda72","#a1f2e5","#9799fc","#fcf897");
    private int index_of_colors = 0;

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
            name = m1.getName().replace("_", " ");
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            result.add(name);
        }
        result.add("\nMétodos para avaliações: \n");
        for (Method m2 : m) {
            name = m2.getName().replace("_", " ");
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            result.add(name);
        }

        String json = new Gson().toJson(result);
        return json;
    }

    @GetMapping("/get_aluno_professor")
    public String aulas() throws IOException {
        index_of_colors = 0;
        String str_file = Files.readString(Path.of("horario_sem_aulas_repetidas.json"));
        if (str_file.equals("")) {
            List<Convert_Aula_CSV_to_JSON> lista_de_aulas_com_aulas_unicas = aux.get_Dias_da_semana(aux.getAulas());
            System.out.println("Size: "+lista_de_aulas_com_aulas_unicas.size());
            String str_list = new Gson().toJson(lista_de_aulas_com_aulas_unicas);
            try {
                FileWriter file = new FileWriter("horario_sem_aulas_repetidas.json");
                file.write(str_list);
                file.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return str_list;
        }
        else return str_file;
    }

    @PostMapping("/post")
    public ResponseEntity<?> readPost(@RequestBody MetodosSelecionados json_metodos) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        MetodosSelecionados metodos = new MetodosSelecionados(json_metodos.getMetodos_aulas(), json_metodos.getMetodos_avaliacoes());
        for (String n : metodos.getList_metodos_aulas()) {
            System.out.println(n);
        }
        for (String m : metodos.getList_metodos_avaliacoes())
            System.out.println(m);
        aux.invoke_method("presevar_caracteristica_da_aula");
        return new ResponseEntity<>("CHEGOU", HttpStatus.OK);
    }

    @PostMapping("/obter_aulas_da_UC_escolhida")
    public String obter_aulas_da_UC_escolhida(@RequestBody UC_escolhida uc) {
        List<Slot_horario_semestral> slots = new ArrayList<>();

        String[] horarios_das_aulas = aux.split_list_elements(uc.getHoras());
        String[] dias_de_semana = aux.split_list_elements(uc.getDias());
        List<String> datas = uc.getDatas();
        List<String> horas_repetidas = uc.getHoras_repetidas();

        String color= aux.setColor_evento(colors,index_of_colors);
        index_of_colors++;
        String sigla = aux.obter_sigla_da_uc(uc.getUnidade_de_execucao());

        for (int i = 0; i < horarios_das_aulas.length; i++) {
            String dia_de_sem = dias_de_semana[i];
            if (i>0) {dia_de_sem = aux.substring_a_str(dia_de_sem);horarios_das_aulas[i]=aux.substring_a_str(horarios_das_aulas[i]);}
            String[] hora_inicio_fim = horarios_das_aulas[i].split(";");
//            System.out.println(hora_inicio_fim[0] + " "+ hora_inicio_fim[1]);
            String id = uc.getTurno() + dia_de_sem + hora_inicio_fim[0]+hora_inicio_fim[1];
            String informacao_detalhada = uc.getUnidade_de_execucao() +" "+"   Semanas: ";
            System.out.println("\nDia de sem: "+dia_de_sem+" Horas: "+horarios_das_aulas[i]);
            List<Integer> number_of_weeks = aux.get_number_of_weeks_of_slot(datas, horas_repetidas, primeiro_dia_de_aulas_cal,
                    dia_de_sem, horarios_das_aulas[i]);
            Collections.sort(number_of_weeks);
//            System.out.println(aux.reduzir_list_number_of_weeks(number_of_weeks));
            String number_of_weeks_reduzida = aux.reduzir_list_number_of_weeks(number_of_weeks);
            informacao_detalhada = informacao_detalhada.concat(number_of_weeks_reduzida);
            String text = sigla + " " + number_of_weeks_reduzida;

            //"2022-12-06T10:30:00"
            String data_ajustada = aux.ajustar_data_horario_sem(dia_de_sem);
            String start = data_ajustada + "T" + hora_inicio_fim[0];
            String end = data_ajustada + "T" + hora_inicio_fim[1];

            slots.add(new Slot_horario_semestral(id, text, start, end, color,informacao_detalhada));
        }
        System.out.println(new Gson().toJson(slots));
        return new Gson().toJson(slots);
    }

}