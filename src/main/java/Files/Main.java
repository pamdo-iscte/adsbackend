package Files;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.lang.Math;
import java.util.Random;

public class Main {
	private final String file_caracterizacao_das_salas = "ADS - Caracterizacao das salas.csv";
	private final String file_horario_1sem = "2- ADS - Horários 1º sem 2022-23.csv";
	private final String file_avaliacoes_1sem = "ADS - Avaliações 1º semestre 2022-23.csv";

	private List<Slot> slots = new ArrayList<>();
	private String[] columns = null;

	private List<List<Sala>> salas_para_aulas = new ArrayList<>();
	private List<Aula> aulas_faltam = new ArrayList<>();



	//private List<Slot help


//	public static void main(String[] args) {
//        Main main = new Main();
//        main.start();
//        //main.test();
//    }

	private void test() {
		List<Sala> sala_to_return = new ArrayList<>();
		//sala_to_return = nearest_room(salas,130,sala_to_return);
		//System.out.println("SIZE: "+sala_to_return.size());
		//sala_to_return = salas_match_caracteristica("Laboratório_de_Informática",salas);
		//for (Sala sala: sala_to_return) {
		//  System.out.println(sala.toString());
		//}

	}

	public void start(List<String> metodos_aulas, List<String> metodos_avaliacoes) {

		//readFile_slotsAula();
		readFile_slotsAvaliacao();
    	
    	/*List<String> metodos = new ArrayList<String>();
    	metodos.add("caracteristica");
    	metodos.add("evitar sobrelotaçao");
*/
		Slot s = slots.get(1);
		Evento e = s.eventos.get(7); // slot 1, evento 7 é o q tem bues pessoas
		List<Sala> sala_to_return = new ArrayList<>();
		//for (Slot s : slots) {
		//for (Evento e : s.eventos) {
		System.out.println("\n novo evento");
		menorNumSalas(s.salas_livres, (Avaliacao) e, s);
		//nearest_room_for_evaluation(s.salas_livres, e.getNumero_de_alunos(), sala_to_return);
		//doMethods(s.salas_livres, (Aula) e, s, metodos);
		//}
		//}

	}

