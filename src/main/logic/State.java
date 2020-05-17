package main.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.pojo.Board;
import main.pojo.Player;
import main.pojo.Result;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class State implements Serializable {
    public Board board;
    @JsonIgnore
    public Logic logic;
    public int qc;
    public int hash;
    public int rV; //realValue
    public int v;//value
    public Player p1;
    public Player p2;
    public boolean is1p;
    public boolean isEnd;
    public Result p1v;
    public Result p2v;
    public int pD; //plyDepth
    public List<Integer> cH; //childrenHash
    public boolean isPrune;
    @JsonIgnore
    public List<State> children;
    
    public void hashChildren(){
        for (State s : this.children){
            this.cH.add(s.hashCode());
            s.hash = s.hashCode();
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
        this.board = this.logic.board;
        this.p1 = new Player();
        this.p2 = new Player();
        this.is1p = true;
        this.isEnd = false;
        this.isPrune = false;
        this.v = 0;
        this.rV = 0;
        this.hash = this.hashCode();
        this.children = new ArrayList<>();
        this.cH = new ArrayList<>();
        this.pD = 0;
        this.qc = this.logic.capturedQuanCounter;
    }



    State(Player p1, Player p2, boolean is1p, boolean isEnd, Logic logic){
        this.p1 = p1;
        this.p2 = p2;
        this.is1p = is1p;
        this.isEnd = isEnd;
        children = new ArrayList<>();
        cH = new ArrayList<>();
        this.logic = logic;
        this.board = logic.board;
        this.qc = logic.capturedQuanCounter;
    }

    State(State state){
        this.p1 = new Player(state.p1);
        this.p2 = new Player(state.p2);
        this.is1p = state.is1p;
        this.isEnd = state.isEnd;
        this.p1v = state.p1v;
        this.p2v = state.p2v;
        this.hash = state.hash;
        this.isPrune = state.isPrune;
        this.rV = state.rV;
        this.v = state.v;
        this.children = state.children;
        this.cH = state.cH;
        this.logic = new Logic(state.board, state.qc);
        this.board = logic.board;
        this.qc = this.logic.capturedQuanCounter;
    }

    public void prune(){
        this.isPrune = true;
    }

    public void setP1v(Result result){
        this.p1v = result;
    }

    public void setP2v(Result p2v) {
        this.p2v = p2v;
    }

    public void printState(){
        System.out.println();
        System.out.println("isFirstPlayerTurn: " + this.is1p);
        System.out.println("plyDepth: " + this.pD);
    }

    public void setChildren(List<State> children){
        this.children = children;

    }

    public void determineStateValue(){
        this.rV = this.p1.getCurrPts() - this.p2.getCurrPts();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;

        State state = (State) o;

        if (is1p != state.is1p) return false;
        if (isEnd != state.isEnd) return false;
        if (pD != state.pD) return false;
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
        result = 31 * result + (p1 != null ? p1.hashCode() : 0);
        result = 31 * result + (p2 != null ? p2.hashCode() : 0);
        result = 31 * result + (is1p ? 1 : 0);
        result = 31 * result + (isEnd ? 1 : 0);
        return result;
    }

    //    @Override
//    public int hashCode() {
//        int result = board != null ? board.hashCode() : 0;
//        result = 31 * result + rV;
//        result = 31 * result + v;
//        result = 31 * result + (p1 != null ? p1.hashCode() : 0);
//        result = 31 * result + (p2 != null ? p2.hashCode() : 0);
//        result = 31 * result + (is1p ? 1 : 0);
//        result = 31 * result + (isEnd ? 1 : 0);
//        result = 31 * result + (p1v != null ? p1v.hashCode() : 0);
//        result = 31 * result + (p2v != null ? p2v.hashCode() : 0);
//        result = 31 * result + pD;
//        result = 31 * result + (isPrune ? 1 : 0);
//        return result;
//    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("State{");
        sb.append("board=").append(board);
        sb.append(", hash=").append(hash);
        sb.append(", rV=").append(rV);
        sb.append(", v=").append(v);
        sb.append(", p1=").append(p1);
        sb.append(", p2=").append(p2);
        sb.append(", is1p=").append(is1p);
        sb.append(", isEnd=").append(isEnd);
        sb.append(", p1v=").append(p1v);
        sb.append(", p2v=").append(p2v);
        sb.append(", pD=").append(pD);
        sb.append(", cH=").append(cH);
        sb.append(", isPrune=").append(isPrune);
        sb.append('}');
        return sb.toString();
    }

    //    @Override
//    public String toString() {
//        return "State{" +
//                "board=" + logic.board +
//                ", hash=" + hash +
//                ", firstPlayer=" + p1 +
//                ", secondPlayer=" + p2 +
//                ", isFirstPlayerTurn=" + is1p +
//                ", isEnd=" + isEnd +
//                ", gameValueForFirstPlayer=" + p1v +
//                ", gameValueForSecondPlayer=" + p2v +
//                ", trueValue=" + rV +
//                ", plyDepth=" + pD +
////                ", children=" + children +
//                ", hashChildren" + cH +
//                '}';
//    }
}
