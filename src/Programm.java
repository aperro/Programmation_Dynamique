import java.util.ArrayList;
import java.util.Scanner;

public class Programm {	
	// Liste des turbines
	private ArrayList<Turbine> list_turbine = new ArrayList<Turbine>();
	// Liste de tous les tableaux que l'on garde en mémoire
	private ArrayList<float[][]> list_table = new ArrayList<float[][]>();
	
	private int qTurbine;
	private float elevAmont;

	private float[][] firstStep;
	private float[][] thisStep;
	private float[][] temporyStep;
	private float[][] previousStep;
	private float[][] lastStep;
	private int indexPreviousStep = 0;
	
	public Programm(int qTurbine, float elevAmont, boolean turbines_enable, boolean turbines_SameStreamFlow) {
		this.qTurbine = qTurbine;
		this.elevAmont = elevAmont;
		
		if(turbines_enable && turbines_SameStreamFlow) {
			for(int i = 0; i < 5; i++) {
				list_turbine.add(new Turbine(i+1, qTurbine));
			}
		} 
		else{
			Scanner turbines = new Scanner(System.in);
			
			System.out.println("Toutes les turbines ne sont pas en marchent! \n");
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
						list_turbine.add(new Turbine(i+1, qTurbine));
					} else {
						//Réponse fausse donnée avant : On demande la valeur de débit max pour la i+1ème turbine
						System.out.println("Quel est le débit de la " + (i+1) + "ère/nde/ème turbine ? \n");
						turbine = turbines.nextLine();
						while(Integer.parseInt(turbine) < 0) {
							System.out.println("Réponse incorrect ! Quel est le débit de la " + i+1 + "ère/nde/ème turbine ? \n");
							turbine = turbines.nextLine();
						}
						list_turbine.add(new Turbine(i+1, Integer.parseInt(turbine), qTurbine));
					}
				}
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
		
		// régler la taille du tableau de manière dynamique
		firstStep = new float[turbine.getList_discretisation().size()][3];

