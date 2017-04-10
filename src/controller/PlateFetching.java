package controller;

import model.players.AbstractPlayer;

public class PlateFetching {

	private final AbstractPlayer player;

	public PlateFetching(final AbstractPlayer player) {
		this.player = player;
	}

	public CheckResult CheckMe(final int x, final int y) {
		final int[][] playerPosition = player.getPlayerPosition();
		final int centerX = playerPosition[0][0];
		final int centerY = playerPosition[0][1];

		final int rightStackHeight = player.getStackHeight(0);
		final int leftStackHeight = player.getStackHeight(1);

		// Checking Right Hand...
		if (x - centerX > 110 - 15 && x - centerX < 110 + 15) { // 15
			if (y - centerY > (0 - 50 * rightStackHeight) - 5 && y - centerY < (0 - 50 * rightStackHeight) + 5) { // 5
				return new CheckResult(0, false);
			}
		}
		// Checking Left Hand.....:*
		if (centerX - x > 15 - 15 && centerX - x < 15 + 15) {
			if (y - centerY > (35 - 50 * leftStackHeight) - 5 && y - centerY < (35 - 50 * leftStackHeight) + 5) {
				return new CheckResult(1, false);
			}
		}

		return new CheckResult(-1, true);
	}

	public class CheckResult {
		private final int indexOfStack;
		private final boolean intersect;

		public CheckResult(final int index, final boolean result) {
			indexOfStack = index;
			intersect = result;
		}

		public int getIndex() {
			return indexOfStack;
		}

		public boolean getResult() {
			return intersect;
		}
	}

}
