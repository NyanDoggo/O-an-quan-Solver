package main.pojo;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public List<Tile> tileList = new ArrayList<>();

    public Board(){
        initBoard();
        connectTiles();
    }

    public Board(Board copy) {
        for (Tile tile : copy.tileList){
            this.tileList.add(new Tile(tile));
        }
        connectTiles();
    }

    public void initBoard(){
        int numberOfTiles = 12;
        for (int i = 0; i < numberOfTiles; i++){
            //QuanTile Index: 0 and 6
            if (i == 0 || i == 6){
                tileList.add(new Tile(true, i));
            }else{
                tileList.add(new Tile(false, i));
            }
        }
    }

    private void connectTiles(){
        tileList.get(0).previousTile = tileList.get(tileList.size() - 1);
        tileList.get(0).nextTile = tileList.get(1);
        for (int i = 1; i < tileList.size() - 1; i++){
            tileList.get(i).previousTile = tileList.get(i - 1);
            tileList.get(i).nextTile = tileList.get(i + 1);
        }
        tileList.get(tileList.size() - 1).previousTile = tileList.get(10);
        tileList.get(tileList.size() - 1).nextTile = tileList.get(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        return tileList != null ? tileList.equals(board.tileList) : board.tileList == null;
    }

    @Override
    public int hashCode() {
        return tileList != null ? tileList.hashCode() : 0;
    }

    public void printBoard(){
        for (int i = 0; i <=6 ; i++){
            System.out.print(tileList.get(i) + " ");
//            if (i == 6){
//                System.out.println("");
//                System.out.print("   ");
//            }
        }
        System.out.println("");
        System.out.print("   ");
        for (int i = tileList.size() - 1; i >= 7; i--){
            System.out.print(tileList.get(i) + " ");
        }
        System.out.println();
    }

    public void printIndex(){
        for (int i = 0; i <= 6; i++){
            System.out.print(tileList.get(i).tileIndex + " ");
        }
        System.out.println();
        System.out.print(" ");
        for (int i = tileList.size() - 1; i >= 7; i--){
            System.out.print(tileList.get(i).tileIndex + " ");
        }
    }
}
