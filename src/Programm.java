import java.util.ArrayList;
import java.util.Scanner;

public class Programm {	
	// Liste des turbines
	private ArrayList<Turbine> list_turbine = new ArrayList<Turbine>();

	// Liste des initialisations
	private ArrayList<Initialisation> list_init = new ArrayList<Initialisation>();
	
	private float qTurbine;
	private float elevAmont;
	private float streamFlowExceed;
	private float finalPowerProduction;

	public float getFinalPowerProduction() {
		return finalPowerProduction;
	}

	public void setFinalPowerProduction(float finalPowerProduction) {
		this.finalPowerProduction = finalPowerProduction;
	}

	public float getStreamFlowExceed() {
		return streamFlowExceed;
	}

	public void setStreamFlowExceed(float streamFlowExceed) {
		this.streamFlowExceed = streamFlowExceed;
	}
	
	// Première étape
	private float[][] firstStep;
	
	// Étape secondaire
	private float[][] temporyStep;
	private float[][] finalStep;
	
	private float[][] previousStep;
	private float[][] lastStep;
	private int indexPreviousStep = 0;
	
	/*
	 * Initialisation
	 */
	public Programm(float qTurbine, float elevAmont, ArrayList<Initialisation> list_init) {
		this.qTurbine = qTurbine;
		this.elevAmont = elevAmont;
		
		// On regarde si list_init est supérieur à 2 élements sinon on considère la centrale comme fermée
		assert(list_init.size() > 2);
		int qTemp = 0;
		
		// initialisation de la turbine 1
		list_turbine.add(new Turbine((list_init.get(0).index), list_init.get(0).qmin, list_init.get(0).qmax, 1, true, qTurbine));
		
		// initialisation des autres turbines
		for(int i=1; i< list_init.size(); i++) {
			qTemp = 0;
			for(int j=i; j < list_init.size(); j++) {
				qTemp = qTemp + list_init.get(j).qmax;
				
				// on regarde si débit turbiné supérieur à débit max de toutes les turbines
				if(i == 1 && j==list_init.size()-1 && (qTurbine > qTemp + list_init.get(0).qmax)) {
					setStreamFlowExceed((float) (qTurbine - (qTemp + list_init.get(0).qmax)));
					System.out.print("Débit relaché (qui ne produit pas d'energie) = " + (qTurbine - (qTemp + list_init.get(0).qmax)));
					this.qTurbine = qTemp + list_init.get(0).qmax;
					
					// change turbine 1 Qturbine
					list_turbine.get(0).changeFirstTurbineQturbine(qTemp + list_init.get(0).qmax);
				}
			}
			list_turbine.add(new Turbine(list_init.get(i).index,list_init.get(i).qmin, list_init.get(i).qmax, qTemp, false, qTurbine));
		}
	}
	
