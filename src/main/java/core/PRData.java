package core;

public class PRData {

	private double precision;
	private double recall;

	PRData(double p, double r){
		precision = p;
		recall = r;
	}


	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}



}
