import java.util.ArrayList;
import java.util.List;

public class Turbine {
	/*
	 * Débit minimum par turbine = 0 m3/s
	 * Débit maximum par turbine = 180 m3/s ou moins
	 */
	private int index;
	private float minStreamFlowTurbine;
	private float maxStreamFlowTurbine;
	private int finalStreamFlow;
	private float finalPowerFlow;
	private int sommeDiscretisation;

	private float[] firstLine;
	private float[][] temporyTable;
	private float[][] finalTable;
	
	
	public float[] getFirstLine() {
		return firstLine;
	}

	public void setFirstLine(float[] firstLine) {
		this.firstLine = firstLine;
	}

	// Liste des débits possible pour cette turbine - 5 par 5 (0...5...10...15...)
	private List<Integer> list_discretisation= new ArrayList<Integer>();


	public Turbine(int index, int min, int max, int sommeDiscretisation, Boolean isFirstTurbine, float Qturbine) {
		this.index = index;
		minStreamFlowTurbine = 0;
		this.maxStreamFlowTurbine = max;
		this.minStreamFlowTurbine = min;
		this.sommeDiscretisation = sommeDiscretisation;

		if(!isFirstTurbine) {

			// On crée un fucking tableau pour chaque turbine
			this.temporyTable = new float[(int) Math.ceil(sommeDiscretisation/5.0)+1][ (int) Math.ceil((sommeDiscretisation)/5.0) + 2];
			this.finalTable = new float[(int) Math.ceil(sommeDiscretisation/5.0)+1][3];
			
			firstLine = new float[(int) Math.ceil((sommeDiscretisation)/5.0) + 1];
			FirstLine();
			FirstColomn();
			Initialisation();
			//ShowTemporyTable();
		}else {
			// On crée un fucking tableau pour chaque turbine
			this.temporyTable = new float[1][(int) Math.ceil((max)/5.0) + 2];
			this.finalTable = new float[1][3];
			
			firstLine = new float[(int) Math.ceil((max)/5.0) + 1];			
			this.temporyTable[0][0] = Qturbine;
			this.finalTable[0][0] = Qturbine;
			
			FirstLine();
			//ShowTemporyTable();
		}
	}
	
	public void changeFirstTurbineQturbine(float qTurbine) {
		this.temporyTable[0][0] = qTurbine;
		this.finalTable[0][0] = qTurbine;
		
		FirstLine();
	}

	public int getSommeDiscretisation() {
		return sommeDiscretisation;
	}

	public void setSommeDiscretisation(int sommeDiscretisation) {
		this.sommeDiscretisation = sommeDiscretisation;
	}

	private void FirstColomn() {
		// Discrétisation [0; 5; 10; ....; n]

		for(int i = 0; i < temporyTable.length; i++) {
			temporyTable[i][0] = i*5;
			finalTable[i][0] = i*5;
		}
	}
	
	private void FirstLine() {
		// Discrétisation [0; 5; 10; ....; n]

		for(int i = 0; i < temporyTable[0].length-1; i++) {
			firstLine[i] = i*5;
			//System.out.print(firstLine[i] + " ");
		}
		//System.out.print(" \n ");
	}

	private void Initialisation() {

		for(int i = 0; i < temporyTable.length; i++) {
			for(int j = 0; j < temporyTable[0].length; j++) {
				if(j != 0 && ((j > i + 1) || (j > maxStreamFlowTurbine/5.0 +1) || ( i - j + 1 > sommeDiscretisation/5.0 - maxStreamFlowTurbine/5.0))) {
					temporyTable[i][j] = -100000;
					
				}
			}
		}
	}

	public void ShowTemporyTable() {
		for(int i = 0; i < temporyTable.length; i++) {
			for(int j = 0; j < temporyTable[0].length; j++) {
				System.out.print(temporyTable[i][j] + " ");
			}
			System.out.print("\n");
		}
	}
	
	public void ShowFinalTable() {
		for(int i = 0; i < finalTable.length; i++) {
			System.out.print(finalTable[i][0] + " m3/s    " + finalTable[i][1] + " mW     " + finalTable[i][2] + " m3/s ");
			System.out.print("\n");
		}
	}

	public float getMinStreamFlowTurbine() {
		return minStreamFlowTurbine;
	}

	public void setMinStreamFlowTurbine(float minStreamFlowTurbine) {
		this.minStreamFlowTurbine = minStreamFlowTurbine;
	}

	public float[][] getTemporyTable() {
		return temporyTable;
	}

	public void setTemporyTable(float[][] temporyTable) {
		this.temporyTable = temporyTable;
	}

	public float[][] getFinalTable() {
		return finalTable;
	}

	public void setFinalTable(float[][] finalTable) {
		this.finalTable = finalTable;
	}

	public List<Integer> getList_discretisation() {
		return list_discretisation;
	}

	public void setList_discretisation(List<Integer> list_discretisation) {
		this.list_discretisation = list_discretisation;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public float getMaxStreamFlowTurbine() {
		return maxStreamFlowTurbine;
	}

	public void setMaxStreamFlowTurbine(float maxStreamFlowTurbine) {
		this.maxStreamFlowTurbine = maxStreamFlowTurbine;
	}

	public int getFinalStreamFlow() {
		return finalStreamFlow;
	}

	public void setFinalStreamFlow(int finalStreamFlow) {
		this.finalStreamFlow = finalStreamFlow;
	}

	public float getFinalPowerFlow() {
		return finalPowerFlow;
	}

	public void setFinalPowerFlow(float f) {
		this.finalPowerFlow = f;
	}

}
