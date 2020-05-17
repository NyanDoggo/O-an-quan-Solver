package main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.logic.Logic;
import main.logic.LogicData;
import main.logic.Solver;
import main.logic.State;
import main.pojo.Board;
import main.table.Table;
import main.util.TableSerializable;

import java.io.*;
import java.util.List;

public class Main {
    //TODO: Search the game tree

    public static void testMakeMove(int index, String direction){
        Logic logic = new Logic();
        logic.makeMove(index,direction);
        logic.board.printBoard();
    }

    public static Board move(int index, String direction){
        Logic logic = new Logic();
        logic.makeMove(index,direction);
        return logic.board;
    }

    public static void printList(List<State> tmp){
        for (State o : tmp){
            o.printState();
        }
    }

    public static void testTileEquals(){
        Board tmp = new Board();
        System.out.println(tmp.tileList.get(3).hashCode() == (tmp.tileList.get(9).hashCode()));
    }

    public static void testTileCopy(){
        Board board = new Board();
        Logic tmp = new Logic(board, 0);
        tmp.board.printBoard();
        tmp.makeMove(2, "right");
        System.out.println("logic board");
        tmp.board.printBoard();
        System.out.println("Board");
        board.printBoard();
        Board copy = new Board(tmp.board);

        System.out.println("Copy");
        copy.printBoard();
    }

    public static void testFirstLevel(){
        Solver solver = new Solver();
        List<State> tmp = solver.getNextStates(new State(true));
        System.out.println("----------------------------");
//        printList(tmp);
        List<State> firstGrandChildren = solver.getNextStates(tmp.get(0));
        System.out.println("----------------------------");
        System.out.println(tmp.get(0).children.isEmpty());
        printList(tmp.get(0).children);
        System.out.println("----------------------------");
        printList(firstGrandChildren);
//        System.out.println("Children Size: " + tmp.size());
    }

    public static void printChildren(State root){
        for (State s : root.children){
            s.printState();
        }
    }

    public static Table readFromJson(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        return mapper.readValue(br, Table.class);
    }

    public static void saveToFile(String filePath, State tmp) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(filePath), tmp);
    }

    public static void stateReinit(State state){
        state.board.tileList.get(0).isQuan = true;
        state.board.tileList.get(6).isQuan = true;
        state.logic = new Logic(state.board, state.qc);
    }

    public static void main(String[] args) throws IOException {
//        testFirstLevel();

        Solver solver = new Solver();
        State root = new State(true);
//        solver.table.saveToFile("C:\\JSON output\\StateTable.json");
        List<State> queueRead = new ObjectMapper().readValue(new File("C:\\JSON output\\Queue.json"), new TypeReference<List<State>>(){});

        for (State s : queueRead){
            stateReinit(s);
        }

        solver.solveBFS(queueRead);
//        saveToFile("C:\\JSON output\\State.json", root);

//        State load = new ObjectMapper().readValue(new File("C:\\JSON output\\State.json"), State.class);
//        System.out.println(load);

        System.out.println("------------------------------------");
    }
}