		// Autant de ligne que d'éléments dans discretisationList
		for(int i = 0; i < turbine.getList_discretisation().size(); i++)
		{    
			// 3 colonnes
			for(int j = 0; j < 3; j++)
			{
				// Sn
				firstStep[i][0] = turbine.getList_discretisation().get(i);
				// fonction
				firstStep[i][1] = ChooseFunction(turbine.getIndex(), turbine.getList_discretisation().get(i));
				
				// On regarde si on ne dépasse pas la valeur max de débit pour la turbine
				if(turbine.getList_discretisation().get(i) > turbine.getMaxStreamFlowTurbine())
				{
					firstStep[i][1] = 0;
				}
				// X*n
				firstStep[i][2] = turbine.getList_discretisation().get(i);
			}
			System.out.print(firstStep[i][0] + " m3/s    " + firstStep[i][1] + " mW     " + firstStep[i][2] + " m3/s ");   
			System.out.println("\n");
		}
		list_table.add(firstStep);
	}
	
	public void MiddleStep(Turbine turbine) {
		System.out.print("étape n= " + turbine.getIndex() + "\n"); 
		// Cette méthode prend une turbine à la fois, rempli un nouveau tableau et ajoute celui-ci à une liste de tableau.
		/* i = 2;3;4
		 * 
		 *	Si/xi	|	0		|	5			|	10	|	15	|	..	|	n	|
		 *	0		|	0		|	x			|	x	|	x	|	x	|	x	|	
		 *	5 		|	f5(5)	|	f5(5)+fi	|	x	|	x	|	x	|	x	|	
		 *	10		|	f5(10)	|	..			|	..	|	x	|	x	|	x	|	
		 * 	15		|	f5(15)	|	..			|	..	|	..	|	x	|	x	|	
		 * 	..		|	..		|	..			|	..	|	..	|	..	|	x	|
		 * 	n		|	f5(n)	|	
		 */
		
		// L'ancien tableau à l'index n-1 de la liste de tableau
		previousStep = list_table.get(indexPreviousStep);
		// tableau intermédiaire index n
		temporyStep = new float[turbine.getList_discretisation().size()][turbine.getList_discretisation().size()];
		// tableau final étape n
		thisStep = new float[turbine.getList_discretisation().size()][3];
		
		// Autant de ligne que d'éléments dans discretisationList
		for(int i = 0; i < turbine.getList_discretisation().size(); i++)
		{    
				thisStep[i][1] = -100;
				// Autant de colonnes que d'éléments dans discretisationList
				for(int j = 0; j < turbine.getList_discretisation().size(); j++)
				{
					thisStep[i][0] = turbine.getList_discretisation().get(i);
					// Première colonne
					if(j == 0) {
						temporyStep[i][j] = turbine.getList_discretisation().get(i);
						// J'écris ça avant d'écrire le tableau.
						//System.out.print(thisStep[i][0] + " m3/s     "); 
					} 
					// si colonne index supérieur à ligne index = X
					if(j > i)
					{
						temporyStep[i][j] = -1;
					}
					else {
						// Fn+1(Sn) ok
						temporyStep[i][j] = ChooseFunction(turbine.getIndex(), turbine.getList_discretisation().get(j));
						
						temporyStep[i][j] = temporyStep[i][j] + previousStep[i - j][1];
						
						// On regarde si on ne dépasse pas la valeur max de débit pour la turbine
						if(turbine.getList_discretisation().get(i) > turbine.getMaxStreamFlowTurbine())
						{
							temporyStep[i][j] = 0;
						}
						
						// On prend la meilleure valeur de la ligne et le débit qui lui a été donné
						if(thisStep[i][1] < temporyStep[i][j]) {
							thisStep[i][1] = temporyStep[i][j];
							thisStep[i][2] = turbine.getList_discretisation().get(j);
						}
					}
					
					if(temporyStep[i][j] != -1) {
						//System.out.print("   " + temporyStep[i][j] + " mW    "); 
					}
					else {
						//System.out.print("   " + temporyStep[i][j] + "       "); 
					}
				}
			//System.out.println("\n");
		}
		//System.out.println("\n");
		
		// Affichage du tableau final
		for(int i = 0; i < turbine.getList_discretisation().size(); i++)
		{  
				for(int j = 0; j < 3; j++)
				{
					if(j == 0) {
						System.out.print(thisStep[i][j] + " m3/s     "); 
					} 
					if(j == 1) {

						System.out.print(thisStep[i][j] + " mW     ");
					}
					if(j == 2) {
						System.out.print(thisStep[i][j] + " m3/s     ");
					}
				}
			 System.out.println("\n");
		}
		// On ajoute le tableau à la liste de tableau
		list_table.add(thisStep);
		indexPreviousStep++;
	}
	
	public void LastStep(Turbine turbine) {
		System.out.print("étape n= " + turbine.getIndex() + "\n"); 
		
		// L'ancien tableau à l'index n-1 de la liste de tableau
		previousStep = list_table.get(indexPreviousStep);
		
		// tableau intermédiaire index n
		float[] temporyLastStep = new float[turbine.getList_discretisation().size()];
		
		// tableau final étape finale
		lastStep = new float[1][3];
		
		lastStep[0][1] = -100;
		// Autant de colonnes que d'éléments dans discretisationList + fn*(Sn) + X*n
		for(int j = 0; j < turbine.getList_discretisation().size(); j++)
		{
			// Première colonne
			if(j == 0) {
				lastStep[0][j] = turbine.getList_discretisation().get(turbine.getList_discretisation().size() - 1);
				//
				temporyLastStep[j] = ChooseFunction(turbine.getIndex(), turbine.getList_discretisation().get(j));
				temporyLastStep[j] = temporyLastStep[j] + previousStep[turbine.getList_discretisation().size() - 1 - j][1];
				
				System.out.print(lastStep[0][j] + " m3/s   ");   
			} 
			else {
				// Fn+1(Sn) ok
				temporyLastStep[j] = ChooseFunction(turbine.getIndex(), turbine.getList_discretisation().get(j));
				
				temporyLastStep[j] = temporyLastStep[j] + previousStep[turbine.getList_discretisation().size() - 1 - j][1];
				
				if(turbine.getList_discretisation().get(j) > turbine.getMaxStreamFlowTurbine()) {
					temporyLastStep[j] = 0;
				}
			}
			// On prend la meilleure valeur de la ligne et le débit qui lui a été donné
			if(lastStep[0][1] < temporyLastStep[j]) {
				lastStep[0][1] = temporyLastStep[j];
				lastStep[0][2] = turbine.getList_discretisation().get(j);
			}
			System.out.print("  " + temporyLastStep[j] + " mW    ");
		}
		System.out.print("\n"); 
		
		// Affichage du tableau final
		for(int j = 0; j < 3; j++)
		{
			if(j == 0) {
				System.out.print(lastStep[0][j] + " m3/s     "); 
			} 
			if(j == 1) {

				System.out.print(lastStep[0][j] + " mW     ");
			}
			if(j == 2) {
				System.out.print(lastStep[0][j] + " m3/s     ");
			}
		}
		// On ajoute le tableau à la liste de tableau
		list_table.add(lastStep);
		System.out.print("\n");
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
		//P3(x,y) = 2.743×10−3y2 + 9.896×10−3x∗y + 2.222×10−3x2 - 2.912×10−1y − 1.184×10−1x + 1.507
		float hauteurChutteNette3 = CalculateHauteurChuteNette(Qturbine);
		
		float productionTurbine3 = (float) ((2.743 * Math.pow(10.0, -3.0) * Math.pow(Qturbine, 2)) + ((9.896 * Math.pow(10.0, -3.0) * hauteurChutteNette3 * Qturbine)) + (2.222 * Math.pow(10.0, -3.0) * Math.pow(hauteurChutteNette3,2)) - (2.912 * Math.pow(10.0, -1.0) * Qturbine) - (1.184 * Math.pow(10.0, -1.0) * hauteurChutteNette3) + 1.507) ;
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
		for(int i = list_turbine.size()-1; i > 0 ; i--)
		{
			if(i == list_turbine.size()-1) {
				// Faire étape 1
				FirstStep(list_turbine.get(i));
			}
			else {
				// Faire étape 2 à n-1
				MiddleStep(list_turbine.get(i));
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
		
		// entier qui nous indique combien de débit est cumulé par itération - ne doit pas dépasser débit maximum par turbine
		int currentDebitCumul = 0;
		
		for(int i = 0; i < list_turbine.size(); i++)
		{
			// Premier tableau différent des autres.
			if(i == 0) {
				thisStep = new float[1][3];
				thisStep = list_table.get(list_turbine.size() - 1 - i);
				
				System.out.print(" Puissance totale récupérée par toutes les turbines: " + thisStep[0][1] + "  mW   \n");
				
				// récupérer le débit qui maximise pour l'étape 1
				System.out.print("Turbine n = " + (i+1) + "\n");
				currentDebitCumul = (int) thisStep[0][2];
				list_turbine.get(i).setFinalStreamFlow((int) thisStep[0][2]);
				list_turbine.get(i).setFinalPowerFlow(ChooseFunction(list_turbine.get(i).getIndex(), (int) thisStep[0][2]));
				System.out.print(" Débit à donner : " + list_turbine.get(i).getFinalStreamFlow() + " m3/s    \n");
				System.out.print(" Puissance récupérée pour cette turbine : " + list_turbine.get(i).getFinalPowerFlow() + "  mW   \n");
			}
			else {
				System.out.print("Turbine n = " + (i+1) + "\n");
				thisStep = new float[list_turbine.get(i).getList_discretisation().size()][3];
				thisStep = list_table.get(list_turbine.size() - 1 - i);
				
				// Autant de ligne que d'éléments dans discretisationList
				for(int i2 = 0; i2 < list_turbine.get(i).getList_discretisation().size(); i2++)
				{
					// Débit max - currentDebitCumul
					if(thisStep[i2][0] == list_turbine.get(i).getList_discretisation().get(list_turbine.get(i).getList_discretisation().size() - 1) - currentDebitCumul) {
						list_turbine.get(i).setFinalStreamFlow((int) thisStep[i2][2]);
						list_turbine.get(i).setFinalPowerFlow((ChooseFunction(list_turbine.get(i).getIndex(), (int) thisStep[i2][2])));
						
						currentDebitCumul = currentDebitCumul + (int) thisStep[i2][2];
						
						System.out.print(" Débit à donner : " + list_turbine.get(i).getFinalStreamFlow() + " m3/s    \n");
						System.out.print(" Puissance récupérée pour cette turbine  : " + list_turbine.get(i).getFinalPowerFlow() + "  mW   \n");
					}
				}
			}
		}
	}
}