	public Programm(float qTurbine, float elevAmont, boolean turbines_enable, boolean turbines_SameStreamFlow) {
		this.qTurbine = qTurbine;
		this.elevAmont = elevAmont;
		
		if(turbines_enable && turbines_SameStreamFlow) {
			//Cas 1
			System.out.println("turbine 1");
			list_turbine.add(new Turbine(1, 0, 180, 1, true, qTurbine));
			//Cas 2 3 et 4
			System.out.println("turbine 2/3/4/5");
			for(int i = 2; i <= 5; i++) {
				list_turbine.add(new Turbine(i, 0, 180, (5 - i + 1)*180, false, qTurbine));
			}
			//Cas 5
			System.out.println("turbine 5");
		} 
		
		else{
			Scanner turbines = new Scanner(System.in);
			
			System.out.println("Toutes les turbines ne sont pas en marche! \n");
			for(int i = 0; i < 5; i++) {
				boolean turbineEnable;
				float maxStreamFlow; 
				// Turbine i+1 en marche ?
				System.out.println("La turbine " + (i+1) +" fonctionne-t-elle? o/n \n");
				String turbine = turbines.nextLine();
				while(!turbine.equalsIgnoreCase("o") && !turbine.equalsIgnoreCase("n")) {
					System.out.println("Réponse incorrect! La turbine " + (i+1) + " fonctionne-t-elle? o/n \n");
					turbine = turbines.nextLine();
				}
				
				if(turbine.equalsIgnoreCase("o")){
					// La turbine i+1 fonctionne, on demande maintenant si elle a un débit max de 180 m3/s
					System.out.println("La turbine " + (i+1) +" fonctionne ! A-t-elle un débit max = 180m3/s ? o/n \n");
					turbine = turbines.nextLine();
					while(!turbine.equalsIgnoreCase("o") && !turbine.equalsIgnoreCase("n")) {
						System.out.println("Réponse incorrect! La turbine " + i+1 + " a-t-elle un débit max = 180m3/s ? o/n \n");
						turbine = turbines.nextLine();
					}
					if(turbine.equalsIgnoreCase("o")){
						list_init.add(new Initialisation(i+1));
					} else {
						//Réponse fausse donnée avant : On demande la valeur de débit max pour la i+1ème turbine
						System.out.println("Quel est le débit de la " + (i+1) + "ère/nde/ème turbine ? \n");
						turbine = turbines.nextLine();
						while(Integer.parseInt(turbine) < 0) {
							System.out.println("Réponse incorrect ! Quel est le débit de la " + i+1 + "ère/nde/ème turbine ? \n");
							turbine = turbines.nextLine();
						}
						list_init.add(new Initialisation(i+1, Integer.parseInt(turbine)));
					}
				}
			}
			/*
			 * Initialisation
			 */
			// On regarde si list_init est supérieur à 2 élements
			assert(list_init.size() > 2);
			int qTemp = 0;
			// initialisation de la turbine 1
			list_turbine.add(new Turbine((list_init.get(0).index), list_init.get(0).qmin, list_init.get(0).qmax, 1, true, qTurbine));
			// initialisation des autres turbines
			for(int i=1; i< list_init.size(); i++) {
				qTemp = 0;
				for(int j=i; j < list_init.size(); j++) {
					qTemp = qTemp + list_init.get(j).qmax;
					// on regarde si débit turbiné supérieur à débit max de toutes les turbines
					if(i == 1 && j==list_init.size()-1 && (qTurbine > qTemp + list_init.get(0).qmax)) {
						setStreamFlowExceed((float) (qTurbine - (qTemp + list_init.get(0).qmax)));
						System.out.print("Débit relaché (qui ne produit pas d'energie) = " + (qTurbine - (qTemp + list_init.get(0).qmax)));
						this.qTurbine = qTemp + list_init.get(0).qmax;
						// change turbine 1 Qturbine
						list_turbine.get(0).changeFirstTurbineQturbine(qTemp + list_init.get(0).qmax);
					}
				}
				list_turbine.add(new Turbine(i+1,list_init.get(i).qmin, list_init.get(i).qmax, qTemp, false, qTurbine));
			}
		}
	}
	
	public void FirstStep(Turbine turbine) {
		System.out.print("étape n= " + turbine.getIndex() + "\n"); 
		// La première étape doit changer dynamiquement, par exemple si la turbine 5 est arrêté il faut prendre la turbine 4.
		/*
		 *	S5		|	f5(S5)		|	x5*
		 *	0		|	0			|	0
		 *	5 		|	f5(5)		|	5
		 *	10		|	f5(10)		|	10
		 * 	15		|	f5(15)		|	15
		 * 	..		|	..			|	..
		 * 	S5max	|	f5(S5max)	|	S5max
		 */
		
		// Récupérer le tableau 
		firstStep = new float[turbine.getFinalTable().length][turbine.getFinalTable()[0].length];
		firstStep = turbine.getFinalTable();
		
		for(int i = 0; i < firstStep.length; i++)
		{    
			// fonction
			firstStep[i][1] = ChooseFunction(turbine.getIndex(), firstStep[i][0]);
			// X*n
			firstStep[i][2] = firstStep[i][0];
		}
		turbine.setTemporyTable(firstStep);
		turbine.setFinalTable(firstStep);
		turbine.ShowFinalTable();
	}
	
