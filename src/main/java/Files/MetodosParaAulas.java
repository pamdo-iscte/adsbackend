package Files;

import java.util.ArrayList;
import java.util.List;

public class MetodosParaAulas {

    private List<Slot> slots = new ArrayList<>();
    private String[] columns = null;



    private List<Sala> evitar_sobrelotacao(List<Sala> salas_livres, Aula aula, Slot slot) {
        List<Sala> salas_to_return = new ArrayList<>();
        int difference = 30;
        for (Sala s: salas_livres) {
            int temp_dif = s.getCapacidade_normal() - aula.inscritos;// aula.turno.inscritos
            if (s.getCaracteristicas().size()!=1 && temp_dif >= 0 && temp_dif < difference) {
                salas_to_return.add(s);
            }
        }
		/*for(Sala sa: salas_to_return) {
			System.out.println(sa.getNome() + " com " + sa.getCapacidade_normal());
		}*/
        return salas_to_return;
    }

    private List<Sala> manter_caracteristica(List<Sala> salas_livres, Aula aula, Slot slot) {
        //List<Sala> salas_livres = slot.salas_livres;
        String caracteristica_aula1 = aula.caracteristica;
        String caracteristica_aula = caracteristica_aula1.replace(" ", "_");


        if (caracteristica_aula.equals("Sala/anfiteatro_aulas")) {
            caracteristica_aula = "";
            for (int i = 0; i < columns.length; i++) {
                if ((columns[i].contains("Sala") && !columns[i].equals("Sala_de_Arquitectura"))
                        || columns[i].equals("Anfiteatro_aulas")) {
                    caracteristica_aula += " " + columns[i];
                }
            }
        }

        if (caracteristica_aula.equals("Lab_ISTA")) {

            caracteristica_aula = "";
            for (int i = 0; i < columns.length; i++) {
                if ((columns[i].contains("Laboratório") && !columns[i].equals("Laboratório_de_Jornalismo"))) {
                    caracteristica_aula += " " + columns[i];
                }
            }
        }

//        List<Sala> salas_livres_com_caracteristica = salas_match_caracteristica(caracteristica_aula, salas_livres);
//        System.out.println("\n");
//        System.out.println("Aula "+caracteristica_aula+" " + aula.inscritos);
//        if(caracteristica_aula.equals("Não_necessita_de_sala")) {
//            System.out.println("Não necessita de sala");
//            return null;
//        }
        List<Sala> salas_to_return = new ArrayList<>();
//        salas_to_return = salas_livres_com_caracteristica;

        return salas_to_return;
    }

    public List<Sala> menor_distancia_entre_salas(Aula aula, Slot slot){
        List<Sala> salas_return = new ArrayList<>();
        for(Slot slotes: slots) {
            if(slot.data.equals(slotes.data) && slot.hora_inicio.equals(slotes.hora_final)) {
                //System.out.println();
                List<Evento> eventos = slotes.eventos;
                for(Evento e: eventos) {
                    //System.out.println("antes do if");
                    if(e instanceof Aula) {
                        //System.out.println("dentro do if");
                        Aula aula_anterior = (Aula) e;
                        if(aula.getTurno().equals(aula_anterior.getTurno())){
                            Sala sala_anterior = aula_anterior.sala;
                            if(slot.salas_livres.contains(sala_anterior)){
                                if(aula_anterior.caracteristica.equals(aula.caracteristica)) {
                                    salas_return.add(sala_anterior);
                                    System.out.println("Sala "+salas_return.get(0));
                                    return salas_return;
                                }
                            }
                            else {
                                for(Sala sala: slot.salas_livres) {
                                    if(sala.getCaracteristicas().equals(aula.getCaracteristica())) {
                                        if(sala.getEdificio().equals(aula.getSala().getEdificio())) {
                                            salas_return.add(sala);
                                            System.out.println("caso especial");

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(salas_return.size() < 1) {
            //System.out.println("Sala "+salas_return.get(0));
            //System.out.println("n há salas");
            return slot.salas_livres;
        }
        System.out.println("Sala "+salas_return.get(0));
        return salas_return;
    }


}
