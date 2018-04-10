
public class Initialisation {

	public int index;
	public int qmin;
	public int qmax;
	
	public Initialisation(int index) {
		this.index = index;
		this.qmin = 0;
		this.qmax = 180;
	}
	
	public Initialisation(int index, int qmax) {
		this.index = index;
		this.qmin = 0;
		this.qmax = qmax;
	}
	
	public Initialisation(int index, int qmin, int qmax) {
		this.index = index;
		this.qmin = qmin;
		this.qmax = qmax;
	}
}