	public void MiddleStep(Turbine turbine) {
		System.out.print("étape n= " + turbine.getIndex() + "\n"); 
		// Cette méthode prend une turbine à la fois, rempli un nouveau tableau et ajoute celui-ci à une liste de tableau.
		/* i = 2;3;4
		 * 
		 *	Si/xi	|		0		|	5			|	10	|	15	|	..	|	n	|
		 *	0		|		0		|	x			|	x	|	x	|	x	|	x	|	
		 *	5 		|		f5(5)	|	f5(5)+fi	|	x	|	x	|	x	|	x	|	
		 *	10		|		f5(10)	|	..			|	..	|	x	|	x	|	x	|	
		 * 	15		|		f5(15)	|	..			|	..	|	..	|	x	|	x	|	
		 * 	..		|		..		|	..			|	..	|	..	|	..	|	x	|
		 * 	180*n	|	f5(180*n)	|	
		 */
		
		// Récupérer le tableau temporaire
		temporyStep = new float[turbine.getTemporyTable().length][turbine.getTemporyTable()[0].length];
		temporyStep = turbine.getTemporyTable();
		// tableau final étape
		finalStep = new float[turbine.getFinalTable().length][turbine.getFinalTable()[0].length];
		finalStep = turbine.getFinalTable();
		// L'ancien tableau à l'index n-1 de la liste de tableau
		previousStep = list_turbine.get(indexPreviousStep).getFinalTable();
		
		
		for(int i = 0; i < temporyStep.length; i++)
		{    
			for(int j = 0; j < temporyStep[0].length; j++)
			{
				if(j != 0) {
					// Ne prendre en compte que les valeurs possibles
					if(!(temporyStep[i][j] == -100000)) {
						temporyStep[i][j] = ChooseFunction(turbine.getIndex(), temporyStep[j-1][0]);
						temporyStep[i][j] = temporyStep[i][j] + previousStep[i - j +1][1];
						
						// On prend la meilleure valeur de la ligne et le débit qui lui a été donné
						if(finalStep[i][1] < temporyStep[i][j]) {
							finalStep[i][1] = temporyStep[i][j];
							finalStep[i][2] = turbine.getFirstLine()[j-1];
						}
					}
				}
			}
		}		
		// Affichage tableau final
		turbine.setTemporyTable(temporyStep);
		turbine.setFinalTable(finalStep);
		//turbine.ShowTemporyTable();
		turbine.ShowFinalTable();
		
	}
	
	public void LastStep(Turbine turbine) {
		System.out.print("étape n= " + turbine.getIndex() + "\n");
		// Récupérer le tableau temporaire
		temporyStep = new float[1][turbine.getTemporyTable()[0].length+1];
		temporyStep = turbine.getTemporyTable();
		// tableau final étape
		finalStep = new float[1][3];
		finalStep = turbine.getFinalTable();
		// L'ancien tableau à l'index n-1 de la liste de tableau
		previousStep = list_turbine.get(indexPreviousStep).getFinalTable();
		
		
		for(int i = 0; i < temporyStep[0].length; i++)
		{
			if(i != 0) {
				temporyStep[0][i] = ChooseFunction(turbine.getIndex(), turbine.getFirstLine()[i-1]);
				
				if(temporyStep[0][0] - (i-1)*5 <= list_turbine.get(turbine.getIndex()).getSommeDiscretisation()) {
					temporyStep[0][i] = temporyStep[0][i] + previousStep[(int) (temporyStep[0][0]/5.0 - i + 1)][1];

					// On prend la meilleure valeur de la ligne et le débit qui lui a été donné
					if(finalStep[0][1] < temporyStep[0][i]) {
						finalStep[0][1] = temporyStep[0][i];
						finalStep[0][2] = turbine.getFirstLine()[i-1];
					}
				}else {
					temporyStep[0][i] = -100000;
				}
			}
		}		
		// Affichage tableau final
		turbine.setTemporyTable(temporyStep);
		turbine.setFinalTable(finalStep);
		turbine.ShowTemporyTable();
		turbine.ShowFinalTable();
	}
	
	// Cette méthode permet de choisir la fonction à calculer en fonction de l'index de la turbine concernée.
	public float ChooseFunction(int index, float Qturbine) {
		float result = 0;
		switch(index) {
			case 1:
				result =  function1(Qturbine);
				break;
			case 2:
				result =  function2(Qturbine);
				break;
			case 3:
				result =  function3(Qturbine);
				break;
			case 4:
				result =  function4(Qturbine);
				break;
			case 5:
				result =  function5(Qturbine);
				break;
			default:
				break;
		}
		return result;
	}

