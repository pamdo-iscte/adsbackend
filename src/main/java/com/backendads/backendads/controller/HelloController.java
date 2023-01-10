package com.backendads.backendads.controller;

import Files.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.nio.file.Files;
import java.util.stream.Stream;


@RestController
public class HelloController {
    private String primeiro_dia_de_aulas = "2022/09/12";
    private final FuncoesAuxiliares aux = new FuncoesAuxiliares();
    private Calendar primeiro_dia_de_aulas_cal = aux.setCalendar(Calendar.getInstance(),primeiro_dia_de_aulas.split("/"));

    private final List<String> colors = Arrays.asList("#1cceb1", "#97fca3", "#5d8ce9","#6cda72","#a1f2e5","#9799fc","#fcf897");
    private int index_of_colors = 0;

    private final String dir_horariosCriados="HorariosCriados";
    private final String dir_horariosCompletos="HorariosCompletos";
    private final String dir_upload_horarios="Upload_de_Horarios";
    private final String dir_upload_avaliacoes="Upload_de_Avaliacoes";
    private final String dir_caracterizacao_das_salas="Caracterizacao_das_Salas";
    private String file_das_aulas_a_ser_usado = "";
    private String file_das_avaliacoes_a_ser_usado = "";
    private Main main;

    @GetMapping("/get_metodos")
    public String get_metodos() {
        main = new Main();
        List<List<String>> metodos = new ArrayList<>();
        List<String> nomes_metodos_aulas = new ArrayList<>();
        List<String> nomes_metodos_avaliacoes = new ArrayList<>();

        Class<MetodosParaAulas> aulas = MetodosParaAulas.class;
        Class<MetodosdeAvaliacao> avaliacoes = MetodosdeAvaliacao.class;
        Method[] methods_aulas = aulas.getDeclaredMethods();
        Method[] methods_avaliacoes = avaliacoes.getDeclaredMethods();

        for (Method metodo_aula : methods_aulas) nomes_metodos_aulas.add(aux.replace_nome_metodo(metodo_aula.getName()));
        for (Method metodo_avaliacao : methods_avaliacoes) nomes_metodos_avaliacoes.add(aux.replace_nome_metodo(metodo_avaliacao.getName()));

        metodos.add(nomes_metodos_aulas);metodos.add(nomes_metodos_avaliacoes);
        return new Gson().toJson(metodos);
    }

