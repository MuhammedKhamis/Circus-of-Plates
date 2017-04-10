package controller;

import controller.util.Enumrations.Players;
import model.players.util.Observer;

public class ScoreManager implements Observer{

    private static ScoreManager manager;
    private static final int noOfPlayers = 2;

    private final int[] scores;

    private ScoreManager(){
        scores = new int[noOfPlayers];
        for(int i = 0 ; i < noOfPlayers ; i++){
            scores[i] = 0;
        }
    }

    public void updateScore(final Players type){
        scores[type.ordinal()]++;
    }

    public int getScore(final Players type){
        return scores[type.ordinal()];
    }

    public static ScoreManager getInstance(){
        if(manager == null){
            manager = new ScoreManager();
        }
        return manager;
    }

	@Override
	public void update(final int player) {
		scores[player]++;
	}

}
