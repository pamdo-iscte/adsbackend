package Files;

import java.util.ArrayList;
import java.util.List;

public class MetodosParaAulas {
	
	//precisamos de ir buscar o difference somehow
	private List<Sala> evitar_sobrelotacao(List<Sala> salas_livres, Aula aula, Main main, int difference) {
    	List<Sala> salas_to_return = new ArrayList<>();
    	
    	//difference = 10; //para ser percentagem, ir buscar ao frontend em principio
    	
    	//isto
    	if(aula.getNumero_de_alunos()==0) {
    		System.out.println("numero de alunos é 0");
    		return null;
    	}
    	
		for (Sala s: salas_livres) {
			int temp_dif = s.getCapacidade_normal() - aula.inscritos;// aula.turno.inscritos
			if ((s.getCaracteristicas().size()!=1) && (temp_dif >= 0) && (temp_dif < ((difference/100)*aula.inscritos))))//((difference/100)*aula.inscritos))) 
			{
				salas_to_return.add(s);		
			}
		}
		/*for(Sala sa: salas_to_return) {
			System.out.println(sa.getNome() + " com " + sa.getCapacidade_normal());
		}*/
		
		return salas_to_return;
    }

    public List<Sala> manter_caracteristica(List<Sala> salas_livres, Aula aula, Main main) {
//        System.out.println("Aulas manter caracteristica");
        String[] columns = main.getColumns();
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

        List<Sala> salas_livres_com_caracteristica = main.salas_match_caracteristica(caracteristica_aula, salas_livres);
//        System.out.println("\n");
//        System.out.println("Aula "+caracteristica_aula+" " + aula.inscritos);
        if(caracteristica_aula.equals("Não_necessita_de_sala")) {
//            System.out.println("Não necessita de sala");
            return null;
        }
        List<Sala> salas_to_return = new ArrayList<>();
        salas_to_return = salas_livres_com_caracteristica;

        return salas_to_return;
    }


}
