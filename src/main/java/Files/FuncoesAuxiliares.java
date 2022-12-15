package Files;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

public class FuncoesAuxiliares {
    private final String file_horarios_1_sem = "ADS - Horários 1º sem 2022-23.csv";


    public List<Convert_Aula_CSV_to_JSON> get_Dias_da_semana(List<Convert_Aula_CSV_to_JSON> aulas) {
        List<Integer> aulas_para_remover = new ArrayList<>();
        List<Integer> aulas_para_percorrer = new ArrayList<>(IntStream.iterate(0, i -> i + 1).limit(aulas.size()).boxed().toList());

        for (int j = aulas_para_percorrer.get(aulas_para_percorrer.size()-1); j>=0;j--) {
            //caso o valor de aulas_para_percorrer.get(j) != j significa que já foi verificado
            if (aulas_para_percorrer.get(j) != j) continue;

            Convert_Aula_CSV_to_JSON current = aulas.get(j);

            if (current.getDias().equals("")) {
                int[] indexes = IntStream.range(0, aulas.size()).filter(i -> aulas.get(i).getTurno().equals(current.getTurno())).toArray();
                List<String> list_of_dias_da_sem = new ArrayList<>();
                List<String> horarios_das_aulas_sem_repetidos = new ArrayList<>();
                for (int i=indexes.length-1;i>=0;i--) {
                    int index = indexes[i];
                    Convert_Aula_CSV_to_JSON aula_do_mesmo_turno = aulas.get(index);
                    //Caso entre no if o valor de aulas_para_percorrer(index) passa para -1 para indicar que já não precisa de ser verificado
                    if (j != index) {aulas_para_percorrer.set(index,-1);aulas_para_remover.add(index);}
                    //Se o slot da aula atual não estiver ainda guardado entra no if e guarda o dia da semana em que ocorre e o respetivo horario
                    String aula_hora = aula_do_mesmo_turno.getHora_inicio()+";"+aula_do_mesmo_turno.getHora_fim();
                    if (!check_se_ja_esta_guardado(list_of_dias_da_sem,horarios_das_aulas_sem_repetidos,aula_do_mesmo_turno)) {
                        list_of_dias_da_sem.add(aula_do_mesmo_turno.getDia_da_semana());
                        horarios_das_aulas_sem_repetidos.add(aula_hora);
                    }
                    //Nas linhas seguintes guarda todas as datas e os respetivos horarios em que ocorre esta aula
                    current.addHoras(aula_hora);
                    current.addDatas(aula_do_mesmo_turno.getData());
                }
                String dias = list_of_dias_da_sem.toString().replace("[","").replace("]","");
                String horas = horarios_das_aulas_sem_repetidos.toString().replace("[","").replace("]","");
                current.setDias(dias);
                current.setHoras(horas);
            }
        }
        return remover_aulas(aulas,aulas_para_remover);
    }



    private boolean check_se_ja_esta_guardado(List<String> list_of_dias_da_sem, List<String> horarios_das_aulas, Convert_Aula_CSV_to_JSON aula_do_mesmo_turno) {
        for (int i=0; i<list_of_dias_da_sem.size(); i++) {
            String[] temp_horas = horarios_das_aulas.get(i).split(";");
            if (list_of_dias_da_sem.get(i).equals(aula_do_mesmo_turno.getDia_da_semana()) &&
                    temp_horas[0].equals(aula_do_mesmo_turno.getHora_inicio()) &&
                    temp_horas[1].equals(aula_do_mesmo_turno.getHora_fim())) {
                return true;
            }
        }
        return false;
    }

