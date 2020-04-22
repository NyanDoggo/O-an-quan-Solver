package main.logic;

import main.pojo.Board;
import main.pojo.Player;
import main.pojo.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class State {
    Board board;
    public Logic logic;
    Player firstPlayer;
    Player secondPlayer;
    boolean isFirstPlayerTurn;
    boolean isEnd;
    Result gameValueForFirstPlayer;
    Result gameValueForSecondPlayer;
    int plyDepth;

    public List<State> children;

    public State(){

    }

    public State(boolean initRoot){
        if (initRoot){
            initRoot();
        }
    }

    private void initRoot(){
        this.logic = new Logic();
        this.board = logic.board;
        this.firstPlayer = new Player(true);
        this.secondPlayer = new Player(false);
        this.isFirstPlayerTurn = true;
        this.isEnd = false;
        this.children = new ArrayList<>();
        this.plyDepth = 0;
    }

    State(Board board, Player firstPlayer, Player secondPlayer, boolean isFirstPlayerTurn, boolean isEnd, Logic logic){
        this.board = board;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.isFirstPlayerTurn = isFirstPlayerTurn;
        this.isEnd = isEnd;
        children = new ArrayList<>();
        this.logic = logic;
    }

    State(State state){
        this.board = new Board(state.board);
        this.firstPlayer = new Player(state.firstPlayer);
        this.secondPlayer = new Player(state.secondPlayer);
        this.isFirstPlayerTurn = state.isFirstPlayerTurn;
        this.isEnd = state.isEnd;
        this.gameValueForFirstPlayer = state.gameValueForFirstPlayer;
        this.gameValueForSecondPlayer = state.gameValueForSecondPlayer;
        this.children = state.children;
        this.logic = new Logic(state.logic);
    }

    public void setGameValueForFirstPlayer(Result result){
        this.gameValueForFirstPlayer = result;
    }

    public void setGameValueForSecondPlayer(Result gameValueForSecondPlayer) {
        this.gameValueForSecondPlayer = gameValueForSecondPlayer;
    }

    public void printState(){
        this.board.printBoard();
        System.out.println();
        System.out.println("isFirstPlayerTurn: " + this.isFirstPlayerTurn);
        System.out.println("plyDepth: " + this.plyDepth);
    }

    public void setChildren(List<State> children){
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;

        State state = (State) o;

        if (isFirstPlayerTurn != state.isFirstPlayerTurn) return false;
        if (isEnd != state.isEnd) return false;
        if (plyDepth != state.plyDepth) return false;
        if (!Objects.equals(board, state.board)) return false;
        if (!Objects.equals(logic, state.logic)) return false;
        if (!Objects.equals(firstPlayer, state.firstPlayer)) return false;
        if (!Objects.equals(secondPlayer, state.secondPlayer)) return false;
        if (gameValueForFirstPlayer != state.gameValueForFirstPlayer) return false;
        if (gameValueForSecondPlayer != state.gameValueForSecondPlayer) return false;
        return Objects.equals(children, state.children);
    }

    @Override
    public int hashCode() {
        int result = board != null ? board.hashCode() : 0;
        result = 31 * result + (logic != null ? logic.hashCode() : 0);
        result = 31 * result + (firstPlayer != null ? firstPlayer.hashCode() : 0);
        result = 31 * result + (secondPlayer != null ? secondPlayer.hashCode() : 0);
        result = 31 * result + (isFirstPlayerTurn ? 1 : 0);
        result = 31 * result + (isEnd ? 1 : 0);
        result = 31 * result + (gameValueForFirstPlayer != null ? gameValueForFirstPlayer.hashCode() : 0);
        result = 31 * result + (gameValueForSecondPlayer != null ? gameValueForSecondPlayer.hashCode() : 0);
        result = 31 * result + plyDepth;
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "State{" +
                "board=" + board +
                ", isFirstPlayerTurn=" + isFirstPlayerTurn +
                ", isEnd=" + isEnd +
                ", gameValueForFirstPlayer=" + gameValueForFirstPlayer +
                ", gameValueForSecondPlayer=" + gameValueForSecondPlayer +
                ", plyDepth=" + plyDepth +
                '}';
    }
}
