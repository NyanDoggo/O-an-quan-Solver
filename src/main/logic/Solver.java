package main.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.pojo.Board;
import main.pojo.Player;
import main.pojo.Result;
import main.table.Table;
import main.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Solver {

    public Player firstPlayer;
    public Player secondPlayer;
    public Table table = new Table();
    private boolean isFirstPlayerTurn;


    public Solver(){
        this.firstPlayer = new Player(true);
        this.secondPlayer = new Player(false);
        this.isFirstPlayerTurn = true;
    }

    public ArrayList<Move> getPossibleMoves(Logic logic){
        ArrayList<Move> moveList = new ArrayList<Move>();
        for (int i = 1; i < logic.getBoard().tileList.size(); i++){
            int numberOfStones = logic.getBoard().tileList.get(i).currentNumberOfStones;
            if (numberOfStones > 0 && i != 6){
                moveList.add(new Move("left", i));
                moveList.add(new Move("right", i));
            }
        }
        return moveList;
    }

    private Result firstPlayerResult(){
        if (this.firstPlayer.getCurrentPoints() > this.secondPlayer.getCurrentPoints()){
            return Result.WIN;
        }else if (this.firstPlayer.getCurrentPoints() == this.secondPlayer.getCurrentPoints()){
            return Result.DRAW;
        }else if (this.firstPlayer.getCurrentPoints() < this.secondPlayer.getCurrentPoints()){
            return Result.LOSE;
        }else{
            System.out.println("Bug in end game result");
            return Result.LOSE;
        }
    }

    private Result flipResult(Result result){
        if (result == Result.WIN){
            return Result.LOSE;
        }else if (result == Result.LOSE){
            return Result.WIN;
        }else{
            return Result.DRAW;
        }
    }

    private Pair move(Logic copy ,int index, String direction){
        int points = copy.makeMove(index,direction);
        Pair<Board, Integer> tuple = new Pair<>();
        tuple.put(copy.board, points);
        return tuple;
    }

    private void setPlayerPoint(int points){
        if (this.isFirstPlayerTurn){
            this.firstPlayer.setCurrentPoints(points);
        }else{
            this.secondPlayer.setCurrentPoints(points);
        }
    }

    public void resolveGameValue(Logic logic, State endState){
        if (logic.isEnd() && endState.isEnd){
            Result firstPlayerResult = firstPlayerResult();
            Result secondPlayerResult = flipResult(firstPlayerResult);
            endState.setGameValueForFirstPlayer(firstPlayerResult);
            endState.setGameValueForSecondPlayer(secondPlayerResult);
        }
        else if (logic.isEnd() != endState.isEnd){
            System.out.println("Sync Problem ");
        }
    }

    private List<State> buildLevel(ArrayList<Move> moveList, Logic logic){
        List<State> children = new ArrayList<>();
        Logic copy = new Logic(logic);
        for (Move m : moveList){
            Pair<Board, Integer> pair = move(logic, m.tileIndex, m.direction);
            Board board = pair.getFirst();
            setPlayerPoint(pair.getSecond());
            State child = new State(board, firstPlayer, secondPlayer, isFirstPlayerTurn, logic.isEnd(), logic);
            resolveGameValue(logic, child);
            children.add(child);
            logic = new Logic(copy);
        }
        return children;
    }

    public List<State> getNextStates(State state){
        State copy = new State(state);
        if (copy.logic.isEnd()){
            return new ArrayList<>();
        }
        ArrayList<Move> moveList = getPossibleMoves(copy.logic);
        List<State> children = buildLevel(moveList, copy.logic);
        copy.children = children;
        this.isFirstPlayerTurn = !isFirstPlayerTurn;
        for (State s : children){
            s.isFirstPlayerTurn = this.isFirstPlayerTurn;
        }
        return children;
    }

    private void printFirstLiniage(State root){
        System.out.println("Printing Lineage:");
        root.printState();
        if (!root.children.isEmpty()){
            printFirstLiniage(root.children.get(0));
        }
    }

    public void saveFileJson(String filePath) throws IOException {
        try{
            System.out.println(this.table);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            Writer writer = new FileWriter(filePath);
            gson.toJson(this.table, writer);
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Pair solve(State root, int count){
        if (count == 1){
//            printFirstLiniage(root);
//            System.out.println("fak");
            return new Pair();
        }
        List<State> children = getNextStates(root);
        root.setChildren(children);
        Pair<Result, Result> result = new Pair<>();
//        List<Pair<Result, Result>> childrenResult = new ArrayList<>();
        if (children.isEmpty() || root.isEnd){
//            System.out.println("empty");
            Pair<Result, Result> pair = new Pair<>();
            pair.put(root.gameValueForFirstPlayer, root.gameValueForSecondPlayer);
            return pair;
        }else{
            for (State child : children){
                child.plyDepth = count;
//                child.printState();
                if (table.exists(child)){
                    break;
                }else{
                    table.add(child);
                    result = solve(child, count + 1);
//                    childrenResult.add(result);
                }
            }
            return result;
        }
    }

}
