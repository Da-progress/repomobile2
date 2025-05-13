package ch.sofa.lodo.data.constants;

public class PointObject {

	private int sequenceNr;
	private int points;

	public PointObject(int sequenceNr, int points) {
		super();
		this.sequenceNr = sequenceNr;
		this.points = points;
	}

	public int getSequenceNr() {
		return sequenceNr;
	}

	public void setSequenceNr(int sequenceNr) {
		this.sequenceNr = sequenceNr;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}