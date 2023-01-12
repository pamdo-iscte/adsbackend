package com.backendads.backendads.controller;

import Files.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.nio.file.Files;


@RestController
public class HelloController {
    private String primeiro_dia_de_aulas = "2022/09/12";
    private final FuncoesAuxiliares aux = new FuncoesAuxiliares();
    private Calendar primeiro_dia_de_aulas_cal = aux.setCalendar(Calendar.getInstance(),primeiro_dia_de_aulas.split("/"));

    private final List<String> colors = Arrays.asList("#1cceb1", "#97fca3", "#5d8ce9","#6cda72","#a1f2e5","#9799fc","#fcf897");

    private final String dir_horariosCriados="HorariosCriados";
    private final String dir_horariosCompletos="HorariosCompletos";
    private final String dir_upload_horarios="Upload_de_Horarios";
    private final String dir_upload_avaliacoes="Upload_de_Avaliacoes";
    private final String dir_caracterizacao_das_salas="Caracterizacao_das_Salas";
    private String file_avaliacoes="Upload_de_Avaliacoes/ADS - Avaliações 1º semestre 2022-23.csv";
    private String file_das_aulas = "Upload_de_Horarios/2- ADS - Horários 1º sem 2022-23.csv";
    private Main main;

    @GetMapping("/get_metodos")
    public String get_metodos() {
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
        File tempFile = new File(dir);
        if (!tempFile.exists()) {
//            List<Convert_Aula_CSV_to_JSON> lista_de_aulas_com_aulas_unicas = aux.get_Dias_da_semana(aux.getAulas(file_das_aulas_a_ser_usado));
            List<Convert_Aula_CSV_to_JSON> lista_de_aulas_com_aulas_unicas = aux.get_Dias_da_semana(aux.getAulas(file_das_aulas));
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
                return new Gson().toJson(Files.readString(Path.of(dir)));
            } catch (IOException e) {
                return new Gson().toJson("Ocorreu um erro");
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
        return main.start(aulas,avaliacoes,json_metodos.isCheckbox(),json_metodos.getNum(),aux);
    }

    @PostMapping("/obter_aulas_da_UC_escolhida")
    public String obter_aulas_da_UC_escolhida(@RequestBody UC_escolhida uc) {
        List<Slot_horario_semestral> slots = new ArrayList<>();


        String[] horarios_das_aulas = aux.split_list_elements(uc.getHoras());
        String[] dias_de_semana = aux.split_list_elements(uc.getDias());
        List<String> datas = uc.getDatas();
//        System.out.println(datas.toString());
        List<String> horas_repetidas = uc.getHoras_repetidas();

        int index_of_colors = 0;
        if (uc.getSelecionados() != null) index_of_colors = uc.getSelecionados().size()-1;

        String color= aux.setColor_evento(colors,index_of_colors);
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

            Slot_horario_semestral new_slot =new Slot_horario_semestral(id, text, start, end, color,informacao_detalhada,uc.getTurno(),dia_de_sem);
            slots.add(new_slot);

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

            aux.guardar_horario_completo(slots.getAulas(), Integer.parseInt(slots.getNum()),dir_horariosCompletos,file_avaliacoes);
            return new Gson().toJson("Horário guardado");
        } catch (IOException e) {
            String mensagem_de_erro = "Ocorreu um erro ao guardar o horário do aluno/docente número "+slots.getNum();
            System.err.println(mensagem_de_erro);
            return new Gson().toJson(mensagem_de_erro);
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
//        System.out.println(data);
        calendar = aux.setCalendar(calendar,data.split("-"));

        List<Slot_horario_semestral> slots = aux.read_file(dir_horariosCompletos,num);
//        System.out.println("        Size: "+slots.size());
        List<Slot_horario_semestral> horario_da_semana = new ArrayList<>();
        List<Slot_horario_semestral> avaliacoes_grandes = new ArrayList<>();

        List<String> turnos_UCs = new ArrayList<>();
        List<String> cores_da_semana = this.colors;
        List<String> cores_das_avaliacoes = Arrays.asList("#f69e51","#e96f6d","#bee96d","#6dcae9","#1cba90");
        int index_cores_das_avaliacoes = 0;

        for (Slot_horario_semestral slot :slots) {
//            System.out.println(slot.getCalendar().getTime());
//            System.out.println(calendar.getTime());
//            if (slot.getTurno() == null) {
//                System.out.println("\n"+slot.getStart() + " "+slot.getEnd()+" "+!slot.getStart().split("T")[0].equals(slot.getEnd().split("T")[0]));
//                System.out.println(!aux.check_se_avaliacao_esta_entre_datas(calendar,slot.getStart(),slot.getEnd()));
//            }
            if (!slot.getStart().split("T")[0].equals(slot.getEnd().split("T")[0]) && !aux.check_se_avaliacao_esta_entre_datas(calendar,slot.getStart(),slot.getEnd())) {
                avaliacoes_grandes.add(slot);
            }
            else if (calendar.get(Calendar.WEEK_OF_YEAR) == slot.getCalendar().get(Calendar.WEEK_OF_YEAR)) {
                horario_da_semana.add(slot);
                System.out.println(slot);
                if (!turnos_UCs.contains(slot.getTurno())) turnos_UCs.add(slot.getTurno());
            }
        }

        System.out.println("Horario da semana: "+horario_da_semana.size());

        for (Slot_horario_semestral aula: horario_da_semana) {
            String color ="";
            if (aula.getTurno() != null) {
                for (int i = 0; i < turnos_UCs.size(); i++) {
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
            }
            else if (cores_das_avaliacoes.size() > index_cores_das_avaliacoes) {
                color = cores_das_avaliacoes.get(index_cores_das_avaliacoes);
                index_cores_das_avaliacoes++;
            }
            else {
                Random random = new Random();
                int nextInt = random.nextInt(0xffffff + 1);
                color = String.format("#%06x", nextInt);
            }
            aula.setBackColor(color);
        }
//        System.out.println("Avaliacoes_Grandes: "+avaliacoes_grandes);
//        List<List<Slot_horario_semestral>> result = new ArrayList<>();
//        result.add(horario_da_semana);
//        result.add(avaliacoes_grandes);
//        System.out.println(result.get(0).toString());
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

    @PostMapping("/deleteschedule")
    public void delete_schedule(@RequestBody JsonNode json) throws IOException {
        String num = json.get("num").asText();
        new FileWriter(dir_horariosCriados + "\\" + num + ".txt", false).close();

    }

    @GetMapping("/check_se_existe_horario")
    public String check_se_existe_horario() {
        File dir = new File(dir_upload_horarios);
        if (dir.isDirectory()) {
            File[] files = Objects.requireNonNull(dir.listFiles());
            if (files.length != 0)
                return new Gson().toJson(files[0].getName());
        }
        return new Gson().toJson("");
    }

    @GetMapping("/check_if_all_files_exist")
    public String check_if_all_files_exist() {
        File dir = new File(dir_upload_horarios);
        File dir1 = new File(dir_upload_avaliacoes);
        File dir2 = new File(dir_caracterizacao_das_salas);
        if (dir.isDirectory() && dir1.isDirectory() && dir2.isDirectory()) {
            File[] files = Objects.requireNonNull(dir.listFiles());
            File[] files1 = Objects.requireNonNull(dir1.listFiles());
            File[] files2 = Objects.requireNonNull(dir2.listFiles());
            if (files.length != 0 && files1.length!=0 && files2.length!=0)
                return new Gson().toJson(true);
        }
        return new Gson().toJson(false);
    }
    @GetMapping("/check_se_existe_avaliacao")
    public String check_se_existe_avaliacao() {
        File dir = new File(dir_upload_avaliacoes);
        if (dir.isDirectory()) {
            File[] files = Objects.requireNonNull(dir.listFiles());
            if (files.length != 0)
                return new Gson().toJson(files[0].getName());
        }
        return new Gson().toJson("");
    }
    @GetMapping("/check_se_existe_caracterizacao_das_salas")
    public String check_se_existe_caracterizacao_das_salas() {
        File dir = new File(dir_caracterizacao_das_salas);
        if (dir.isDirectory()) {
            File[] files = Objects.requireNonNull(dir.listFiles());
            if (files.length != 0)
                return new Gson().toJson(files[0].getName());
        }
        return new Gson().toJson("");
    }

    @PostMapping("/upload_caracterizacao_salas")
    public String upload_caracterizacao_salas(@RequestParam("file") MultipartFile file) {
        String result = aux.getFileofDirectory(dir_caracterizacao_das_salas,file);
        if (result.equals("")) return new Gson().toJson("Erro no upload");
        main.setFile_caracterizacao_das_salas(result);
        return new Gson().toJson("Upload concluído com sucesso");
    }

    @PostMapping("/upload_avaliacoes")
    public String upload_avaliacoes(@RequestParam("file") MultipartFile file) {
        String result = aux.getFileofDirectory(dir_upload_avaliacoes,file);
        if (result.equals("")) return new Gson().toJson("Erro no upload");
        main.setFile_avaliacoes_1sem(result);
        this.file_avaliacoes = dir_upload_avaliacoes+"/"+result;
        return new Gson().toJson("Upload concluído com sucesso");
    }

    @PostMapping("/upload")
    public String upload_horario(@RequestParam("file") MultipartFile file) {
        String result = aux.getFileofDirectory(dir_upload_horarios,file);
        if (result.equals("")) return new Gson().toJson("Erro no upload");
        main.setFile_horario_1sem(result);
        this.file_das_aulas = dir_upload_horarios+"/"+result;
        return new Gson().toJson("Upload concluído com sucesso");
    }

    @GetMapping("/start_main")
    public void start_main() {
        main = new Main();
    }

    @PostMapping("/definir_primeiro_dia")
    public void definir_primeiro_dia(@RequestBody JsonNode json) {
        String data = json.get("startDate").asText();
        System.out.println(data);
        String ano_full = data.split("T")[0].split("-")[0];
        String ano = ano_full.substring(2);
        System.out.println(ano);
        primeiro_dia_de_aulas = data.split("T")[0].replace("-","/");
        primeiro_dia_de_aulas_cal = aux.setCalendar(Calendar.getInstance(),primeiro_dia_de_aulas.split("/"));
        main.setAno_valor(ano);
    }
    @PostMapping("/csv")
    public String csv() {
        return aux.to_csv("Aulas.txt");
    }

}