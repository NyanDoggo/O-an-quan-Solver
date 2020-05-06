package main.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Player {
    private int currPts = 0;

    @JsonIgnore
    private boolean isFirstPlayer;
//    private Result gameValue;

    public Player(boolean isFirstPlayer){
        this.isFirstPlayer = isFirstPlayer;
    }

    public Player(boolean isFirstPlayer, int points){
        this.isFirstPlayer = isFirstPlayer;
        this.currPts = points;
    }

    public Player(Player player){
        this.currPts = player.currPts;
        this.isFirstPlayer = player.isFirstPlayer;
    }

    public int getCurrPts() {
        return currPts;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

//    public Result getGameValue() {
//        return gameValue;
//    }
//
//    public void setGameValue(Result gameValue) {
//        this.gameValue = gameValue;
//    }

    public void setCurrPts(int currPts) {
        this.currPts = currPts;
    }

    @Override
    public String toString() {
        return "Player{" +
                "currentPoints=" + currPts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (currPts != player.currPts) return false;
        return isFirstPlayer == player.isFirstPlayer;
    }

    @Override
    public int hashCode() {
        int result = currPts;
        result = 31 * result + (isFirstPlayer ? 1 : 0);
        return result;
    }
}
