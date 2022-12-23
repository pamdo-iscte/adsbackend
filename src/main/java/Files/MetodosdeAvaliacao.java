package Files;

import java.util.ArrayList;
import java.util.List;

public class MetodosdeAvaliacao {

    public List<List<Sala>> menor_numero_de_salas(List<Sala> salas_livres, Avaliacao aval, Slot slot, Main main) {
        List<List<Sala>> salas_to_return = new ArrayList<>();
        if (aval.getNumero_de_alunos() == 0) {
            System.out.println("não há alunos");
            return null;
        }

        //dar um aviso qq
        if(!aval.getData().equals(aval.getData_final())) {
            System.out.println("Tem datas diferentes");
            return null;
        }

        List<Sala> aux = new ArrayList<>();

        List<Sala> aux2 = new ArrayList<>(salas_livres);

        List<Sala> conjunto_salas = new ArrayList<>();

        int i = 1;
        int[] indexes = new int[6];
        indexes[0] = 0;

        while (i < 6) {
            conjunto_salas = main.nearest_room_for_evaluation(aux2, aval.getNumero_de_alunos(),aux);
            for(Sala s: conjunto_salas) {
                aux2.remove(s);
            }
            indexes[i] = conjunto_salas.size();
            i++;
        }

        i = 0;
        while (i < 5) {
            salas_to_return.add(conjunto_salas.subList(indexes[i], indexes[i+1]));
            i++;
        }

        for (List<Sala> sa : salas_to_return) {
            System.out.println(".");
            System.out.println(aval);
            for (Sala s : sa) {
                System.out.println(s);
            }
        }
        return salas_to_return;
    }


    public List<List<Sala>> igual_numero_de_alunos_por_sala(List<Sala> salas_livres, Avaliacao aval, Slot slot, Main main) { //dar int div cm parametro?
        List<List<Sala>> salas_to_return = new ArrayList<>();
        List<Sala> conjunto_salas = new ArrayList<>();
        System.out.println(aval);
        if (aval.getNumero_de_alunos() == 0) {
            System.out.println("não há alunos");
            return null;
        }

        if(!aval.getData().equals(aval.getData_final())) {
            System.out.println("Tem datas diferentes");
            return null;
        }

        int dividir = 1;
        List<Sala> aux = new ArrayList<>(salas_livres);

        while(salas_to_return.size()<5)
        {
            conjunto_salas = main.dividirAlunos(aux, dividir, aval);
            if(conjunto_salas.size() == 0) {
                dividir++;
                aux.clear();
                aux.addAll(salas_livres);
                System.out.println("Impossivel de fazer essa divisão, dividir alunos agora em " + dividir);

            }else {
                salas_to_return.add(conjunto_salas);
                for(Sala s: conjunto_salas) {
                    aux.remove(s);
                }
            }
        }
        for(List<Sala> l: salas_to_return) {
            System.out.println(".");
            for (Sala d: l) {
                System.out.println(d);
            }
        }

        return salas_to_return;
    }









}
