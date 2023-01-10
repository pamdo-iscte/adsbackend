package Files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;



public class Metricas {

	List<Integer> salas_livres = new ArrayList<>();
	
//é preciso meter as salas das aulas a null se nao for feito de forma automatica!
	public Metricas(List<Slot> slots) {
	        for (Slot slot : slots) {
	            List<Evento> eventos_slot = slot.eventos;
	            salas_livres.add(slot.getSalas_livres().size());
	            aulas_livres_slot(slot);
	            for (Evento e : eventos_slot) {
		            if (e instanceof Aula) {
			            quantidade_alocacao_automatica_falhada((Aula) e);
			            quantidade_aulas_sobrelotacao((Aula)e);
			            quantidade_caracteristicas_mal_atribuidas((Aula) e, slot);
			           	mudanca_sala((Aula) e, slots);
		            }
		            if (e instanceof Avaliacao) {
		            	quantidade_avaliacoes_sobrelotacao((Avaliacao) e);
			            //quantidade_caracteristicas_desperdicadas((Aula) e);
		            }
	            }
	        }
	       mostra();
	}
	
	
	//quantidade de salas que não foram alocadas automáticamente
	int nao_foi_possivel_alocacao_automatica = 0;
	
	public void quantidade_alocacao_automatica_falhada(Aula aula) {
		if(!aula.getCaracteristica().equals("Não necessita de sala")) {
			if(aula.getSala() == null){
				nao_foi_possivel_alocacao_automatica++;
			}
		}
	}
	
	//Retorna a quantidade de aulas com possibilidade de sobrelotacao de salas e duas listas:
	//Uma lista do número de alunos que estão a mais sempre que existe uma sobrelotação das salas em aulas
	//Uma lista do número de lugares desperdiçados nas salas em aulas
	int aulas_sobrelotadas = 0;
	List<Integer> quantidade_alunos_sobrelotados_por_aulas = new ArrayList<Integer>();
	List<Integer> quantidade_alunos_desperdicados_por_aulas = new ArrayList<Integer>();
	List<Integer> quantidade_alunos_sobrelotados_por_aulas_percentagem = new ArrayList<Integer>();
	List<Integer> quantidade_alunos_desperdicados_por_aulas_percentagem = new ArrayList<Integer>();
	
	public void quantidade_aulas_sobrelotacao(Aula aula) {
		Sala sala = aula.getSala();
	    if(sala != null){
	    	if(!aula.getCaracteristica().equals("Não necessita de sala")) {
	            if(aula.getNumero_de_alunos() > sala.getCapacidade_normal()) {
		            //System.out.print("A sala " +sala.getNome()+ "numero de alunos inscritos = "+ aula.getNumero_de_alunos()+ " capacidade da sala"+ sala.getCapacidade_normal()+"\n");
		            aulas_sobrelotadas++;
					int alunos_a_mais = aula.getNumero_de_alunos() - sala.getCapacidade_normal();
		            quantidade_alunos_sobrelotados_por_aulas.add(alunos_a_mais);
					//int percentagem_a_mais = (alunos_a_mais / sala.getCapacidade_normal()) * 100;
					//quantidade_alunos_sobrelotados_por_aulas_percentagem.add(percentagem_a_mais);
	            }
	            else {
					int alunos_a_menos = sala.getCapacidade_normal() - aula.getNumero_de_alunos();
		            quantidade_alunos_desperdicados_por_aulas.add(alunos_a_menos);
					//int percentagem_a_menos = (alunos_a_menos / sala.getCapacidade_normal()) * 100;
					//quantidade_alunos_desperdicados_por_aulas_percentagem.add(percentagem_a_menos);
	            }
	        }
	    }
	}
	
	//quantidade de alunos com sobrelotação de aulas
	public int quantidade_alunos_sobrelotacao_aulas(){
		return quantidade_alunos_sobrelotados_por_aulas.stream().mapToInt(Integer::intValue).sum();
	}

	//Percentagem de alunos em sobrelotação, quando existe sobrelotação
	public int quantidade_percentagem_alunos_sobrelotacao_aulas(){
		int total_alunos_sobrelotacao = quantidade_alunos_sobrelotados_por_aulas.stream().mapToInt(Integer::intValue).sum();
		return total_alunos_sobrelotacao / quantidade_alunos_sobrelotados_por_aulas.size();
	}