    public List<Convert_Aula_CSV_to_JSON> getAulas() {
        List<Convert_Aula_CSV_to_JSON> all_aulas = new ArrayList<>();
        try {
            FileReader filereader = new FileReader(file_horarios_1_sem);

            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            boolean first_line = true;

            while ((nextRecord = csvReader.readNext()) != null) {
                if (first_line) first_line = false;
                else {
                    String temp = nextRecord[0].replace("'",",");
                    String[] line = temp.split(";");
                    String turno = line[2];
                    String sala = "";
                    String data ="";
                    if (line.length >=13) sala = line[12];
                    if (line.length >= 11) data = line[10];
                    Convert_Aula_CSV_to_JSON convert = new Convert_Aula_CSV_to_JSON(line[0],line[1],turno,line[3],line[7],
                            line[8],line[9],data,sala,"");

                    all_aulas.add(convert);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return all_aulas;
    }

    public List<Convert_Aula_CSV_to_JSON> remover_aulas(List<Convert_Aula_CSV_to_JSON> aulas, List<Integer> remover) {
        remover.sort(Collections.reverseOrder());
        for (int i:remover) {
            aulas.remove(i);
        }
        return aulas;
    }

//    public List<Convert_Aula_CSV_to_JSON> juntar_aulas(List<Convert_Aula_CSV_to_JSON> aulas, List<String> datas) {
//        HashSet<String> hset_datas = new HashSet<>(datas);
//        for (String d : hset_datas) {
//            int[] indexes = IntStream.range(0, aulas.size()).filter(i -> aulas.get(i).getData().equals(d)).toArray();
//            Comparator<Convert_Aula_CSV_to_JSON> compare = Comparator.comparing(Convert_Aula_CSV_to_JSON::getTurno);
//
//        }
//        aulas.sort(compare);
//        List<Integer> aulas_para_remover = new ArrayList<>();
//
//        for (Convert_Aula_CSV_to_JSON current : aulas) {
//            int index = aulas.indexOf(current);
//            if (index == 0) continue;
//            Convert_Aula_CSV_to_JSON previous = aulas.get(index-1);
//
//            if (string_equals(previous.getData(),current.getData()) && string_equals(previous.getUnidade_de_execucao(),current.getUnidade_de_execucao()) &&
//                    string_equals(previous.getTurno(),current.getTurno()) && string_equals(previous.getSala(), current.getSala())) {
//                String hora_inicio = previous.getHora_inicio();
//                if (!previous.compareHoras(current.getHora_inicio(),true)) hora_inicio = current.getHora_inicio();
//                String hora_fim = current.getHora_fim();
//                if (!previous.compareHoras(current.getHora_fim(),false)) hora_fim = previous.getHora_fim();
//                previous.setHora_inicio(hora_inicio);
//                previous.setHora_fim(hora_fim);
//
//                aulas_para_remover.add(index);
//            }
//        }
//
//        return remover_aulas(aulas,aulas_para_remover);
//    }
//
//    public Boolean string_equals(String s1,String s2) {
//        return s1.equals(s2);
//    }
//
//    public List<String> get_slots_30_min(String hora_inicio, String hora_final) {
//        String[] fields_hora_inicio = hora_inicio.split(":");
//        String[] fields_hora_final = hora_final.split(":");
//        int[] int_hora_ini = {Integer.parseInt(fields_hora_inicio[0]),Integer.parseInt(fields_hora_inicio[1]),Integer.parseInt(fields_hora_inicio[2])};
//        int[] int_hora_fin = {Integer.parseInt(fields_hora_final[0]),Integer.parseInt(fields_hora_final[1]),Integer.parseInt(fields_hora_final[2])};
//
//        int number_of_slots = 0;
//
//        if (int_hora_fin[1] < int_hora_ini[1]) {
//            number_of_slots = (int_hora_fin[0] - int_hora_ini[0])*2 - 1;
//        } else {
//            number_of_slots = (int_hora_fin[0] - int_hora_ini[0])*2 + (int_hora_fin[1] - int_hora_ini[1])/30;
//        }
//
//        for (int i=0; i<number_of_slots;i++) {
//
//        }
//        return new ArrayList<>();
//    }

    //Apenas para testar
    public void invoke_method(String name) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<MetodosParaAulas> class_metodos_aulas = MetodosParaAulas.class;
        Object t = class_metodos_aulas.newInstance();
        Method[] metodos = class_metodos_aulas.getDeclaredMethods();
        for (Method m: metodos) {
            if (m.getName().equals(name)) {
                System.out.println(m.getName().equals(name));
                m.invoke(t,1);
            }
        }
    }

    public Calendar setCalendar(Calendar calendar, String[] data_fields) {
        calendar.set(Integer.parseInt(data_fields[0]),Integer.parseInt(data_fields[1])-1,Integer.parseInt(data_fields[2]));
        return calendar;
    }

    public int get_int_dia_de_semana(String dia_da_sem) {
        return switch (dia_da_sem) {
            case "Dom" -> Calendar.SUNDAY;
            case "Seg" -> Calendar.MONDAY;
            case "Ter" -> Calendar.TUESDAY;
            case "Qua" -> Calendar.WEDNESDAY;
            case "Qui" -> Calendar.THURSDAY;
            case "Sex" -> Calendar.FRIDAY;
            case "Sáb" -> Calendar.SATURDAY;
            default -> 0;
        };
    }

    public String ajustar_data_horario_sem(String dia_da_sem_da_aula) {
        Calendar c = Calendar.getInstance();
        int dif_entre_dias = get_int_dia_de_semana(dia_da_sem_da_aula) - c.get(Calendar.DAY_OF_WEEK);
//        System.out.println("Dia da sem de hj: "+c.get(Calendar.DAY_OF_WEEK)+ " Dia da sem da aula: "+get_int_dia_de_semana(dia_da_sem_da_aula) +
//                " Diferença: "+dif_entre_dias);
        c.add(Calendar.DAY_OF_MONTH, dif_entre_dias);
        int month=c.get(Calendar.MONTH)+1;
        int day=c.get(Calendar.DAY_OF_MONTH);
        String dayS= String.valueOf(day);
        if(day<10 && dayS.length() < 2){
            dayS="0"+dayS;
        }
        return c.get(Calendar.YEAR)+"-"+month+"-"+dayS;
    }

    public String[] split_list_elements(String s) {
        return s.split(",");
    }

    public List<Integer> get_number_of_weeks_of_slot(List<String> datas, List<String> horas_repetidas, Calendar primeiro_dia_de_aulas_cal,
                                       String dia_da_sem, String horarios_das_aulas) {
        List<Integer> number_of_weeks = new ArrayList<>();
        int sem_primeiro_dia_de_aulas = primeiro_dia_de_aulas_cal.get(Calendar.WEEK_OF_YEAR);
//        System.out.println("PRMIERO     "+primeiro_dia_de_aulas_cal.getTime());
        Calendar calendar = Calendar.getInstance();
        for(int j = 0; j < datas.size(); j++) {
            String[] data_fields = datas.get(j).split("/");
            calendar = setCalendar(calendar,data_fields);
//            System.out.println("Data: "+datas.get(j) + " Calendar: "+calendar.getTime());
            int int_dia_de_semana = get_int_dia_de_semana(dia_da_sem);
//            System.out.println(datas.get(j) + " "+dia_da_sem + " "+int_dia_de_semana + " Calendar: "+calendar.get(Calendar.DAY_OF_WEEK));
//            System.out.println("Horas repetidas: "+horas_repetidas.get(j) + " Horatios das aulas: "+horarios_das_aulas);
            if (int_dia_de_semana == calendar.get(Calendar.DAY_OF_WEEK) &&
                    horas_repetidas.get(j).equals(horarios_das_aulas)) {
                number_of_weeks.add(calendar.get(Calendar.WEEK_OF_YEAR) +1 - sem_primeiro_dia_de_aulas);
            }
        }
//        System.out.println(number_of_weeks);
        return number_of_weeks;
    }

    public String reduzir_list_number_of_weeks(List<Integer> number_of_weeks) {
        String str_number_of_weeks = "";
        if (number_of_weeks.size() == 1) return String.valueOf(number_of_weeks.get(0))+";";
        for (int j=0; j<number_of_weeks.size()-1;j++) {
            int next_iteration = j+1;
            int dif = number_of_weeks.get(next_iteration) - number_of_weeks.get(j);
            while(dif <= 1) {
                next_iteration++;
                if (next_iteration == number_of_weeks.size()) break;
                dif = number_of_weeks.get(next_iteration) - number_of_weeks.get(next_iteration-1);
            }
            String concat = "";
            if (next_iteration-1 == j)
                concat = number_of_weeks.get(j)+"; ";
            else
                concat =number_of_weeks.get(j)+"-"+number_of_weeks.get(next_iteration-1)+"; ";
            str_number_of_weeks=str_number_of_weeks.concat(concat);
            j=next_iteration-1;
        }
        return str_number_of_weeks;
    }

    public String setColor_evento(List<String> colors, int index_of_colors) {
        String color="";
        if (index_of_colors >= colors.size()) {
            Random random = new Random();
            int nextInt = random.nextInt(0xffffff + 1);
            color = String.format("#%06x", nextInt);
        } else {
            color = colors.get(index_of_colors);
        }
        return color;
    }

    public String substring_a_str(String str) {
        return str.substring(1);
    }

    public String obter_sigla_da_uc(String uc) {
        List<String> palavras_para_ignorar = Arrays.asList("de","e","dos");
        List<String> chars_para_ignorar = Arrays.asList("(",")","[","]");
        String[] fields = uc.split(" ");
        String sigla = "";

        for (String f: fields) {
            if (palavras_para_ignorar.contains(f)) continue;
            String c = String.valueOf(Character.toUpperCase(f.charAt(0)));
            if (chars_para_ignorar.contains(c)) continue;
            sigla = sigla.concat(c);
        }
        return sigla;
    }

    public String replace_nome_metodo(String metodo) {
        String name = metodo.replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}

