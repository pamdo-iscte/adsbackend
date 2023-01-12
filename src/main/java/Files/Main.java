package Files;

import com.google.gson.Gson;
import com.opencsv.CSVReader;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.Math;

public class Main {
	private String file_caracterizacao_das_salas = "Caracterizacao_das_Salas/ADS - Caracterizacao das salas.csv";
	private String file_horario_1sem = "Upload_de_Horarios/2- ADS - Horários 1º sem 2022-23.csv";
	private String file_avaliacoes_1sem = "Upload_de_Avaliacoes/ADS - Avaliações 1º semestre 2022-23.csv";

	private String dir_horarios_gerados = "HorariosGerados";

	private List<Slot> slots = new ArrayList<>();
	private String[] columns = null;
	private int difference = 25;


	public String ano_valor;

	public void setAno_valor(String ano_valor) {
		this.ano_valor = ano_valor;
		System.out.println(this.ano_valor);
	}

	public void setFile_caracterizacao_das_salas(String file_caracterizacao_das_salas) {
		this.file_caracterizacao_das_salas = file_caracterizacao_das_salas;
	}

	public void setFile_horario_1sem(String file_horario_1sem) {
		this.file_horario_1sem = file_horario_1sem;
	}

	public void setFile_avaliacoes_1sem(String file_avaliacoes_1sem) {
		this.file_avaliacoes_1sem = file_avaliacoes_1sem;
	}