	//quantidade_alunos_desperdicados_aulas, lugares vazios nas aulas
	public int quantidade_alunos_desperdicados_aulas(){
		return quantidade_alunos_desperdicados_por_aulas.stream().mapToInt(Integer::intValue).sum();
	}

	//Percentagem_alunos_desperdicados_aulas, lugares vazios nas aulas, quando não existe sobrelotação
	public int quantidade_percentagem_alunos_desperdicados_aulas(){
		int total_alunos_desperdicados = quantidade_alunos_desperdicados_por_aulas.stream().mapToInt(Integer::intValue).sum();
		return total_alunos_desperdicados / quantidade_alunos_desperdicados_por_aulas.size();
	}
	
	//Retorna a quantidade de avaliações com possibilidade de sobrelotacao de salas e duas listas:
	//Uma lista do número de alunos que estão a mais sempre que existe uma sobrelotação das salas em avaliações
	//Uma lista do número de lugares desperdiçados nas salas em avaliações
	int avaliacoes_sobrelotadas = 0;
	List<Integer> quantidade_alunos_sobrelotados_por_avaliacoes = new ArrayList<Integer>();
	List<Integer> quantidade_alunos_desperdicados_por_avaliacoes = new ArrayList<Integer>();
	
	public void quantidade_avaliacoes_sobrelotacao(Avaliacao avaliacao) {
		int capacidade_maxima_salas_avaliacao = 0;
		List<Sala> sala1 = avaliacao.getSalas();
		if(sala1 != null){
			for(Sala a : sala1) {
		    	capacidade_maxima_salas_avaliacao = capacidade_maxima_salas_avaliacao + a.getCapacidade_exame();
		    }
		    if(avaliacao.getNumero_de_alunos() > capacidade_maxima_salas_avaliacao) {
		        avaliacoes_sobrelotadas++;
		        quantidade_alunos_sobrelotados_por_avaliacoes.add((avaliacao.getNumero_de_alunos() - capacidade_maxima_salas_avaliacao));
		    }
		    else {
		         quantidade_alunos_desperdicados_por_avaliacoes.add((capacidade_maxima_salas_avaliacao - avaliacao.getNumero_de_alunos()));
		    }
		}
	}
	
	//quantidade de alunos com sobrelotação de avaliações
	public int quantidade_alunos_sobrelotacao_avaliacoes(){
		return quantidade_alunos_sobrelotados_por_avaliacoes.stream().mapToInt(Integer::intValue).sum();
	}
	
	//quantidade_alunos_desperdicados_avaliacoes, lugares vazios nas aulas
	public int quantidade_alunos_desperdicados_avaliacoes(){
		return quantidade_alunos_desperdicados_por_avaliacoes.stream().mapToInt(Integer::intValue).sum();
	}
	
	//quantidade de alunos com problemas de sobrelotação (aulas + avaliações)
	public int quantidade_alunos_sobrelotacao_total(){
		return quantidade_alunos_sobrelotados_por_aulas.stream().mapToInt(Integer::intValue).sum() + quantidade_alunos_sobrelotados_por_avaliacoes.stream().mapToInt(Integer::intValue).sum();
	}
	
	//quantidade de vezes que as características das salas são desperdiçadas, uma sala só é considerada desperdiçada
	//se tiver várias características e estiver a ser usada como uma sala normal.
	int quantidade_vezes_caracteristicas_desperdiçadas = 0;
	int quantidade_caracteristicas_desperdiçadas = 0;
	int aula_mal_atribuida = 0;
	//está mal pq eu não sei qual as caracteristicas que sao necessarias para ser considfrerado desperdicio, por exemplo, uma aula de mestrado tambem pode ser auditorio é desperdicio ?
	//lista de características pedida
	List<String> caracteristicas_pedidas = new ArrayList<String>();
	List<String> caracteristicas_desperdicadas = new ArrayList<String>();
	
	//quantidade_caracteristicas_desperdicadas(Aula aula)
	
