package ai.crawler;

/**
 *
 * @author arsalan
 */
public class Player {

    private final int playerCode;
    private final String playerName;

    public Player(int playerCode, String playerName) {
        this.playerCode = playerCode;
        this.playerName = playerName;
    }

    public int getPlayerCode() {
        return playerCode;
    }

    public String getPlayerName() {
        return playerName;
    }

}
