package Files;

public class MetodosParaAulas {

    public int presevar_caracteristica_da_aula(int i) {
        System.out.println("MetodosParaAulas: presevar_característica_da_aula");
        int temp = i+5;
        return temp;
    }

    public void garantir_que_haja_lugar_para_todos(int i) {
        System.out.println("MetodosParaAulas: method2");
    }

    public void menor_distancia_entre_salas_numa_aula(int i) {
        System.out.println("MetodosParaAulas: menor_distancia_entre_salas_numa_aula");
    }

    /*
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
		if (list_methods.get(0).equals("caracteristica")) { // aqui o nome dps logo se vê
			
			salas_possiveis = keepCaracteristic(sala_apos_metodo, a, help);
		}

		if (list_methods.get(0).equals("evitar sobrelotaçao")) { // aqui o nome dps logo se vê
			
			salas_possiveis = evitarSobrelotacao(sala_apos_metodo, a, help);
		}
		if (list_methods.get(0).equals("menor distancia")) { // aqui o nome dps logo se vê
			salas_possiveis = sala_menor_distancia(a, help);
		}

		if (salas_possiveis.size()== 0) {
			salas_possiveis = sala_apos_metodo;
			if (sala_apos_metodo.size()==0) {
				salas_possiveis = resolver_conflito(a,help);
			}
		}
		
		if (list_methods.size()>1) {
			System.out.println("salas possiveis:");

			for (Sala s : salas_possiveis) {
				System.out.println(s);
			}
			doMethods(salas_possiveis, a, help, list_methods.subList(1, list_methods.size()));
			return null;
		} else {
		
			int index = 0;
			if(salas_possiveis.size() > 1) {
				index = rand.nextInt(salas_possiveis.size() - 1);
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
		//for(Sala sa: salas_to_return) {
		//	System.out.println(sa.getNome() + " com " + sa.getCapacidade_normal());
		//}
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
		//for(Sala s: salas_livres_com_caracteristica)
		//System.out.println(s);
		List<Sala> salas_to_return = new ArrayList<>();
		salas_to_return = salas_livres_com_caracteristica;
		return salas_to_return;
	}
    



/*
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
    */
    
    
    
    
}
