package ch.sofa.lodo.data.constants;

public class ExplorerPoints {

	public static PointObject getPointObject(int sequenceNr) {
		return new PointObject(sequenceNr, sequenceNr + 9);
	}
}
