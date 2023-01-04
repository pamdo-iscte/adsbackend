package Files;

import com.opencsv.CSVReader;

import java.io.*;
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


    public Calendar setCalendar(Calendar calendar, String[] data_fields) {
        calendar.set(Integer.parseInt(data_fields[0]),Integer.parseInt(data_fields[1])-1,Integer.parseInt(data_fields[2]));
        return calendar;
    }

    public int get_int_dia_de_semana(String dia_da_sem) {
        String lower_case_dia_da_sem = dia_da_sem.toLowerCase();
        return switch (lower_case_dia_da_sem) {
            case "dom", "domingo" -> Calendar.SUNDAY;
            case "seg", "segunda", "segunda-feira" -> Calendar.MONDAY;
            case "ter", "terça", "terça-feira" -> Calendar.TUESDAY;
            case "qua", "quarta", "quarta-feira" -> Calendar.WEDNESDAY;
            case "qui", "quinta", "quinta-feira" -> Calendar.THURSDAY;
            case "sex", "sexta", "sexta-feira" -> Calendar.FRIDAY;
            case "sáb", "sábado" -> Calendar.SATURDAY;
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
        String monthS = String.valueOf(month);
        if(month<10 && monthS.length() < 2){
            monthS="0"+monthS;
        }
        int day=c.get(Calendar.DAY_OF_MONTH);
        String dayS= String.valueOf(day);
        if(day<10 && dayS.length() < 2){
            dayS="0"+dayS;
        }
        return c.get(Calendar.YEAR)+"-"+monthS+"-"+dayS;
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

    public void guardar_horario_completo(List<Convert_Aula_CSV_to_JSON> slots, int num, String dir) {
        try {
            FileOutputStream fo = new FileOutputStream(dir + "\\"+num+".txt");
            ObjectOutputStream oo = new ObjectOutputStream(fo);

//            Calendar calendar = Calendar.getInstance();
            for (Convert_Aula_CSV_to_JSON slot: slots) {
                List<String> datas = slot.getDatas();
                List<String> horas = slot.getHoras_repetidas();

                for (int i=0; i<datas.size(); i++) {
                    String[] data_fields = datas.get(i).split("/");
                    String[] inicio_fim = horas.get(i).split(";");

//                    calendar = setCalendar(calendar,data_fields);
//                    System.out.println(datas.get(i) + " "+calendar.getTime());
                    String id = slot.getTurno() + slot.getDia_da_semana() + inicio_fim[0]+inicio_fim[1];
                    String text = obter_sigla_da_uc(slot.getUnidade_de_execucao()) + "      Sala: "+slot.getSala();

                    confirmar_formato_da_data(data_fields);
                    String data_da_aula = data_fields[0]+"-"+data_fields[1]+"-"+data_fields[2];
                    String start = data_da_aula+"T"+inicio_fim[0];
                    String end = data_da_aula+"T"+inicio_fim[1];

                    String informacao_detalhada = slot.getUnidade_de_execucao() +" | Curso(s): "+slot.getCurso() +" | Sala: "+slot.getSala();

                    Slot_horario_semestral new_slot = new Slot_horario_semestral(id,text,start,end,null,informacao_detalhada,slot.getTurno());
//                    new_slot.setCal(calendar);

//                    System.out.println(new_slot.getCalendar().getTime() + " "+new_slot.getStart());
                    oo.writeObject(new_slot);

                }
            }
            oo.writeObject(null);

            oo.close();
            fo.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void confirmar_formato_da_data(String[] data_fields) {
        int int_day = Integer.parseInt(data_fields[2]);
        int int_month = Integer.parseInt(data_fields[1]);
        int OTHER_YEAR_SIZE = 2;

        if(int_day<10 && data_fields[2].length() < 2) data_fields[2]="0"+data_fields[2];
        if (int_month<10 && data_fields[1].length() < 2) data_fields[1] = "O"+data_fields[1];
        if (data_fields[0].length() == OTHER_YEAR_SIZE) data_fields[0] = "20"+data_fields[0];

    }

    public List<Slot_horario_semestral> read_file(String dir, String num) {
        List<Slot_horario_semestral> slots = new ArrayList<>();
        try {
            FileInputStream fi = new FileInputStream(dir+"/"+num+".txt");
            ObjectInputStream oi = new ObjectInputStream(fi);

            while (true) {
                Slot_horario_semestral slot = (Slot_horario_semestral) oi.readObject();
                if (slot != null) {
                    slots.add(slot);
                    String data = slot.getStart().split("T")[0];
                    Calendar calendar = Calendar.getInstance();
                    slot.setCal(setCalendar(calendar,data.split("-")));
                    System.out.println(slot.getCalendar().getTime() + " "+slot.getStart());
                }
                else break;
            }

            oi.close();
            fi.close();

        } catch (IOException  e) {
            return slots;
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException");
        }
        return slots;
    }
}

