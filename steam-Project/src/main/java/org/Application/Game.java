package org.Application;

public class Game {
    private int gameId;
    private String gameName;

    public Game() {}

    public Game(int gameId, String gameName) {
        this.gameId = gameId;
        this.gameName = gameName;
    }

    public int getgameId() { return gameId; }
    public void setgameId(int gameId) { this.gameId = gameId; }
    public String getgameName() { return gameName; }
    public void setgameName(String gameName) { this.gameName = gameName; }

    public void addGame() {
        new GameDB(this).insert(DataBaseConnection.getConnection());
    }



}