	public void quantidade_caracteristicas_mal_atribuidas(Aula aula, Slot slot) {
		Sala sala = aula.getSala();
	    if(sala != null){
	    	List<String> caracteristicas_list = sala.getCaracteristicas();
	        String caracteristica_aula = aula.getCaracteristica();
	        boolean mal_atribuida = true;
	        for(String caracteristica : caracteristicas_list) { // se não for igual a outra sala e a caracteristica necessaria for normal faz break
	        	String firstFourChars = caracteristica_aula.substring(0, 4);
	            if((caracteristica.contains(caracteristica_aula)) || (((firstFourChars.equals("Sala") ||(firstFourChars.equals("Anfi"))) && caracteristica.equals("Sala/anfiteatro aulas")))) {
	            	mal_atribuida = false;
	            }
	        }
	        if(mal_atribuida) { 
			    aula_mal_atribuida++;
			    caracteristicas_pedidas.add(caracteristica_aula); 
			    quantidade_vezes_caracteristicas_desperdiçadas++;
			    quantidade_caracteristicas_desperdiçadas = quantidade_caracteristicas_desperdiçadas + caracteristicas_list.size();
			    for(String c : caracteristicas_list) {
			    	caracteristicas_desperdicadas.add(c);
			    }
			    //System.out.println("Entrei aki2: " + caracteristica_aula + " lista = "+caracteristicas_list);
	        }
	    }
	}
	
	public void aulas_mal_atribuidas() {
		HashMap<String, Integer> counts = new HashMap<>();
	    for (String element : caracteristicas_pedidas) {
	      if (counts.containsKey(element)) {
	        counts.put(element, counts.get(element) + 1);
	      } else {
	        counts.put(element, 1);
	      }
	    }
	    for (String element : counts.keySet()) {
	      System.out.println(element + ": " + counts.get(element));
	    }
	  }
	
	public void aulas_mal_atribuidas_carateristicas_desperdicadas() {
		HashMap<String, Integer> counts = new HashMap<>();
	    for (String element : caracteristicas_desperdicadas) {
	      if (counts.containsKey(element)) {
	        counts.put(element, counts.get(element) + 1);
	      } else {
	        counts.put(element, 1);
	      }
	    }
	    for (String element : counts.keySet()) {
	      System.out.println(element + ": " + counts.get(element));
	    }
	  }
	
	public void aulas_livres_slot(Slot slot) { //Em teoria estão aqui as carateristicas das salas livres por slot( não testado )
		HashMap<String, Integer> counts = new HashMap<>();
	    for (Sala element : slot.getSalas_livres()) {
	    	List<String> element_caracteristica = element.getCaracteristicas();
	    	for(String caracteristica : element_caracteristica) {
		    	if (counts.containsKey(caracteristica)) {
		    		counts.put(caracteristica, counts.get(caracteristica) + 1);
		    	} else {
		    		counts.put(caracteristica, 1);
		    	}
	    	}
	    }
	    for (String caracteristica : counts.keySet()) {
	      System.out.println(caracteristica + ": " + counts.get(caracteristica));
	    }
	}

	
	
	public int todas_salas_livres(){
		return salas_livres.stream().mapToInt(Integer::intValue).sum();
	}

	
	//Isto vai ver as mudanças de sala e de edificio numa aula de 3 horas da mesma disciplina
	int mudanca_edificio = 0;
	int mudanca_sala = 0;
	
	public void mudanca_sala(Aula aula_seguinte, List<Slot> slots) { //( não testado )
		for (Slot slot : slots) {
			List<Evento> eventos_slot = slot.eventos;
		    for (Evento e : eventos_slot) {
		    	if (e instanceof Aula) {
		    		Aula aula_anterior = (Aula) e;
			    	if(aula_seguinte.getData().equals(aula_anterior.getData())) {
						if(aula_anterior.getHora_final().equals(aula_seguinte.getHora_inicial())) {
							Sala sala_anterior = aula_anterior.getSala();
						    Sala sala_seguinte = aula_seguinte.getSala();
						    if(aula_anterior.getTurno().equals(aula_seguinte.getTurno())) {
							    if(!(sala_anterior.getEdificio().equals(sala_seguinte.getEdificio()))) {
							        mudanca_edificio++;
							    }
						    	if(!(sala_anterior.getNome().equals(sala_seguinte.getNome()))) {
						    		mudanca_sala++;
						        }
						    }
						}
			    	}
		    	}
		    }
		}
	}
	public void mudanca_sala2(Aula aula_anterior, List<Slot> slots) { //( não testado )
		for (Slot slot : slots) {
			List<Evento> eventos_slot = slot.eventos;
			for (Evento e : eventos_slot) {
				if (e instanceof Aula) {
					Aula aula_seguinte = (Aula) e;
					if(aula_anterior.getData().equals(aula_seguinte.getData())) {
						if(aula_seguinte.getHora_inicial().equals(aula_anterior.getHora_final())) {
							Sala sala_anterior = aula_seguinte.getSala();
							Sala sala_seguinte = aula_anterior.getSala();
							if(aula_seguinte.getTurno().equals(aula_anterior.getTurno())) {
								if(!(sala_anterior.getEdificio().equals(sala_seguinte.getEdificio()))) {
									mudanca_edificio++;
								}
								if(!(sala_anterior.getNome().equals(sala_seguinte.getNome()))) {
									mudanca_sala++;
								}
							}
						}
					}
				}
			}
		}
	}
	