	public float function1(float Qturbine){
		//P1(x,y) = − 4,145×10−4y2 + 9,065×10−3xy − 2,168×10−3x2 + 3,574×10−2y + 1.352×10−1x − 2.102
		
		float hauteurChutteNette1 = CalculateHauteurChuteNette(Qturbine);
		float productionTurbine1 = (float) ((-4.145 * Math.pow(10.0, -4.0) * Math.pow(Qturbine, 2)) + ((9.065 * Math.pow(10.0, -3.0) * hauteurChutteNette1 * Qturbine)) - (2.168 * Math.pow(10.0, -3.0) * Math.pow(hauteurChutteNette1,2)) + (3.574 * Math.pow(10.0, -2.0) * Qturbine) + (1.352 * Math.pow(10.0, -1.0) * hauteurChutteNette1) - 2.102) ;
		return productionTurbine1;
	}
	
	public float function2(float Qturbine){
		//P2(x,y) = − 3,259×10−4y2 + 9,546×10−3x∗y + 4,397×10−3x2 + 1,201×10−2y − 3.009×10−1x + 5,162
		
		float hauteurChutteNette2 = CalculateHauteurChuteNette(Qturbine);
		float productionTurbine2 = (float) ((-3.259 * Math.pow(10.0, -4.0) * Math.pow(Qturbine, 2)) + ((9.546 * Math.pow(10.0, -3.0) * hauteurChutteNette2 * Qturbine)) + (4.397 * Math.pow(10.0, -3.0) * Math.pow(hauteurChutteNette2,2)) + (1.201 * Math.pow(10.0, -2.0) * Qturbine) - (3.009 * Math.pow(10.0, -1.0) * hauteurChutteNette2) + 5.162) ;
		return productionTurbine2;
	}
	
	public float function3(float Qturbine){
		//P3(x,y) = -1.562×10−5x2*y - 1.562.10-5x3+ 6.494×10−3x∗y + 3.543×10−3x2 + 1.672×10−3y − 1.672×10−1x - 4.805.10-2
		float hauteurChutteNette3 = CalculateHauteurChuteNette(Qturbine);
		
		float productionTurbine3 = (float) ((-1.562 * Math.pow(10.0, -5.0) * Math.pow(Qturbine, 2) * hauteurChutteNette3) 
				- (1.562 * Math.pow(10.0, -5.0) * Math.pow(Qturbine, 3)) + ((6.494 * Math.pow(10.0, -3.0) * hauteurChutteNette3 * Qturbine)) 
				+ (3.543* Math.pow(10.0, -3.0) * Math.pow(Qturbine, 2)) + (1.672 * Math.pow(10.0, -3.0) *hauteurChutteNette3) 
				- (1.672 * Math.pow(10.0, -1.0) * Qturbine) + (Math.pow(1.507 , 2))) ;
		return productionTurbine3;
	}
	
	public float function4(float Qturbine){
		//P4(x,y) = - 3.321×10−4y2 + 9.951×10−3x∗y + 8.414×10−3x2 + 1.507×10−2y − 5.092×10−1x + 7.627
		float hauteurChutteNette4 = CalculateHauteurChuteNette(Qturbine);
		
		float productionTurbine4 = (float) ((-3.321 * Math.pow(10.0, -4.0) * Math.pow(Qturbine, 2)) + ((9.951 * Math.pow(10.0, -3.0) * hauteurChutteNette4 * Qturbine)) + (8.414 * Math.pow(10.0, -3.0) * Math.pow(hauteurChutteNette4,2)) + (1.507 * Math.pow(10.0, -2.0) * Qturbine) - (5.092 * Math.pow(10.0, -1.0) * hauteurChutteNette4) + 7.627) ;
		return productionTurbine4;
	}
	
	public float function5(float Qturbine){
		//P5(x,y) = - 4.07×10−4y2 + 1.083×10−2x∗y + 9.861×10−3x2 - 9.553×10−3y − 6.275×10−1x + 9.954
		float hauteurChutteNette5 = CalculateHauteurChuteNette(Qturbine);
		
		float productionTurbine5 = (float) ((-4.071 * Math.pow(10.0, -4.0) * Math.pow(Qturbine, 2)) + ((1.083 * Math.pow(10.0, -2.0) * hauteurChutteNette5 * Qturbine)) + (9.861 * Math.pow(10.0, -3.0) * Math.pow(hauteurChutteNette5,2)) - (9.553 * Math.pow(10.0, -2.0) * Qturbine) - (6.275 * Math.pow(10.0, -1.0) * hauteurChutteNette5) +  9.954) ;
		return productionTurbine5;
	}
	
