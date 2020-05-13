package main.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.pojo.Board;
import main.pojo.Tile;

import java.util.Objects;

public class Logic {
    public Board board;
    final int quanPoint = 5;
    int capturedQuanCounter = 0;

    public Logic(Board board, int capturedQuanCounter){
        this.board = new Board(board);
        this.capturedQuanCounter = capturedQuanCounter;
    }

    public Logic(Logic copy){
        this.board = new Board(copy.board);
        this.capturedQuanCounter = copy.capturedQuanCounter;
    }

    public Logic(){
        this.board = new Board();
    }

    public boolean isEnd(){
        return capturedQuanCounter == 2;
    }

    public int makeMove(int tileIndex, String direction){
        int acquiredPoints = 0;
        Tile tmp = board.tileList.get(tileIndex);
        int numberOfPickedStones = tmp.a;
        tmp.a = 0;
        if (direction.equalsIgnoreCase("left")){
           int haltIndex = passStone(tmp.previousTile, direction, numberOfPickedStones);
           Tile haltTile = board.tileList.get(haltIndex);
            //if stop at Quan, return no points and halt the method
            if (haltIndex == 0 || haltIndex == 6){
                return 0;
            }
            //if next tiles from haltIndex has 0 stones, return no points and halt the method
            else if (haltTile.a == 0 && haltTile.previousTile.a == 0){
                return 0;
            }
            // if next tiles from haltIndex has stones, return that amount of points and halt the method
            else if (haltTile.a == 0 && haltTile.previousTile.a != 0){
                while (haltTile.a == 0 && haltTile.previousTile.a != 0){
                    if (haltTile.previousTile.isQuan){
                        acquiredPoints += haltTile.previousTile.a + quanPoint;
                        this.capturedQuanCounter++;
                        haltTile.previousTile.clearTile();
                    }else{
                        acquiredPoints += haltTile.previousTile.a;
                        haltTile.previousTile.clearTile();
                    }
                    haltTile = haltTile.previousTile.previousTile;
                }
                return acquiredPoints;
            }
            acquiredPoints = makeMove(haltIndex, direction);
        }else if (direction.equalsIgnoreCase("right")){
           int haltIndex = passStone(tmp.nextTile, direction, numberOfPickedStones);
           Tile haltTile = board.tileList.get(haltIndex);
           //if next tiles from haltIndex has 0 stones, return no points and halt the method
            if (haltTile.a == 0 && haltTile.nextTile.a == 0){
               return 0;
           }
           // if next tiles from haltIndex has stones, return that amount of points, check if next tile is empty and
           // the one after that has stones and halt the method

           else if (haltTile.a == 0 && haltTile.nextTile.a != 0){
               while (haltTile.a == 0 && haltTile.nextTile.a != 0){
                   if (haltTile.nextTile.isQuan){
                       acquiredPoints += haltTile.nextTile.a + quanPoint;
                       this.capturedQuanCounter++;
                       haltTile.nextTile.clearTile();
                   }else{
                       acquiredPoints += haltTile.nextTile.a;
                       haltTile.nextTile.clearTile();
                   }
                    haltTile = haltTile.nextTile.nextTile;
               }
               return acquiredPoints;
           }//if stop before Quan, return no points and halt the method
            else if ((haltIndex == 0 && haltTile.a != 0)|| (haltIndex == 6 && haltTile.a != 0)){
                return 0;
            }
           acquiredPoints = makeMove(haltIndex, direction);
        }
        return acquiredPoints;
    }

    private int passStone(Tile tile, String direction, int currentNumberOfPickedStones){
        int returnIndex = -1;
        if (currentNumberOfPickedStones == 0){
            returnIndex = tile.getTileIndex();
            return returnIndex;
        }
        if (direction.equalsIgnoreCase("left")){
            tile.a += 1;
            returnIndex = passStone(tile.previousTile, "left",currentNumberOfPickedStones - 1);
        }

        if (direction.equalsIgnoreCase("right")){
            tile.a += 1;
            returnIndex = passStone(tile.nextTile, "right",currentNumberOfPickedStones - 1);
        }
        return returnIndex;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Logic{");
        sb.append("board=").append(board);
        sb.append(", capturedQuanCounter=").append(capturedQuanCounter);
        sb.append('}');
        return sb.toString();
    }

    public Board getBoard(){
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Logic)) return false;

        Logic logic = (Logic) o;

        if (capturedQuanCounter != logic.capturedQuanCounter) return false;
        return (!Objects.equals(board, logic.board)) ;
    }

    @Override
    public int hashCode() {
        int result = board != null ? board.hashCode() : 0;
        result = 31 * result + capturedQuanCounter;
        return result;
    }
}