	public String start(List<String> metodos_aulas, List<String> metodos_avaliacoes, boolean checkbox, int num, FuncoesAuxiliares funcoes_aux) {

		difference = num;

		List<Convert_metrica_JSON> result_aulas = new ArrayList<>();
		List<Convert_metrica_JSON> result_avaliacoes = new ArrayList<>();
//		int count1 = 0;
//		for (Slot s : slots) {
//			for (Evento e : s.eventos) {
//				count1++;
//			}
//		}
//		System.out.println("			Numero de eventos Inicial " + count1);

		List<String> metodosAulas = new ArrayList<String>();
		for (String as : metodos_aulas) {
			metodosAulas.add(as);
		}


		List<Sala> sala_to_return = new ArrayList<>();
		Sala sala = null;
		int i = 0;
		int j = 0;
		int file_num = 0;

		String name_aulas = "";
		String name_avaliacoes = "";

		for (int count = 0; count < metodos_aulas.size() + 1; count++) {

			slots.clear();
			initialSlots();
			readFile_slotsAula();
			readFile_slotsAvaliacao();

			file_num++;
			if( metodos_aulas.size()==1 || count >=  metodos_aulas.size()) {
				name_aulas = file_num + "_Aulas_" + metodos_aulas.toString() + ".txt";
				name_avaliacoes = file_num + "_Avaliacoes_" + metodos_aulas.toString() + ".txt";
				metodosAulas = metodos_aulas;
				count =metodos_aulas.size() + 1;
			}
			else {if (count < metodos_aulas.size()) {
				name_aulas = file_num + "_Aulas_" + metodos_aulas.get(count) + ".txt";
				name_avaliacoes = file_num + "_Avaliacoes_" + metodos_aulas.get(count) + ".txt";
				String q = metodos_aulas.get(count);
				metodosAulas.clear();
				metodosAulas.add(q);
			} }

			for (Slot s : slots) {
				System.out.println("\nslot " + i + " " + s.data + " " + s.hora_inicio + " " + s.hora_final);
				j = 0;
				for (Evento e : s.eventos) {

					System.out.println("\nnovo evento " + j);
					System.out.println(e);
					j++;
					if (e instanceof Avaliacao) {
						System.out.println("avaliacao");
						if (((Avaliacao) e).getSalas() != null) {
							if (((Avaliacao) e).getSalas().size() != 0) {
								System.out.println("\nsalas:");
								System.out.println(((Avaliacao) e).getSalas());

								continue;
							}
						}
					}
					if (e instanceof Aula) {
						System.out.println("aula");
						if (((Aula) e).getSala() != null) {
							System.out.println(((Aula) e).getSala());
							continue;
						}
					}

					// System.out.println(e);
					String aux = s.hora_final;
					if (e instanceof Avaliacao) {
						doMethodsAvals(null, (Avaliacao) e, s, metodos_avaliacoes);
					}
					if (e instanceof Aula) {
						doMethodsAulas(null, (Aula) e, s, metodos_aulas);
					}

					if (!e.getHora_fim().equals(s.hora_final)) {
						for (Slot sl : slots) {
							for (Evento ev : sl.eventos) {

								if (e instanceof Aula && checkbox) {
									if (ev.getData().equals(e.getData()) && ev.getHora_inicio().equals(e.getHora_fim())
											&& ((Aula) e).getTurno().equals(((Aula) ev).getTurno()) && ((Aula) ev).getSala() == null) {

										((Aula) ev).setSala(((Aula) e).getSala());
										sala = ((Aula) e).getSala();
										if (sala != null) {
											for (Sala asd : sl.salas_livres) {
												if (asd.getNome().equals(sala.getNome())) {
													sl.salas_livres.remove(asd);
													break;
												}
											}
										}

									}
								}


								if (e.equals(ev) && !sl.hora_inicio.equals(s.hora_inicio)) {

									aux = sl.hora_final;
									if (e instanceof Avaliacao) {

										List<Sala> listsala = ((Avaliacao) e).getSalas();
										if (listsala != null) {
											for (Sala hi : ((Avaliacao) e).getSalas()) {
												for (Sala asd : sl.salas_livres) {
													if (asd.getNome().equals(hi.getNome())) {
														sl.salas_livres.remove(asd);
														break;
													}
												}
											}

										}
									}
									if (e instanceof Aula) {
										sala = ((Aula) e).getSala();
										if (sala != null) {
											for (Sala asd : sl.salas_livres) {
												if (asd.getNome().equals(sala.getNome())) {
													sl.salas_livres.remove(asd);
													break;
												}
											}
										}
									}

								}
							}
						}
					}
					if (e instanceof Avaliacao)
						System.out.println(((Avaliacao) e).getSalas());
					if (e instanceof Aula) System.out.println(((Aula) e).getSala());

				}

				i++;
			}
			List<String> aux = new ArrayList<>();
			List<String> lista = new ArrayList<>();
			boolean st = true;
			boolean sst = true;

			for (Slot s : slots) {
				for (Evento e : s.eventos) {
					if (e instanceof Aula) {
						aux = ficheiroAtualizado(e, lista, st, name_aulas);
						lista = aux;
						st = false;
					}
					if (e instanceof Avaliacao) {
						aux = ficheiroAtualizado(e, lista, sst, name_avaliacoes);
						lista = aux;
						sst = false;
					}

				}
			}
			Metricas metricas = new Metricas(slots);
			Convert_metrica_JSON metrica_json1 = metricas.convert_metrica_aulas_JSON();
			metrica_json1.setFile_aulas(funcoes_aux.to_csv(name_aulas));
			Convert_metrica_JSON metrica_avaliacao = metricas.convert_metrica_avaliacoes_JSON();
			metrica_avaliacao.setFile_avaliacoes(funcoes_aux.to_csv(name_avaliacoes));

			String nome ="";
			if( metodos_aulas.size()==1 || count >=  metodos_aulas.size()) {
				nome = metodos_aulas.toString();
			} else if (count < metodos_aulas.size()) {
				nome = metodos_aulas.get(count);
			}
			nome = nome+" ";
			for (String s:metodos_avaliacoes) {
				nome = nome +"_"+s;
			}
			metrica_json1.setNome(nome);
			metrica_avaliacao.setNome(nome);
			result_aulas.add(metrica_json1);
			result_avaliacoes.add(metrica_avaliacao);

			//escrever_ficheiro(lista_convert_metrica);

		}
		List<List<Convert_metrica_JSON>> result = new ArrayList<>();
		result.add(result_aulas);
		result.add(result_avaliacoes);
		return new Gson().toJson(result);
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

	public List<Sala> nearest_room_for_evaluation(List<Sala> salas, int numero_alunos, List<Sala> sala_to_return) {
		if(salas.isEmpty()){
			return null;
		}
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

	public List<Sala> dividirAlunos(List<Sala> salas_livres, int dividir,Avaliacao aval) {
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

	public List<Sala> salas_match_caracteristica(String caracteristica, List<Sala> salas_livres) {
		List<Sala> salas = new ArrayList<>();

		//System.out.println("caracteristica pedida "+caracteristica );
		for (Sala s : salas_livres) {
			if(s==null) {
				return null;
			}
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

	public List<Slot> getSlots() {
		return slots;
	}

	public String[] getColumns() {
		return columns;
	}

	private List<Sala> doMethodsAvals(List<List<Sala>> sala_apos_metodo, Avaliacao a, Slot help, List<String> list_methods) {

		//System.out.println("salas livres " + help.salas_livres.size());
		if(help.salas_livres.size()==0) {
			System.out.println("n há mais salas");
		}

		//perguntar ao stor se existe algumas avaliações q n precisam de sala e cm vê-las
		if(a.getNumero_de_alunos() == 0){
			System.out.println("0 alunos inscritos");
			return null;
		}


		Random rand = new Random();
		List<List<Sala>> salas_possiveis = new ArrayList<>();
		MetodosdeAvaliacao m = new MetodosdeAvaliacao();

		if (list_methods.get(0).equals("menor_numero_de_salas")) {
			if(sala_apos_metodo != null) {
				salas_possiveis.add(sala_apos_metodo.get(0));
			}
			else {
				sala_apos_metodo = new ArrayList<>();
				sala_apos_metodo.add(help.salas_livres);
				for(List<Sala> ls: sala_apos_metodo) {
					salas_possiveis = m.menor_numero_de_salas(ls, a, help, this);
				}
			}

		}
		if (list_methods.get(0).equals("igual_numero_de_alunos_por_sala")) {
			if(sala_apos_metodo != null) {
				salas_possiveis = sala_apos_metodo;
				//	System.out.println("Não é possível realizar o método igualForma pq vem a seguir ao menorNumSalas"); //nem sei se isto vale a pena dizer honestly
			}
			else {
				sala_apos_metodo = new ArrayList<>();
				sala_apos_metodo.add(help.salas_livres);
				for(List<Sala> ls: sala_apos_metodo) {
					salas_possiveis = m.igual_numero_de_alunos_por_sala(ls, a, help,this);
				}
			}
		}
		if(salas_possiveis==null) {
			System.out.println("não há salas possíveis");
			return null;
		}
		if (salas_possiveis.size()== 0) {
			salas_possiveis = sala_apos_metodo;
			if (sala_apos_metodo==null) {
				//se houver salas disponiveis para colocar tds os alunos, o menorNumSalas dá
				salas_possiveis = m.menor_numero_de_salas(help.salas_livres, a, help,this);
				if(salas_possiveis==null) {
					System.out.println("não há salas possíveis 1");
					return null;
				}
				if (salas_possiveis.size()== 0) {
					// se não, é impossivel, diz-se q houve um erro?
					System.out.println("Não existem salas disponiveis para alocar");
					return null;
				}}
		}

		if (list_methods.size()>1) {
			doMethodsAvals(salas_possiveis, a, help, list_methods.subList(1, list_methods.size()));
			return null;
		} else {
			int index = 0;
			if(salas_possiveis.size() > 1) {
				index = rand.nextInt(salas_possiveis.size() - 1);
			}

			List<Sala> salas_escolhida = salas_possiveis.get(index);

//			for(Sala s: salas_escolhida) {
//				System.out.println("salas escolhidas " + s);
//			}
			a.setSalas(salas_escolhida) ;

			if(salas_escolhida.size() == help.salas_livres.size()) help.salas_livres.clear();

			else {
				for(Sala s: salas_escolhida) {
					help.salas_livres.remove(s);
				}}



			return salas_escolhida;
		}
	}


	private Sala doMethodsAulas(List<Sala> sala_apos_metodo, Aula a, Slot help, List<String> list_methods) {
		//System.out.println(a.caracteristica + a.inscritos + a.unidade_de_execucao);
		if (sala_apos_metodo == null) {
			sala_apos_metodo = help.salas_livres;
		}
		if(a.caracteristica.equals("Não necessita de sala")){
			System.out.println("Não necessita de sala");
			return null;
		}
		Random rand = new Random();
		List<Sala> salas_possiveis = new ArrayList<>();

		Class<MetodosParaAulas> class_metodos_aulas = MetodosParaAulas.class;
		try {
			Object t = class_metodos_aulas.getDeclaredConstructor().newInstance();
			Method[] metodos = class_metodos_aulas.getDeclaredMethods();
			for (Method m: metodos) {
				if (m.getName().equals(list_methods.get(0))) {
					Parameter[] parameters = m.getParameters();
					if (parameters.length == 4)
						salas_possiveis = (List<Sala>) m.invoke(t,sala_apos_metodo,a,this,difference);
					else {
						salas_possiveis = (List<Sala>) m.invoke(t,sala_apos_metodo,a,this);
					}
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}

		if(salas_possiveis == null) {
			salas_possiveis = resolver_conflito(sala_apos_metodo,a,help);		
		}
		
//		if (salas_possiveis.size()== 0) {
//			salas_possiveis = resolver_conflito(sala_apos_metodo,a,help);		
//		}
		
//		for(Sala s: salas_possiveis) {
//			System.out.println(s.getCapacidade_normal());
//		}
		
		if (list_methods.size()>1) {
			
			if (salas_possiveis.size()==0) {
				salas_possiveis = sala_apos_metodo;
				//System.out.println("aqui");
			}
			
			doMethodsAulas(salas_possiveis, a, help, list_methods.subList(1, list_methods.size()));
			return null;
		} else {
			if(salas_possiveis.size()==0) {
				System.out.println("não há salas possiveis");
				return null;
			}
			int index = 0;
			if(salas_possiveis.size() > 1) {
				index = rand.nextInt(salas_possiveis.size() - 1);
			}

			//System.out.println("salas possiveis:");

//			for (Sala s: salas_possiveis) {
//				System.out.println(s);
//			}

			Sala sala_escolhida = salas_possiveis.get(index);
			a.setSala(sala_escolhida);

			help.salas_livres.remove(sala_escolhida);

			//System.out.println("sala escolhida "+sala_escolhida);


			return sala_escolhida;
		}
	}


	//no caso de conseguir alocar através do método, mas der um erro qq assinalar tb isso, cores com avisos
	//dar um aviso
	 private List<Sala> resolver_conflito(List<Sala> salas_livres,Aula aula, Slot slot) {
    	System.out.println("--- Resolver conflito ---");
    	List<Sala> salas_to_return = new ArrayList<>();
    //	int difference = 10; //para ser percentagem, buscar do frontend
    	//Sala worstcase = null;
		for (Sala s: salas_livres) {
			int temp_dif = Math.abs(s.getCapacidade_normal() - aula.inscritos);// aula.turno.inscritos
//			if(s.getCapacidade_normal() < 100) {
//			//if(worstcase == null) worstcase = s;
//			else {
//				if(worstcase.getCapacidade_normal() < s.getCapacidade_normal()) worstcase = s;
//			}}
			if ((temp_dif <= ((difference/100)*aula.inscritos)) && (s.getCaracteristicas().size()!=1)) {
				//difference = temp_dif;
				salas_to_return.add(s);				
			}
		}
//		if(salas_to_return.size()==0) {
//			System.out.println("Não existem salas com a capacidade pedida, em pior caso:");
//			salas_to_return.add(worstcase);
//		}
		return salas_to_return;
    }


	public void aulas_seguidas(Aula aula_anterior, List<Slot> slots, Sala sala_escolhida){
		for(Slot slotes: slots) {
			if(aula_anterior.getData().equals(slotes.getData()) && aula_anterior.hora_final.equals(slotes.hora_inicio)) {
				List<Evento> eventos = slotes.eventos;
				for(Evento e: eventos) {
					if(e instanceof Aula) {
						Aula aula_seguinte = (Aula) e;
						if(aula_seguinte.getTurno().equals(aula_anterior.getTurno())){
							if(aula_anterior.getCaracteristica().equals(aula_seguinte.getCaracteristica())) {
								aula_seguinte.setSala(sala_escolhida); //Sala escolhida está na salas livres pq nenhuma sala foi atribuida para o slot em questão!
								slotes.salas_livres.remove(sala_escolhida);
							}
						}
					}
				}
			}
		}
	}

	//Tem de existir um if se já estiver alocada uma sala a um evento, não é preciso meter outra sala
	//Os slots tem de ser vistos por ordem cronolofica






	//métodos para avaliações

	//menor numero de salas






	// menor distancia


	//pode ou não mudar de sala - obrigatorio, hard not soft metric



	//  distribuir de igual forma os alunos pelas salas










	private void initialSlots() {
    	int hora_inicio = 7;
    	int hora_final = 0;
    	String minuto = "30";
    	String segundo = "00";
    	
//    	DateFormat dateFormat = new SimpleDateFormat("dd-mm-yy");
//    	String data_inicio = dateFormat.format(ano_valor);
//    	String[] aux = data_inicio.split("-"); //o q é q recebo
//    	String[] aux2 = ano_valor.split("");
//    	String data_to_give = aux[1]+"-"+aux[0]+"-"+aux2[2]+aux2[3];
    	
    	String data_inicio = "01-09-"+ano_valor;
    	
    	String data_final = "31-08-"+Integer.toString(Integer.parseInt(ano_valor)+1);
    	boolean i = false;
		List<Evento> eventos = new ArrayList<>();
		List<Sala> salas = readFile_caracterizacaoDasSalas();

		String string_hora_inicio = "";
		String string_hora_final = "";



		while(!data_inicio.equals(data_final)) {

			while(hora_inicio < 24) { //ou hora_final n sei
				if(Integer.toString(hora_inicio).length()==1) {
					string_hora_inicio = "0"+Integer.toString(hora_inicio) + ":" + minuto + ":" + segundo;
				}
				else string_hora_inicio = Integer.toString(hora_inicio) + ":" + minuto + ":" + segundo;
				//hora_inicio++; assim é de 1h30 em 1h30, mas n sei se faz mt sentido
				if(i==false) {
					hora_inicio++; //assim é de 30 em 30 mins
					minuto = "00";
					i = true;
				}
				else {
					minuto = "30";
					i = false;
				}
				if(Integer.toString(hora_inicio).length()==1) {
					string_hora_final = "0"+Integer.toString(hora_inicio) + ":" + minuto + ":" + segundo;
				}
				else string_hora_final = Integer.toString(hora_inicio) + ":" + minuto + ":" + segundo;
				Slot new_slot = new Slot(data_inicio, string_hora_inicio, string_hora_final, null, null);
				slots.add(new_slot);

			}
			hora_inicio = 7;

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(sdf.parse(data_inicio));
			} catch (ParseException e) {
			}
			c.add(Calendar.DATE, 1);
			data_inicio = sdf.format(c.getTime());
		}
	}
	private void fillSlot(Evento evento, String data, String hora_inicio, String hora_final) {

		for (Slot s : slots) {
			if(s.eventos == null) {
				s.eventos =new ArrayList<>();
			}
			if(s.salas_livres == null) {
				s.salas_livres = readFile_caracterizacaoDasSalas();
			}
			if (s.data.equals(data) && s.hora_inicio.compareTo(hora_inicio)>=0 && s.hora_inicio.compareTo(hora_final) < 0) {
				s.eventos.add(evento);
			}
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
							date = new SimpleDateFormat("dd-MM-yyyy").parse(line[10]);
						}

						evento = new Aula(date, date, Integer.parseInt(line[4]), cursos, unidade_de_execucao,
								hora_inicial, hora_final, line);
						fillSlot(evento, line[10], hora_inicial, hora_final);
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

						String[] aux = data[0].split("/");
						String[] ano = aux[0].split("");
						String data_to_give = aux[2]+"-"+aux[1]+"-"+ano[2]+ano[3];

						fillSlot(evento, data_to_give, hora_inicial, hora_final);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> ficheiroAtualizado(Evento e, List<String> list, boolean st, String namefile) {
		String delimeter = ";";
		String linha = "";
		if(e instanceof Aula) {

			try {
				File file = new File(namefile);
				BufferedWriter myWriter = new BufferedWriter(new FileWriter(namefile, true));
				if(st) {
					PrintWriter writer = new PrintWriter(namefile);
					linha = "Curso"+delimeter+"Unidade de execução"+delimeter+"Turno"+delimeter+"Turma"+delimeter+"Inscritos no turno"+delimeter+""
							+ "Turnos com capacidade superior à capacidade das características das salas"+delimeter+"Turno com inscrições superiores à capacidade das salas"+delimeter+"Dia da Semana"+delimeter+""
							+ "Início"+delimeter+"Fim"+delimeter+"Dia"+delimeter+"Características da sala pedida para a aula"+delimeter+"Sala da aula"+delimeter+"Lotação"+delimeter+"Características reais da sala";
					writer.print(linha);
					System.out.println("created");
					writer.close();
				}

				String sala = "";
				String capacidade = "";
				String caracteristica = "Não necessita de sala";
				if(!((Aula)e).caracteristica.equals("Não necessita de sala") && ((Aula)e).sala!=null) {
					sala=((Aula)e).sala.getNome();
					capacidade = String.valueOf(((Aula)e).sala.getCapacidade_normal());
					caracteristica = ((Aula)e).sala.getCaracteristicas().toString();
				}


				linha = ((Aula)e).curso + delimeter +((Aula)e).unidade_de_execucao + delimeter +((Aula)e).turno + delimeter +((Aula)e).turma + delimeter +((Aula)e).inscritos + delimeter +
						((Aula)e).turnoCapacidadeSuperior + delimeter +((Aula)e).turnoInscricoesSuperior + delimeter +((Aula)e).dia_semana + delimeter +e.getHora_inicio() + delimeter +e.getHora_fim() + delimeter +
						((Aula)e).dia + delimeter +((Aula)e).caracteristica + delimeter +sala + delimeter + capacidade + delimeter +caracteristica;




				boolean existe = false;

				for(String s: list) {
					if(s.equals(linha))
						existe = true;
				}

				if(!existe) {
					myWriter.append("\n");
					myWriter.append(linha);
					list.add(linha);
				}

				myWriter.close();

			} catch (IOException et) {
				System.out.println("An error occurred.");
				et.printStackTrace();
			}

		}



		if(e instanceof Avaliacao) {

			try {
				File file = new File(namefile);
				BufferedWriter myWriter = new BufferedWriter(new FileWriter(namefile, true));
				if(st) {
					PrintWriter writer = new PrintWriter(namefile);
					linha = "Código"	+ delimeter + "Unidade Curricular"	+ delimeter + "Cursos"	+ delimeter + "Tipo"	+ delimeter
							+ "Época"+ delimeter + 	"Nome"+ delimeter + 	"Requer inscrição prévia"+ delimeter + "	Período de Inscrição"	+ delimeter
							+ "Data"	+ delimeter + "Salas"+ delimeter + "	Estado de pedido de sala	"+ delimeter + "Capacidade Salas"+ delimeter + 	"Nº alunos previsto	"+ delimeter
							+ "Lugares" ;

					writer.print(linha);
					System.out.println("created");
					writer.close();

				}

				String salas = "";
				String estado = "";
				int capacidade_aux =0;
				int lugares_aux = 0;
				if(((Avaliacao)e).salas!=null) {
					for(Sala s: ((Avaliacao)e).salas) {
						salas+= s.getNome();
						capacidade_aux += s.getCapacidade_exame();

					}
					lugares_aux = capacidade_aux - ((Avaliacao)e).getNumero_de_alunos();//n sei bem o q por aqui
				}
				String capacidade = String.valueOf(capacidade_aux);
				String lugares = String.valueOf(lugares_aux);

				linha = ((Avaliacao)e).codigo + delimeter +((Avaliacao)e).unidade + delimeter +((Avaliacao)e).curso + delimeter +((Avaliacao)e).tipo + delimeter +((Avaliacao)e).epoca + delimeter +
						((Avaliacao)e).nome + delimeter +((Avaliacao)e).requer_inscricao_previa + delimeter +((Avaliacao)e).periodo_inscricao + delimeter +((Avaliacao)e).data_hora + delimeter +salas + delimeter +
						estado + delimeter +capacidade + delimeter +String.valueOf(((Avaliacao)e).getNumero_de_alunos()) + delimeter + lugares;


				boolean existe = false;

				for(String s: list) {
					if(s.equals(linha))
						existe = true;
				}

				if(!existe) {
					myWriter.append("\n");
					myWriter.append(linha);
					list.add(linha);
				}

				myWriter.close();

			} catch (IOException et) {
				System.out.println("An error occurred.");
				et.printStackTrace();
			}
		}
		return list;

	}


}
