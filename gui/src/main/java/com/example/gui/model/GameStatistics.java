package com.example.gui.model;

public class GameStatistics {
    private int win = 0;
    private int lose = 0;
    private int draw = 0;

    private int gamesPlayed = 0;

    public int getGamesPlayed() {
        return gamesPlayed;
    }
    public GameStatistics() {
        this.win = 0;
        this.draw = 0;
        this.lose = 0;
        this.gamesPlayed = 0;
    }
    public GameStatistics(int win, int draw, int lose){
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.gamesPlayed = win + draw + lose;
    }

    public void incrementWin() {
        win++;
        gamesPlayed++;
    }
    public void incrementDraw() {
        draw++;
        gamesPlayed++;
    }
    public void incrementLose() {
        lose++;
        gamesPlayed++;
    }
    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
        updateGamesPlayed();
    }
    private void updateGamesPlayed() {
        gamesPlayed = win + draw + lose;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
        updateGamesPlayed();
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
        updateGamesPlayed();
    }
}
