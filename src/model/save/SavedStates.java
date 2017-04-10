package model.save;

public class SavedStates {

    private final int elapsedTime;
    private final int[] scores;
    private final int diff;

    private final int[][] pS;

    public SavedStates(final int[] scores, final int elapsedTime, final int diff, final int[] p1, final int[] p2) {
        this.elapsedTime = elapsedTime;
        this.scores = scores;
        this.diff = diff;
        pS = new int[2][];
        pS[0] = p1;
        pS[1] = p2;
    }

    public int[] getScores() {
        return scores;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public int getDiff() {
        return diff;
    }

    public int[] getP(final int index) {
        return pS[index];
    }
    public int getPsLength() {
        return pS.length;
    }

}
