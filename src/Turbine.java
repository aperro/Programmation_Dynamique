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
	
	// Liste des débits possible pour cette turbine - 5 par 5 (0...5...10...15...)
	private List<Integer> list_discretisation= new ArrayList<Integer>();
	
	
	public Turbine(int index, float maxStreamFlowTurbine, int qTurbine) {
		this.index = index;
		minStreamFlowTurbine = 0;
		this.maxStreamFlowTurbine = maxStreamFlowTurbine;

		Discretisation(qTurbine);
	}
	
	public Turbine(int index, int qTurbine) {
		this.index = index;
		minStreamFlowTurbine = 0;
		maxStreamFlowTurbine = 180;
		
		// Regarder si qTurbine <= maxStreamFlowTurbine
		/*if(qTurbine > maxStreamFlowTurbine) {
		Discretisation();
		}
		else {*/
			
		Discretisation(qTurbine);
	}
	
	public void Discretisation(int qTurbine) {
		// Discrétisation [0; 5; 10; ....; n]
		for(int i = 0; i <= qTurbine/5; i++) {
			list_discretisation.add(i*5);
		}
	}
	
	public void Discretisation() {
		// Discrétisation [0; 5; 10; ....; n]
		for(int i = 0; i <= maxStreamFlowTurbine/5; i++) {
			list_discretisation.add(i*5);
		}
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
