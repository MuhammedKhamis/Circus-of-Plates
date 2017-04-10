package model.save;

public class Snapshot {

    private SavedStates game;
    private PlayersStacksData[] data;
    private final SaverIF saver;

    public Snapshot(final SavedStates game, final PlayersStacksData[] data) {
        this.game = game;
        this.data = data;
        saver = XmlSaver.getInstance();
    }

    public Snapshot() {
        saver = XmlSaver.getInstance();
    }

    public void buildGameState(final int[] scores, final int elapsedTime, final int diff, final int[] p1, final int[] p2) {
        game = new SavedStates(scores, elapsedTime, diff, p1, p2);
    }

    public void buildGameState(final SavedStates game) {
        this.game = game;
    }

    public void buildPlayer(final PlayersStacksData data[]) {
        this.data = new PlayersStacksData[data.length];
        for (int i = 0; i < data.length; i++) {
            this.data[i] = data[i];
        }
    }

    public SavedStates getGameState() {
        return game;
    }

    public PlayersStacksData getDate(final int index) {
        return data[index];
    }

    public int getDateSize() {
        return data.length;
    }

    public void saveShot(final String path, final String fileName) {
        // Saves data
        saver.save(this, path, fileName);
    }

    public void LoadDate(final String path, final String fileName) {
        // Load data
        final Snapshot load = saver.load(path, fileName);
        this.game = load.getGameState();
        this.data = new PlayersStacksData[load.getDateSize()];
        for (int i = 0; i < load.getDateSize(); i++) {
            data[i] = load.getDate(i);
        }
    }

}