    @GetMapping("/get_aluno_professor")
    public String aulas()  {
        String dir = "horario_sem_aulas_repetidas.json";
        index_of_colors = 0;
        File tempFile = new File(dir);
        if (!tempFile.exists()) {
//            List<Convert_Aula_CSV_to_JSON> lista_de_aulas_com_aulas_unicas = aux.get_Dias_da_semana(aux.getAulas(file_das_aulas_a_ser_usado));
            List<Convert_Aula_CSV_to_JSON> lista_de_aulas_com_aulas_unicas = aux.get_Dias_da_semana(aux.getAulas(file_das_aulas_a_ser_usado));
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
        else {
            try {
                return Files.readString(Path.of(dir));
            } catch (IOException e) {
                return "Ocorreu um erro";
            }
        }
    }

    @PostMapping("/obter_metodos_selecionados")
    public String obter_metodos_selecionados(@RequestBody MetodosSelecionados json_metodos) {
        List<String> aulas = json_metodos.getAulas();
        System.out.println("Aulas antes do replace: "+aulas);
        List<String> avaliacoes = json_metodos.getAvaliacoes();
        System.out.println("Avaliacoes antes do replace: "+avaliacoes);
        aulas.replaceAll(s -> (s.substring(0, 1).toLowerCase() + s.substring(1)).replace(" ", "_"));
        avaliacoes.replaceAll(s -> (s.substring(0, 1).toLowerCase() + s.substring(1)).replace(" ", "_"));

        System.out.println("Metodos Aulas: "+aulas);
        System.out.println("Metodos Aulas: "+avaliacoes);
        main.start(aulas,avaliacoes);
        //objetivo é dar return ao filename do horario criado
        return "";
    }

    @PostMapping("/obter_aulas_da_UC_escolhida")
    public String obter_aulas_da_UC_escolhida(@RequestBody UC_escolhida uc) {
        List<Slot_horario_semestral> slots = new ArrayList<>();

        String[] horarios_das_aulas = aux.split_list_elements(uc.getHoras());
        String[] dias_de_semana = aux.split_list_elements(uc.getDias());
        List<String> datas = uc.getDatas();
        System.out.println(datas.toString());
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
            String informacao_detalhada = uc.getUnidade_de_execucao() +" | Curso(s): "+uc.getCurso()+" | Semanas: ";
//            System.out.println("\nDia de sem: "+dia_de_sem+" Horas: "+horarios_das_aulas[i]);
            List<Integer> number_of_weeks = aux.get_number_of_weeks_of_slot(datas, horas_repetidas, primeiro_dia_de_aulas_cal,
                    dia_de_sem, horarios_das_aulas[i]);
            Collections.sort(number_of_weeks);
//            System.out.println(aux.reduzir_list_number_of_weeks(number_of_weeks));
            String number_of_weeks_reduzida = aux.reduzir_list_number_of_weeks(number_of_weeks);
            informacao_detalhada = informacao_detalhada.concat(number_of_weeks_reduzida);
            String text = sigla + " | " + number_of_weeks_reduzida;

            //"2022-12-06T10:30:00"
            String data_ajustada = aux.ajustar_data_horario_sem(dia_de_sem);
            String start = data_ajustada + "T" + hora_inicio_fim[0];
            String end = data_ajustada + "T" + hora_inicio_fim[1];

            slots.add(new Slot_horario_semestral(id, text, start, end, color,informacao_detalhada,uc.getTurno(),dia_de_sem));
        }
//        System.out.println(new Gson().toJson(slots));
        return new Gson().toJson(slots);
    }



    @PostMapping("/guardar_horario")
    public String guardar_horario(@RequestBody ReceiveClasses slots)  {
        try {
            FileOutputStream fo = new FileOutputStream(dir_horariosCriados + "\\" + slots.getNum() + ".txt");
            ObjectOutputStream oo = new ObjectOutputStream(fo);

            for (Slot_horario_semestral slot : slots.getSlots()) {
                System.out.println(slot.toString());
                oo.writeObject(slot);
            }
            oo.writeObject(null);

            oo.close();
            fo.close();

            aux.guardar_horario_completo(slots.getAulas(), Integer.parseInt(slots.getNum()),dir_horariosCompletos);
            return "Horário guardado";
        } catch (IOException e) {
            String mensagem_de_erro = "Ocorreu um erro ao guardar o horário do aluno/docente número "+slots.getNum();
            System.err.println(mensagem_de_erro);
            return mensagem_de_erro;
        }
    }

    @PostMapping("/fileexists")
    public String FileExists(@RequestBody JsonNode json) {
        String file = json.get("file").asText();
        System.out.println(dir_horariosCriados + "\\"+file+".txt");
        File tempFile = new File(dir_horariosCriados + "\\"+file+".txt");
        boolean exists = tempFile.exists();
        System.out.println(exists);//true
        return new Gson().toJson(exists);
    }

