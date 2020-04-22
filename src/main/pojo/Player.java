package main.pojo;

public class Player {
    private int currentPoints = 0;
    private boolean isFirstPlayer;
//    private Result gameValue;

    public Player(boolean isFirstPlayer){
        this.isFirstPlayer = isFirstPlayer;
    }

    public Player(Player player){
        this.currentPoints = player.currentPoints;
        this.isFirstPlayer = player.isFirstPlayer;
    }

    public int getCurrentPoints() {
        return currentPoints;
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

    public void setCurrentPoints(int currentPoints) {
        this.currentPoints = currentPoints;
    }

    @Override
    public String toString() {
        return "Player{" +
                "currentPoints=" + currentPoints +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (currentPoints != player.currentPoints) return false;
        return isFirstPlayer == player.isFirstPlayer;
    }

    @Override
    public int hashCode() {
        int result = currentPoints;
        result = 31 * result + (isFirstPlayer ? 1 : 0);
        return result;
    }
}
