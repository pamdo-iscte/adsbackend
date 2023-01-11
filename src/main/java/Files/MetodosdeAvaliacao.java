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
		List<Sala> aux2 = new ArrayList<>();

		for(Sala s: salas_livres) {
			aux2.add(s);
		}


		List<Sala> conjunto_salas = new ArrayList<>();

		int i = 1;
		int[] indexes = new int[6];
		indexes[0] = 0;

		while (i < 6) {
			conjunto_salas = main.nearest_room_for_evaluation(aux2, aval.getNumero_de_alunos(),aux);
			if(conjunto_salas == null) {
				break;
			}
			for(Sala s: conjunto_salas) {
				aux2.remove(s);
			}
			indexes[i] = conjunto_salas.size();
			i++;
		}

		i = 0;
		while (i < 5) {
			if(conjunto_salas!= null) {
				salas_to_return.add(conjunto_salas.subList(indexes[i], indexes[i+1]));
			}
			else break;
			i++;
		}

//		for (List<Sala> sa : salas_to_return) {
//			System.out.println(".");
//			System.out.println(aval);
//			for (Sala s : sa) {
//				System.out.println(s);
//			}
//		}
		return salas_to_return;
	}


	public List<List<Sala>> igual_numero_de_alunos_por_sala(List<Sala> salas_livres, Avaliacao aval, Slot slot, Main main) { //dar int div cm parametro?
		List<List<Sala>> salas_to_return = new ArrayList<>();
		List<Sala> conjunto_salas = new ArrayList<>();
		if (aval.getNumero_de_alunos() == 0) {
			System.out.println("não há alunos");
			return null;
		}

		if(!aval.getData().equals(aval.getData_final())) {
			System.out.println("Tem datas diferentes");
			return null;
		}

		int dividir = 1;
		List<Sala> aux = new ArrayList<>();
		for(Sala s: salas_livres) {
			aux.add(s);
		}

		while(salas_to_return.size()<5)
		{
			conjunto_salas = main.dividirAlunos(aux, dividir, aval);
			if(dividir == 30) { // máximo número de salas que queremos que a avaliação seja dividida
				if(salas_to_return.size() == 0) {
					System.out.println("n há maneira de dividir em igual forma em menos de " +dividir+"salas");
					return null;
				}

				break;
			}
			if(conjunto_salas.size() == 0) {
				dividir++;
				aux.clear();
				for(Sala s: salas_livres) {
					aux.add(s);
				}
				//System.out.println("Impossivel de fazer essa divisão, dividir alunos agora em " + dividir);

			}else {
				salas_to_return.add(conjunto_salas);
				for(Sala s: conjunto_salas) {
					aux.remove(s);
				}
			}
		}
		return salas_to_return;
	}









}
