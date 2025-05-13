package ch.sofa.lodo.data.constants;

import java.util.ArrayList;
import java.util.List;

public class PerLocationPerOpponentPoints {

	private static List<PointObject> startPointList = new ArrayList<>();

	static {
		startPointList.add(new PointObject(getSequenceNr(), 50, 20));
		startPointList.add(new PointObject(getSequenceNr(), 25, 10));
		startPointList.add(new PointObject(getSequenceNr(), 17, 7));
		startPointList.add(new PointObject(getSequenceNr(), 13, 5));
		startPointList.add(new PointObject(getSequenceNr(), 10, 4));
		startPointList.add(new PointObject(getSequenceNr(), 8, 3));
		startPointList.add(new PointObject(getSequenceNr(), 7, 3));
		startPointList.add(new PointObject(getSequenceNr(), 6, 3));
		startPointList.add(new PointObject(getSequenceNr(), 6, 2));
		startPointList.add(new PointObject(getSequenceNr(), 5, 2));
		startPointList.add(new PointObject(getSequenceNr(), 5, 2));
		startPointList.add(new PointObject(getSequenceNr(), 4, 2));
		startPointList.add(new PointObject(getSequenceNr(), 4, 2));
		startPointList.add(new PointObject(getSequenceNr(), 4, 1));
		startPointList.add(new PointObject(getSequenceNr(), 3, 1));
		startPointList.add(new PointObject(getSequenceNr(), 3, 1));
		startPointList.add(new PointObject(getSequenceNr(), 3, 1));
		startPointList.add(new PointObject(getSequenceNr(), 3, 1));
		startPointList.add(new PointObject(getSequenceNr(), 3, 1));
		startPointList.add(new PointObject(getSequenceNr(), 3, 1));
	}

	private static int getSequenceNr() {
		return startPointList.size() + 1;
	}

	public static PointObject getPointObject(int gamePlayedSequenceNr) {
		if (gamePlayedSequenceNr <= startPointList.size()) {
			return startPointList.get(gamePlayedSequenceNr - 1);
		}
		return new PointObject(gamePlayedSequenceNr, 3, 1);
	}

	public static class PointObject {

		private int gameSequenceNr;
		private int winPoints;
		private int lossPoints;

		public PointObject(int gameSequenceNr, int winPoints, int lossPoints) {
			super();
			this.gameSequenceNr = gameSequenceNr;
			this.winPoints = winPoints;
			this.lossPoints = lossPoints;
		}

		public int getGameSequenceNr() {
			return gameSequenceNr;
		}

		public void setGameSequenceNr(int gameSequenceNr) {
			this.gameSequenceNr = gameSequenceNr;
		}

		public int getWinPoints() {
			return winPoints;
		}

		public void setWinPoints(int winPoints) {
			this.winPoints = winPoints;
		}

		public int getLossPoints() {
			return lossPoints;
		}

		public void setLossPoints(int lossPoints) {
			this.lossPoints = lossPoints;
		}
	}
}
