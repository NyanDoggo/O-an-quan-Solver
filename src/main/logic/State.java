package main.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.pojo.Board;
import main.pojo.Player;
import main.pojo.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class State {
    public Board board;
    Logic logic;
    public int hash;
    public Player p1;
    public Player p2;
    public boolean is1p ;
    public boolean isEnd;
    public Result p1v;
    public Result p2v;
    public int plyDepth;
    public List<Integer> childrenHash;
    @JsonIgnore
    public List<State> children;



    public void hashChildren(){
        for (State s : this.children){
            this.childrenHash.add(s.hashCode());
        }
    }

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
        this.p1 = new Player(true);
        this.p2 = new Player(false);
        this.is1p = true;
        this.isEnd = false;
        this.children = new ArrayList<>();
        this.childrenHash = new ArrayList<>();
        this.plyDepth = 0;
    }

    State(Board board, Player p1, Player p2, boolean is1p, boolean isEnd, Logic logic){
        this.board = board;
        this.p1 = p1;
        this.p2 = p2;
        this.is1p = is1p;
        this.isEnd = isEnd;
        children = new ArrayList<>();
        childrenHash = new ArrayList<>();
        this.logic = logic;
    }

    State(State state){
        this.board = new Board(state.board);
        this.p1 = new Player(state.p1);
        this.p2 = new Player(state.p2);
        this.is1p = state.is1p;
        this.isEnd = state.isEnd;
        this.p1v = state.p1v;
        this.p2v = state.p2v;
        this.children = state.children;
        this.childrenHash = state.childrenHash;
        this.logic = new Logic(state.logic);
    }

    public void setP1v(Result result){
        this.p1v = result;
    }

    public void setP2v(Result p2v) {
        this.p2v = p2v;
    }

    public void printState(){
        this.board.printBoard();
        System.out.println();
        System.out.println("isFirstPlayerTurn: " + this.is1p);
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

        if (is1p != state.is1p) return false;
        if (isEnd != state.isEnd) return false;
        if (plyDepth != state.plyDepth) return false;
        if (!Objects.equals(board, state.board)) return false;
        if (!Objects.equals(logic, state.logic)) return false;
        if (!Objects.equals(p1, state.p1)) return false;
        if (!Objects.equals(p2, state.p2)) return false;
        if (p1v != state.p1v) return false;
        if (p2v != state.p2v) return false;
        return Objects.equals(children, state.children);
    }

    @Override
    public int hashCode() {
        int result = board != null ? board.hashCode() : 0;
        result = 31 * result + (logic != null ? logic.hashCode() : 0);
        result = 31 * result + (p1 != null ? p1.hashCode() : 0);
        result = 31 * result + (p2 != null ? p2.hashCode() : 0);
        result = 31 * result + (is1p ? 1 : 0);
        result = 31 * result + (isEnd ? 1 : 0);
        result = 31 * result + (p1v != null ? p1v.hashCode() : 0);
        result = 31 * result + (p2v != null ? p2v.hashCode() : 0);
        result = 31 * result + plyDepth;
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "State{" +
                "board=" + board +
                ", hash=" + hash +
                ", firstPlayer=" + p1 +
                ", secondPlayer=" + p2 +
                ", isFirstPlayerTurn=" + is1p +
                ", isEnd=" + isEnd +
                ", gameValueForFirstPlayer=" + p1v +
                ", gameValueForSecondPlayer=" + p2v +
                ", plyDepth=" + plyDepth +
//                ", children=" + children +
                ", hashChildren" + childrenHash +
                '}';
    }
}