    @PostMapping("/obter_horario_de_uma_semana")
    public String obter_horario_de_uma_semana(@RequestBody JsonNode json) {
        String data = json.get("data").asText().split("T")[0];
        String num = json.get("num").asText();

//        System.out.println(data + " "+num);
        Calendar calendar = Calendar.getInstance();
        calendar = aux.setCalendar(calendar,data.split("-"));

        List<Slot_horario_semestral> slots = aux.read_file(dir_horariosCompletos,num);
//        System.out.println("        Size: "+slots.size());
        List<Slot_horario_semestral> horario_da_semana = new ArrayList<>();

        List<String> turnos_UCs = new ArrayList<>();
        List<String> cores_da_semana = this.colors;

        for (Slot_horario_semestral slot :slots) {
//            System.out.println(slot.getCalendar().getTime());
//            System.out.println(calendar.getTime());
            if (calendar.get(Calendar.WEEK_OF_YEAR) == slot.getCalendar().get(Calendar.WEEK_OF_YEAR)) {
                horario_da_semana.add(slot);
                if (!turnos_UCs.contains(slot.getTurno())) turnos_UCs.add(slot.getTurno());
            }
        }

        for (Slot_horario_semestral aula: horario_da_semana) {
            String color ="";
            for (int i=0; i<turnos_UCs.size();i++) {
                if (turnos_UCs.get(i).equals(aula.getTurno())) {
                    if (i == cores_da_semana.size()) {
                        Random random = new Random();
                        int nextInt = random.nextInt(0xffffff + 1);
                        cores_da_semana.add(String.format("#%06x", nextInt));
                    }
                    color = cores_da_semana.get(i);
                    break;
                }
            }
            aula.setBackColor(color);
        }

        return new Gson().toJson(horario_da_semana);
    }


    @PostMapping("/reformular_horario")
    public String reformular_horario(@RequestBody JsonNode json) {
        String num = json.get("num").asText();

        List<Slot_horario_semestral> slots = aux.read_file(dir_horariosCriados,num);

        List<String> turnos = new ArrayList<>();
        if (slots.isEmpty()) return "[]";

        for (Slot_horario_semestral slot: slots) {
            String new_date = aux.ajustar_data_horario_sem(slot.getDia_da_sem());
            String[] split_start = slot.getStart().split("T");
            String[] split_end = slot.getEnd().split("T");
            slot.setStart(new_date+"T"+split_start[1]);
            slot.setEnd(new_date+"T"+split_end[1]);
            slot.setCal(null);
            if (!turnos.contains(slot.getTurno())) turnos.add(slot.getTurno());
        }
        String slots_json = new Gson().toJson(slots);
        String turnos_json = new Gson().toJson(turnos);

        return "["+slots_json+","+turnos_json+"]";
    }

    @GetMapping("/download_horario")
    public File download_horario(@RequestBody JsonNode json) {
        String filename_json = json.get("filename").asText();
        return new File(dir_upload_horarios + "\\"+filename_json);
    }
//    @RequestMapping("/download")
//    public ResponseEntity downloadFile1(@RequestParam String fileName) throws IOException {
//
//        File file = new File(fileName);
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .contentLength(file.length());
//

    @PostMapping("/deleteschedule")
    public void delete_schedule(@RequestBody JsonNode json) throws IOException {
        String num = json.get("num").asText();
        new FileWriter(dir_horariosCriados + "\\" + num + ".txt", false).close();

    }

    @GetMapping("/check_se_existe_caracterizacao_das_salas")
    public String check_se_existe_caracterizacao_das_salas() {
        File dir = new File(dir_caracterizacao_das_salas);
        if (dir.isDirectory()) {
            File[] files = Objects.requireNonNull(dir.listFiles());
            if (files.length != 0)
                return files[0].getName();
        }
        return "";
    }

    @PostMapping("/upload_caracterizacao_salas")
    public String upload_caracterizacao_salas(@RequestParam("file") MultipartFile file) {
        String result =  aux.upload_file(file,dir_caracterizacao_das_salas);
        if (result.equals("")) return "Erro no upload";
        main.setFile_caracterizacao_das_salas(result);
        return "Upload concluído com sucesso";
    }

    @PostMapping("/upload_avaliacoes")
    public String upload_avaliacoes(@RequestParam("file") MultipartFile file) {
        String result =  aux.upload_file(file,dir_upload_avaliacoes);
        if (result.equals("")) return "Erro no upload";
        main.setFile_avaliacoes_1sem(result);
        main.readFile_slotsAvaliacao();
        return "Upload concluído com sucesso";
    }

    @PostMapping("/upload")
    public String upload_horario(@RequestParam("file") MultipartFile file) {
        String result =  aux.upload_file(file,dir_upload_horarios);
        if (result.equals("")) return "Erro no upload";
        main.setFile_horario_1sem(result);
        main.readFile_slotsAula();
        return "Upload concluído com sucesso";
    }


}