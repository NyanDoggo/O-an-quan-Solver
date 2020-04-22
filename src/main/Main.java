package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.logic.Logic;
import main.logic.Solver;
import main.logic.State;
import main.pojo.Board;
import main.table.Table;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
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

    public static void main(String[] args) throws IOException {
//        testFirstLevel();

        Solver solver = new Solver();
        State root = new State(true);
        solver.table.add(root);

        solver.solve(root, 0);
        System.out.println(solver.table.getTable().size());
//        solver.table.saveToFile("C:\\JSON output\\StateTable.json");
        solver.saveFileJson("C:\\JSON output\\StateTable.json");
//        solver.table.saveToFile("C:\\JSON output\\StateTable.json", solver.table);
        System.out.println("------------------------------------");

//        printChildren(root.children.get(0).children.get(0));

        Table readTest = readFromJson("C:\\JSON output\\StateTable.json");

        System.out.println(readTest);

//        solver.table.getTable().entrySet().forEach(entry -> {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        });


//        solver.saveTable("C:\\JSON output\\StateTable.json");
//        printChildren(root);

//        List<State> tmp = solver.getNextStates(root);
//        for (State s : tmp){
//            s.printState();
//        }
//        System.out.println("root");
//        root.printState();
//
//        System.out.println("_______________________________---");

//        printChildren(root);



//        testMakeMove(2, "left");
//
//        List<Move> moveList = solver.getPossibleMoves();
//
//        ArrayList<Board> tmp = new ArrayList<>();

//        for (Move m : moveList){
//            tmp.add(move(m.tileIndex, m.direction));
//        }
//
//        for (Board b : tmp){
//            System.out.println("Compare");
//            b.printBoard();
//        }

//        solver.logic.makeMove(moveList.get(1).tileIndex, moveList.get(1).direction);

//        List<State> tmp = solver.getNextStates();
//        printList(tmp);

    }
}