	private List<Sala> readFile_caracterizacaoDasSalas() {
		List<Sala> salas = new ArrayList<>();
		try {

			FileReader filereader = new FileReader(file_caracterizacao_das_salas);

			CSVReader csvReader = new CSVReader(filereader);
			String[] nextRecord;

			//String[] columns = null;
			boolean first_line = true;

			// we are going to read data line by line
			while ((nextRecord = csvReader.readNext()) != null) {
				if (first_line) {

					columns = nextRecord[0].split(";");
					first_line = false;
				} else {

					String[] line = nextRecord[0].split(";");
					List<String> caracteristicas = new ArrayList<>();


					int num_caracteristicas = Integer.parseInt(line[4]);
					for (int i = 5; i < line.length; i++) {
						if (line[i].equalsIgnoreCase("x") && num_caracteristicas > 0) {

							caracteristicas.add(columns[i]);

							num_caracteristicas--;
						}
					}
					Sala s = new Sala(line[0], line[1], Integer.parseInt(line[2]), Integer.parseInt(line[3]), caracteristicas);
					salas.add(s);
				}
			}
           /*for (Sala s : salas) {
                System.out.println(s.toString());
            }*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salas;
	}

	private void adicionar_espacos_a_eventos() {
		List<Slot> slots = new ArrayList<>();

		for (Slot slot : slots) {
			List<Evento> eventos_slot = slot.eventos;
			for (Evento e : eventos_slot) {
				if (e instanceof Avaliacao) check_salas_para_avaliacao((Avaliacao) e, slot);
				else if (e instanceof Aula) check_sala_para_aula((Aula) e,slot);
			}
		}
	}

	private List<Sala> nearest_room_for_evaluation(List<Sala> salas, int numero_alunos, List<Sala> sala_to_return) {

		int distance = salas.get(0).getCapacidade_exame() - numero_alunos;
		int index=0;

		for (int i=1; i < salas.size(); i++) {
			int temp_distance = salas.get(i).getCapacidade_exame() - numero_alunos;
			if ((temp_distance>=0 && temp_distance < distance) ||
					distance < 0 && temp_distance > distance) {
				index=i;
				distance=temp_distance;
			}
		}
		Sala sala_index = salas.get(index);
		//System.out.println(sala_index);
		if (sala_index.getCapacidade_exame() >= numero_alunos) sala_to_return.add(sala_index);
		else {
			int new_numero_alunos = numero_alunos - sala_index.getCapacidade_exame();
			salas.remove(index);
			sala_to_return.add(sala_index);
			sala_to_return = nearest_room_for_evaluation(salas,new_numero_alunos,sala_to_return);
		}



		return sala_to_return;
	}

	private void check_salas_para_avaliacao(Avaliacao a, Slot slot) {
		List<Sala> salas_livres = slot.salas_livres;
		String estado_pedido = a.getEstado_pedido_sala();
		if (estado_pedido.equals("Aberto") || estado_pedido.equals("Novo")) {
			List<Sala> sala_to_return = new ArrayList<>();
			sala_to_return = nearest_room_for_evaluation(salas_livres,a.getNumero_de_alunos(),sala_to_return);
			if (!sala_to_return.isEmpty()) {
				a.setSalas(sala_to_return);
				int capacidade_salas=0;
				for (Sala s:sala_to_return) {
					capacidade_salas+=s.getCapacidade_exame();
				}
				a.setCapacidade_salas(capacidade_salas);
			} else {
				System.err.println("Não encontrou nenhuma aula para a avaliação");
			}
		}
	}

	private List<Sala> salas_match_caracteristica(String caracteristica, List<Sala> salas_livres) {
		List<Sala> salas = new ArrayList<>();
		//System.out.println("caracteristica pedida "+caracteristica );
		for (Sala s : salas_livres) {
			//System.out.println("da salas"+s.getCaracteristicas());
			String[] aux = caracteristica.split(" ");
			for(int i = 0; i<aux.length; i++) {
				if (s.getCaracteristicas().contains(aux[i])) {
					//System.out.println("contem caracteristica");
					salas.add(s);
					break;
				}}
		}

		return salas;
	}

	private Sala check_sala_para_aula(Aula aula, Slot slot) {
		List<Sala> salas_livres = slot.salas_livres;
		String caracteristica_aula = aula.caracteristica;
		List<Sala> salas_livres_com_caracteristica = salas_match_caracteristica(caracteristica_aula, salas_livres);

		if (!salas_livres_com_caracteristica.isEmpty()) {
			int diferenca = 0; //diferenca entre capacidade da sala e numero de inscritos
			Sala sala_to_return = null;

			for (Sala s : salas_livres_com_caracteristica) {
				int temp_dif = s.getCapacidade_normal() - aula.inscritos;
				if (temp_dif >= 0 && temp_dif < diferenca) {
					diferenca = temp_dif;
					sala_to_return = s;
				}
			}
			if (sala_to_return != null) return sala_to_return;
			else {
				//Aqui há salas com aquela caracteristica disponiveis mas com capacidade inferior
				System.out.println("Sobrelotação");
				//resolver_conflito();
			}
		}
		//Aqui nao ha salas com aquela caracteristica
		//resolver_conflito();
		return null;
	}






	private Sala doMethods(List<Sala> sala_apos_metodo, Aula a, Slot help, List<String> list_methods) {
		System.out.println(a.caracteristica + a.inscritos + a.unidade_de_execucao);
		if (sala_apos_metodo == null) {
			sala_apos_metodo = help.salas_livres;
		}
		if(a.caracteristica.equals("Não necessita de sala")){
			System.out.println("Não necessita de sala");
			return null;
		}
		Random rand = new Random();
		List<Sala> salas_possiveis = new ArrayList<>();

		// ver se há alguma maneira mais eficiente de ir buscar o nome dos metodos

		Class<MetodosParaAulas> class_metodos_aulas = MetodosParaAulas.class;
		try {
			Object t = class_metodos_aulas.getDeclaredConstructor().newInstance();
			Method[] metodos = class_metodos_aulas.getDeclaredMethods();
			for (Method m: metodos) {
				if (m.getName().equals(list_methods.get(0))) {
					Parameter[] parameters = m.getParameters();
					if (parameters.length == 3)
						m.invoke(t,sala_apos_metodo,a,help);
					System.out.println(m.getName().equals(list_methods.get(0)));
					m.invoke(t,1);
				}
			}

		}catch (Exception e){}


		if (list_methods.get(0).equals("caracteristica")) { // aqui o nome dps logo se vê

			salas_possiveis = keepCaracteristic(sala_apos_metodo, a, help);
		}

		if (list_methods.get(0).equals("evitar sobrelotaçao")) { // aqui o nome dps logo se vê

			salas_possiveis = evitarSobrelotacao(sala_apos_metodo, a, help);
		}
		if (list_methods.get(0).equals("menor distancia")) { // aqui o nome dps logo se vê
			salas_possiveis = sala_menor_distancia(a, help);
		}

		assert salas_possiveis != null;
		if (salas_possiveis.size()== 0) {
			salas_possiveis = sala_apos_metodo;
			if (sala_apos_metodo.size()==0) {
				salas_possiveis = resolver_conflito(a,help);
			}
		}

		if (list_methods.size()>1) {
			/*System.out.println("salas possiveis:");

			for (Sala s : salas_possiveis) {
				System.out.println(s);
			}*/
			doMethods(salas_possiveis, a, help, list_methods.subList(1, list_methods.size()));
			return null;
		} else {

			int index = 0;
			if(salas_possiveis.size() > 1) {
				index = rand.nextInt(salas_possiveis.size() - 1);
				aulas_faltam.add(a);
				salas_para_aulas.add(salas_possiveis);
				//tentar ter tds as listas de salas possiveis para atribuir primeiro aquelas q só têm uma opção??
			}

			//System.out.println("salas possiveis:");

			//for (Sala s: salas_possiveis) {
			//System.out.println(s);
			//}

			Sala sala_escolhida = salas_possiveis.get(index);
			help.salas_livres.remove(sala_escolhida);

			System.out.println("sala escolhida "+sala_escolhida);
			return sala_escolhida;
		}
	}



