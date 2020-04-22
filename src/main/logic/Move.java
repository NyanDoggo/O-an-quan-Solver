package main.logic;

public class Move {
    public String direction;
    public int tileIndex;

    public Move(String direction, int tileIndex){
        this.direction = direction;
        this.tileIndex = tileIndex;
    }

    @Override
    public String toString() {
        return "Move{" +
                "direction='" + direction + '\'' +
                ", tileIndex=" + tileIndex +
                '}';
    }
}
