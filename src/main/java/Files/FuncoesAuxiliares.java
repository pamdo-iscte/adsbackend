package Files;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class FuncoesAuxiliares {
    private final String file_horarios_1_sem = "ADS - Horários 1º sem 2022-23.csv";


    public List<Convert_Aula_CSV_to_JSON> get_Dias_da_semana(List<Convert_Aula_CSV_to_JSON> aulas) {
        List<Integer> aulas_para_remover = new ArrayList<>();
        List<Integer> aulas_para_percorrer = new ArrayList<>(IntStream.iterate(0, i -> i + 1).limit(aulas.size()).boxed().toList());
        for (int j = aulas_para_percorrer.get(aulas_para_percorrer.size()-1); j>=0;j--) {
            if (aulas_para_percorrer.get(j) != j) continue;

            Convert_Aula_CSV_to_JSON current = aulas.get(j);
            if (current.getDias().equals("")) {
                int[] indexes = IntStream.range(0, aulas.size()).filter(i -> aulas.get(i).getTurno().equals(current.getTurno())).toArray();
                List<String> list_of_dias = new ArrayList<>();
                for (int i=indexes.length-1;i>=0;i--) {
                    int index = indexes[i];
                    Convert_Aula_CSV_to_JSON aula = aulas.get(index);
                    if (j != index) {aulas_para_percorrer.set(index,-1);aulas_para_remover.add(index);}
                    if (!list_of_dias.contains(aula.getDia_da_semana())) list_of_dias.add(aula.getDia_da_semana());
                }
                String dias = list_of_dias.toString().replace("[","").replace("]","");
                aulas.get(j).setDias(dias);
            }
        }
        return remover_aulas(aulas,aulas_para_remover);
    }

    public List<Convert_Aula_CSV_to_JSON> getAulas() {
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

    public List<Convert_Aula_CSV_to_JSON> juntar_aulas(List<Convert_Aula_CSV_to_JSON> aulas) {
        Comparator<Convert_Aula_CSV_to_JSON> compare = Comparator.comparing(Convert_Aula_CSV_to_JSON::getData);
        aulas.sort(compare);
        List<Integer> aulas_para_remover = new ArrayList<>();

        for (Convert_Aula_CSV_to_JSON current : aulas) {
            int index = aulas.indexOf(current);
            if (index == 0) continue;
            Convert_Aula_CSV_to_JSON previous = aulas.get(index-1);

            if (string_equals(previous.getData(),current.getData()) && string_equals(previous.getUnidade_de_execucao(),current.getUnidade_de_execucao()) &&
                    string_equals(previous.getTurno(),current.getTurno()) && string_equals(previous.getSala(), current.getSala())) {
                String hora_inicio = previous.getHora_inicio();
                if (!previous.compareHoras(current.getHora_inicio(),true)) hora_inicio = current.getHora_inicio();
                String hora_fim = current.getHora_fim();
                if (!previous.compareHoras(current.getHora_fim(),false)) hora_fim = previous.getHora_fim();
                previous.setHora_inicio(hora_inicio);
                previous.setHora_fim(hora_fim);

                aulas_para_remover.add(index);
            }
        }

        return remover_aulas(aulas,aulas_para_remover);
    }

    public Boolean string_equals(String s1,String s2) {
        return s1.equals(s2);
    }

    public List<String> get_slots_30_min(String hora_inicio, String hora_final) {
        String[] fields_hora_inicio = hora_inicio.split(":");
        String[] fields_hora_final = hora_final.split(":");
        int[] int_hora_ini = {Integer.parseInt(fields_hora_inicio[0]),Integer.parseInt(fields_hora_inicio[1]),Integer.parseInt(fields_hora_inicio[2])};
        int[] int_hora_fin = {Integer.parseInt(fields_hora_final[0]),Integer.parseInt(fields_hora_final[1]),Integer.parseInt(fields_hora_final[2])};

        int number_of_slots = 0;

        if (int_hora_fin[1] < int_hora_ini[1]) {
            number_of_slots = (int_hora_fin[0] - int_hora_ini[0])*2 - 1;
        } else {
            number_of_slots = (int_hora_fin[0] - int_hora_ini[0])*2 + (int_hora_fin[1] - int_hora_ini[1])/30;
        }

        for (int i=0; i<number_of_slots;i++) {

        }
        return new ArrayList<>();
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

