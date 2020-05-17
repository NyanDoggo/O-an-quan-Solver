package main.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.pojo.Board;
import main.pojo.Player;
import main.pojo.Result;
import main.table.Table;
import main.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Solver {

    public Player firstPlayer;
    public Player secondPlayer;
    public Table table = new Table();
    private boolean isFirstPlayerTurn;


    public Solver(){
        this.firstPlayer = new Player();
        this.secondPlayer = new Player();
        this.isFirstPlayerTurn = true;
    }

    private Result firstPlayerResult(State state){
        if (state.p1.getCurrPts() > state.p2.getCurrPts()){
            return Result.WIN;
        }else if (state.p1.getCurrPts() == state.p2.getCurrPts()){
            return Result.DRAW;
        }else if (state.p1.getCurrPts() < state.p2.getCurrPts()){
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

    public void resolveGameValue(Logic logic, State endState){
        if (logic.isEnd() && endState.isEnd){
            Result firstPlayerResult = firstPlayerResult(endState);
            Result secondPlayerResult = flipResult(firstPlayerResult);
            endState.setP1v(firstPlayerResult);
            endState.setP2v(secondPlayerResult);
        }
        else if (logic.isEnd() != endState.isEnd){
            System.out.println("Sync Problem ");
        }
    }

    public ArrayList<Move> getPossibleMoves(State state){
        Logic logic = new Logic(state.logic);

        ArrayList<Move> moveList = new ArrayList<Move>();
        if (state.is1p){
            for (int i = 1; i <= 5; i++){
                int numberOfStones = logic.getBoard().tileList.get(i).a;
                if (numberOfStones > 0){
                    moveList.add(new Move("left", i));
                    moveList.add(new Move("right", i));
                }
            }
        }else {
            for (int i = 7; i <= 11; i++){
                int numberOfStones = logic.getBoard().tileList.get(i).a;
                if (numberOfStones > 0){
                    moveList.add(new Move("left", i));
                    moveList.add(new Move("right", i));
                }
            }
        }
        return moveList;
    }

    private List<State> buildLevel(State state, ArrayList<Move> moveList, Logic logic){
        List<State> children = new ArrayList<>();
        Logic copy = new Logic(logic);
        for (Move m : moveList){
            Pair<Board, Integer> pair = move(logic, m.tileIndex, m.direction);
//            Board board = pair.getFirst();
            Player p1Copy = new Player(state.p1);
            Player p2Copy = new Player(state.p2);
            if (state.is1p){
                int points = state.p1.getCurrPts() + pair.getSecond();
                p1Copy.setCurrPts(points);
            }else {
                int points = state.p2.getCurrPts() + pair.getSecond();
                p2Copy.setCurrPts(points);
            }
            State child = new State(p1Copy, p2Copy, isFirstPlayerTurn, logic.isEnd(), logic);
            child.determineStateValue();
            resolveGameValue(logic, child);
            children.add(child);
            logic = new Logic(copy);
        }
        return children;
    }

    private void sowStones(State state){
        if (state.is1p){
            for (int i = 1; i <= 5; i++){
                state.logic.board.tileList.get(i).a++;
            }
            state.p1.setCurrPts(state.p1.getCurrPts() - 5);
        }else{
            for (int i = 7; i <= 11; i++){
                state.logic.board.tileList.get(i).a++;
            }
            state.p2.setCurrPts(state.p2.getCurrPts() - 5);
        }
    }

    public List<State> getNextStates(State state){
        State copy = new State(state);
        if (copy.logic.isEnd()){
            return new ArrayList<>();
        }
        ArrayList<Move> moveList = getPossibleMoves(state);
        if (moveList.isEmpty() && !state.isEnd){
            sowStones(state);
            moveList = getPossibleMoves(state);
        }
        List<State> children = buildLevel(state, moveList, copy.logic);
        copy.children = children;
        for (State s : children){
            s.is1p = !state.is1p;
            s.pD = state.pD + 1;
        }
        return children;
    }

//    public void saveFileJson(String filePath) throws IOException {
//        try{
//            System.out.println(this.table);
//            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//            Writer writer = new FileWriter(filePath);
//            gson.toJson(this.table, writer);
//            writer.close();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    private void trackingProgress(int printFrequency){
        if (table.table.size() % printFrequency == 0){
            System.out.println("Table Size: " + table.table.size());
        }
    }

    //TODO: fix board remaining Stones
    public void alphaBeta(State state){
        int p1 = state.p1.getCurrPts();
        int p2 = state.p2.getCurrPts();
        int remainingStones = 70 - (p1 + p2);
        if (p1 + remainingStones < p2){
            state.prune();
        }
    }

    public static void saveQueue(String filePath, List<State> tmp) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(filePath), tmp);
    }

    public void solveFromRoot(State root) throws IOException {
        List<State> queue = new LinkedList<>();
        queue.add(root);
        solveBFS(queue);
    }

    //BUG: queue only has root, children are added to queue to Save, but doesnt print queue because queue to save < 1M
    public void solveBFS(List<State> queue) throws IOException {
//        List<State> queue = new LinkedList<>();
//        queue.add(root);
        List<State> queueToSave = new LinkedList<>();
        while(!queue.isEmpty() && queueToSave.size() <= 100000){
            if (queue.isEmpty()){
                System.out.println("Input queue empty, loading queueToSave");
                queue.addAll(queueToSave);
                queueToSave = new LinkedList<>();
            }
            State currState = queue.get(0);
            if (!currState.isEnd && !this.table.exists(queue.get(0)) && !currState.isPrune){
                List<State> children = getNextStates(currState);
                for (State s : children){
                    alphaBeta(s);
                }
                currState.setChildren(children);
                currState.hashChildren();
                queueToSave.addAll(children);
            }
            if (!currState.isPrune){
                table.add(currState);
                currState.hash = currState.hashCode();
            }
            queue.remove(0);
            trackingProgress(10000);
            if (queueToSave.size() >= 200000){
                System.out.println("queueToSave size > 200000, saving queueToSave");
                saveQueue("C:\\JSON output\\QueueToSave.json", queueToSave);
                System.out.println("Saving remaining input queue");
                saveQueue("C:\\JSON output\\Queue.json", queue);
                System.out.println("Saving Table");
                this.table.saveToFile("C:\\JSON output\\StateTable.json");
                return;
            }
        }
        System.out.println("Input Queue empty and queueToSave is too large, saving queueToSave with size " + queueToSave.size());
        saveQueue("C:\\JSON output\\QueueToSave.json", queueToSave);
        System.out.println("Saving Table");
        this.table.saveToFile("C:\\JSON output\\StateTable.json");
    }

    public Pair solve(State root, int count){
        if (count == 1){
            return new Pair();
        }
        List<State> children = getNextStates(root);
        root.setChildren(children);
        root.hashChildren();
        Pair<Result, Result> result = new Pair<>();
//        List<Pair<Result, Result>> childrenResult = new ArrayList<>();
        if (children.isEmpty() || root.isEnd){
//            System.out.println("empty");
            Pair<Result, Result> pair = new Pair<>();
            pair.put(root.p1v, root.p2v);
            return pair;
        }else{
            for (State child : children){
                child.pD = count;
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