	public float CalculateHauteurChuteNette(float Qturbine) {
		// HauteurChuteNette = ((eam-eav(Qtot))-0,5*e-5 * Q²)
		// eaval(Qtot) = -7,179*10-7x² + 4,111*10-3x + 137,2
		float eAval = (float) (-7.179 * Math.pow(10.0, -7.0) * Math.pow(Qturbine, 2) + 4.111 * Math.pow(10.0, -3.0) * Qturbine + 137.2);
		return (float) ((elevAmont - eAval));
		//- 0.5 * Math.exp(-5.0) * Math.pow(Qturbine, 2));		
	}
	
	public void RunBackward() {

		System.out.print("\n Backward : \n");
		System.out.println(list_turbine.size());
		for(int i = list_turbine.size()-1; i > 0 ; i--)
		{
			if(i == list_turbine.size()-1) {
				// Faire étape 1
				FirstStep(list_turbine.get(i));
				// Set a new previous index step
				indexPreviousStep = i;
			}
			else {
				// Faire étape 2 à n-1
				MiddleStep(list_turbine.get(i));

				// Set a new previous index step
				indexPreviousStep = i;
			}
		}
		// Faire dernière étape
		LastStep(list_turbine.get(0));
	}
	
	public void RunForward() {
		/*
		 * Recalculer pour chaque débit la production d'une turbine puis faire le set StreamPower
		 */
		System.out.print("\n Forward : \n");
				
		int cumuledStreamFlow = 0;
		for(int i = 0; i < list_turbine.size(); i++)
		{
			// Récupérer la turbine en cours
			Turbine currentTurbine = list_turbine.get(i);
					
			// Premier tableau différent des autres.
			if(i == 0) {
				// Puissance totale
				temporyStep = new float[1][3];
				temporyStep = currentTurbine.getFinalTable();
				setFinalPowerProduction(temporyStep[0][1]);
				System.out.print(" Puissance totale récupérée par toutes les turbines: " + getFinalPowerProduction() + "  mW   \n");

				// récupérer le débit qui maximise pour l'étape 1
				int firstTurbineStreamFlow = (int) temporyStep[0][2];
				temporyStep = new float[1][currentTurbine.getTemporyTable()[0].length];
				temporyStep = currentTurbine.getTemporyTable();
				
				System.out.print("Turbine n = " + list_turbine.get(i).getIndex() + "\n");
				currentTurbine.setFinalStreamFlow(firstTurbineStreamFlow);
				currentTurbine.setFinalPowerFlow(ChooseFunction(currentTurbine.getIndex(),firstTurbineStreamFlow));
				// On fait la soustraction de Qturbine avec le débit de la turbine 1
				cumuledStreamFlow = (int) (qTurbine - currentTurbine.getFinalStreamFlow());
				System.out.print(" Débit à donner : " + currentTurbine.getFinalStreamFlow() + " m3/s    \n");
				System.out.print(" Puissance récupérée pour cette turbine : " + currentTurbine.getFinalPowerFlow() + "  mW   \n");
			}
			else {
				System.out.print("Turbine n = " + list_turbine.get(i).getIndex() + "\n");

				// récupérer le débit qui maximise pour l'étape 2 à n
				temporyStep = new float[currentTurbine.getFinalTable().length][currentTurbine.getFinalTable()[0].length];
				temporyStep = currentTurbine.getFinalTable();

				int middleTurbineStreamFlow = (int) temporyStep[cumuledStreamFlow/5][2];
				currentTurbine.setFinalStreamFlow(middleTurbineStreamFlow);
				currentTurbine.setFinalPowerFlow(ChooseFunction(currentTurbine.getIndex(), middleTurbineStreamFlow));

				// On fait la soustraction de Qturbine avec le débit de la turbine 1
				cumuledStreamFlow = (int) (cumuledStreamFlow - currentTurbine.getFinalStreamFlow());
				System.out.print(" Débit à donner : " + currentTurbine.getFinalStreamFlow() + " m3/s    \n");
				System.out.print(" Puissance récupérée pour cette turbine : " + currentTurbine.getFinalPowerFlow() + "  mW   \n");
			}
		}
	}
	
	
	public ArrayList<Turbine> getList_turbine() {
		return list_turbine;
	}

	public void setList_turbine(ArrayList<Turbine> list_turbine) {
		this.list_turbine = list_turbine;
	}

}