	private List<Sala> evitarSobrelotacao(List<Sala> salas_livres, Aula aula, Slot slot) {
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



	private List<Sala> keepCaracteristic(List<Sala> salas_livres, Aula aula, Slot slot) {
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

		List<Sala> salas_livres_com_caracteristica = salas_match_caracteristica(caracteristica_aula, salas_livres);
		System.out.println("\n");
		System.out.println("Aula "+caracteristica_aula+" " + aula.inscritos);
		if(caracteristica_aula.equals("Não_necessita_de_sala")) {
			System.out.println("Não necessita de sala");
			return null;
		}
		List<Sala> salas_to_return = new ArrayList<>();
		salas_to_return = salas_livres_com_caracteristica;

		return salas_to_return;
	}


	//no caso de conseguir alocar através do método, mas der um erro qq assinalar tb isso, cores com avisos
	//dar um aviso
	private List<Sala> resolver_conflito(Aula aula, Slot slot) {
		System.out.println("--- Resolver conflito ---");
		List<Sala> salas_to_return = new ArrayList<>();
		int diferenca = 20;
		for (Sala s: slot.salas_livres) {
			int temp_dif = Math.abs(s.getCapacidade_normal() - aula.inscritos);// aula.turno.inscritos
			if (temp_dif < diferenca && s.getCaracteristicas().size()!=1) { // numero menor q o da sala anterior
				diferenca = temp_dif;
				salas_to_return.add(s);
			}
		}
		//System.out.println(aula.inscritos);
		for(Sala sa: salas_to_return) {
			System.out.println(sa.getNome() + " com " + sa.getCapacidade_normal());
		}
		return salas_to_return;
	}














	public List<Sala> sala_menor_distancia(Aula aula, Slot slot){
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





	//métodos para avaliações

	//menor numero de salas

	private List<List<Sala>> menorNumSalas(List<Sala> salas_livres, Avaliacao aval, Slot slot) {
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
			conjunto_salas = nearest_room_for_evaluation(aux2, aval.getNumero_de_alunos(),aux);
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





	// menor distancia


	//pode ou não mudar de sala - obrigatorio, hard not soft metric



	//  distribuir de igual forma os alunos pelas salas

	private List<List<Sala>> igualForma(List<Sala> salas_livres, Avaliacao aval, Slot slot) { //dar int div cm parametro?
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
		List<Sala> aux = new ArrayList<>();
		for(Sala s: salas_livres) {
			aux.add(s);
		}

		while(salas_to_return.size()<5)
		{
			conjunto_salas = dividirAlunos(aux, dividir, aval);
			if(conjunto_salas.size() == 0) {
				dividir++;
				aux.clear();
				for(Sala s: salas_livres) {
					aux.add(s);
				}
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





	private List<Sala> dividirAlunos(List<Sala> salas_livres, int dividir, Avaliacao aval) {
		int num = aval.getNumero_de_alunos()/dividir;
		List<Sala> conjunto_salas = new ArrayList<>();

		for (Sala s : salas_livres) {
			if (s.getCapacidade_exame() > 0) {
				if(conjunto_salas.size() < dividir) {
					if (s.getCapacidade_exame() >= num) {
						conjunto_salas.add(s);
					}}
			}
		}
		if(conjunto_salas.size() < dividir) {
			conjunto_salas.clear();
		}
		return conjunto_salas;
	}








	private void fillSlot(Evento evento, String data, String hora_inicio, String hora_final) {
		List<Evento> eventos = new ArrayList<>();
		eventos.add(evento);
		List<Sala> salas = readFile_caracterizacaoDasSalas();
		Slot new_slot = new Slot(data, hora_inicio, hora_final, eventos, salas);
		boolean duplicate = false;

		for (Slot s : slots) {
			if (s.data.equals(new_slot.data) && s.hora_inicio.equals(new_slot.hora_inicio)
					&& s.hora_final.equals(new_slot.hora_final)) {
				s.eventos.add(evento);
				duplicate = true;
			}
		}

		if (!duplicate) {
			slots.add(new_slot);
		}
	}



	private void readFile_slotsAula() {

		try {

			FileReader filereader = new FileReader(file_horario_1sem);

			try (CSVReader csvReader = new CSVReader(filereader)) {
				String[] nextRecord;


				boolean first_line = true;
				while ((nextRecord = csvReader.readNext()) != null) {
					if (first_line) {
						first_line = false;

					} else {
						String[] line = nextRecord;
						if(line[0].isEmpty()) {
							break;
						}

						String unidade_de_execucao = line[1];
						String[] cursos = line[0].split(",");
						Evento evento = null;


						String hora_inicial = line[8];
						String hora_final = line[9];

						Date date = null;
						if(!line[10].isEmpty()) {
							date = new SimpleDateFormat("dd-MM-yyyy").parse(line[10]);}

						evento = new Aula(date, date, Integer.parseInt(line[4]), cursos, unidade_de_execucao,
								hora_inicial, hora_final, line);

						fillSlot(evento, line[10], line[8], line[9]);
					}
				}
			}
			//for (Slot s: slots) {
			//System.out.println(s.toString());
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}






	private void readFile_slotsAvaliacao() {

		try {

			FileReader filereader = new FileReader(file_avaliacoes_1sem);

			try (CSVReader csvReader = new CSVReader(filereader)) {
				String[] nextRecord;


				boolean first_line = true;
				while ((nextRecord = csvReader.readNext()) != null) {
					if (first_line) {
						first_line = false;

					} else {
						String[] line = nextRecord;
						if(line[0].isEmpty()) {
							break;
						}

						String unidade_de_execucao = line[1];
						String[] cursos = line[0].split(",");
						Evento evento = null;

						String[] data = line[8].split(" ");

						String hora_inicial = data[1];
						String hora_final = null;
						Date data_inicial = null;
						Date data_final = null;
						data_inicial = new SimpleDateFormat("yyyy/MM/dd").parse(data[0]);



						if(data.length==5) {
							hora_final = data[4];
							data_final = new SimpleDateFormat("yyyy/MM/dd").parse(data[3]);
						}
						else {
							hora_final = data[3];
							data_final = data_inicial;
						}


						evento = new Avaliacao(data_inicial, data_final, Integer.parseInt(line[12]), cursos, unidade_de_execucao,
								hora_inicial, hora_final, line);



						fillSlot(evento, data[0], hora_inicial, hora_final);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
