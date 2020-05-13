package main.pojo;

public class Player {
    private int currPts = 0;

    public Player(){
        super();
    }

    public Player(Player player){
        this.currPts = player.currPts;
    }

    public int getCurrPts() {
        return currPts;
    }

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

        return currPts == player.currPts;
    }

    @Override
    public int hashCode() {
        return currPts;
    }
}