	//Corre todas as turmas e faz 2 listas com os valores de todas as turmas. Uma com todas as mudanças de sala por cada turma e outra com todas as mudanças de edificio por cada turma.
	
	public void mudanca_sala_todas() {
		List<String> turmas = new ArrayList<>();
		List<Integer> mudanca_edificio_list = new ArrayList<>();
		List<Integer> mudanca_sala_list = new ArrayList<>();
		for (String turma : turmas) {
			//mudanca_sala(turma);
			mudanca_edificio_list.add(mudanca_edificio);
			mudanca_sala_list.add(mudanca_sala);
			mudanca_edificio = 0;
			mudanca_sala = 0;
		}
		System.out.print("o numero de mudanca de edificios de todas as turmas é de: " + mudanca_edificio_list.stream().mapToInt(Integer::intValue).sum());
		System.out.print("o numero de mudanca de salas de todas as turmas é de:  " + mudanca_sala_list.stream().mapToInt(Integer::intValue).sum());
	}
	
	 //quantida de salas livres por slot depois de alocar as aulas todas(quantas salas foram desperdiçadas)
	 // horas que cada tipo de sala ficou disponivel
	
	public void mostra() {
		
	   System.out.print("o numero de salas de aulas que não foram alocadas(null) são de: " + nao_foi_possivel_alocacao_automatica + "\n");
		
	   System.out.print("o numero de salas de aulas sobrelotadas é de: " + aulas_sobrelotadas + "\n");
	   
	   System.out.print("os estudantes afetados por esta sobrelotação de salas de aulas é de: " + quantidade_alunos_sobrelotacao_aulas()+"\n");
	   
	   System.out.print("O número de lugares desperdiçados nas aulas é de: " + quantidade_alunos_desperdicados_aulas() + "\n");
	   
	   System.out.print("O número de aulas mal atribuidas: " + aula_mal_atribuida + "\n");
	   
	   aulas_mal_atribuidas();
	   
	   System.out.print("O número de aulas que ficaram livres/disponiveis : " + todas_salas_livres() + "\n"); //(não testado)(não existe lista de salas livres)

	   System.out.print("O número de aulas de três horas que tiveram mudanças de sala : " + mudanca_sala + "\n"); //(não testado) (falta nome das salas)
	   
	   System.out.print("O número de aulas de três horas que tiveram mudanças de edificio : " + mudanca_edificio + "\n"); //(não testado)(falta edificio nas salas)
	   
	   System.out.print("o numero de salas de exames sobrelotados é de: " + avaliacoes_sobrelotadas +"\n");
	   
	   System.out.print("O numero de salas de aulas mal atribuidas que tiveram caracteristicas desperdiçadas foi de:" + quantidade_vezes_caracteristicas_desperdiçadas + "\n");
	   
	   System.out.print("O numero de caracteristicas em potêncial desperdiçadas por cause de salas de aulas mal atribuidas foi de:" + quantidade_caracteristicas_desperdiçadas + "\n");
	   
	   aulas_mal_atribuidas_carateristicas_desperdicadas();
	   
	   System.out.print("os estudantes afetados por esta sobrelotação de salas de exames é de: " + quantidade_alunos_sobrelotacao_avaliacoes()+"\n");
	
	   System.out.print("O número de lugares desperdiçados nas avaliações é de: " + quantidade_alunos_desperdicados_avaliacoes()+ "\n");
	   
	}
	
}