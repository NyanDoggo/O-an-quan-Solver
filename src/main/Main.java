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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

    public static void combineTable() throws IOException {
        Solver solver = new Solver();
        int size = 0;
        for (int i = 1; i <= 8; i++){
                String filePath = "C:\\JSON output\\StateTable" + i + ".json";
                Table table = new ObjectMapper().readValue(new File(filePath), Table.class);
                System.out.println(table.table.size());
                size += table.table.size();
                solver.table.table.putAll(table.table);
        }
        System.out.println("Total size: " + size);
        System.out.println("Final Table Size: " + solver.table.table.size());
        solver.table.saveToFile("C:\\JSON output\\CombinedTable.json");
    }

    public static void solveWrapper() throws IOException {
        Solver solver = new Solver();

        List<State> queueRead = new ObjectMapper().readValue(new File("C:\\JSON output\\Queue.json"), new TypeReference<List<State>>(){});
//        Table table = new ObjectMapper().readValue(new File("C:\\JSON output\\StateTable.json"), Table.class);
        for (State s : queueRead){
            stateReinit(s);
        }
        solver.solveBFS(queueRead, new Table());
    }

    public static void fillChildrenHash(Table table){
        Solver solver = new Solver();
        for (HashMap.Entry<Integer, State> entry : table.table.entrySet()){
            State tmp = entry.getValue();
            Integer tmpHash = tmp.hash;
            if (tmp.cH.isEmpty() && !tmp.isEnd){

                stateReinit(tmp);
                System.out.println(tmp);
                List<State> entryChildren = solver.getNextStates(tmp);
                tmp.setChildren(entryChildren);
                tmp.hashChildren();
                table.table.replace(tmpHash, tmp);
            }
        }
    }

    public static State getStateFromTable(Integer key, Table table){
        if (table.table.containsKey(key)){
            return table.table.get(key);
        }else{
            System.out.println("Doesnt contain key");
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
//        solveWrapper();




        Solver solver = new Solver();
//        solver.solveFromRoot(new State(true));





        Table table = new ObjectMapper().readValue(new File("C:\\JSON output\\StateTable.json"), Table.class);
//        System.out.println(getStateFromTable(root.hashCode(), table));
//        System.out.println(root);
//        root.logic.makeMove(3, "right");
//        System.out.println(root);
//        System.out.println(root.hashCode());
//        root.logic.makeMove(1, "right");
//        System.out.println(root);
//        System.out.println(root.hashCode());
//        combineTable();

        fillChildrenHash(table);
        table.saveToFile("C:\\JSON output\\StateTable.json");

//        System.out.println(root.hashCode());
//        Scanner sc = new Scanner(System.in);
//
//        do {
//            System.out.println("Input Start");
//            int tileIndex = Integer.parseInt(sc.nextLine());
//            System.out.println(tileIndex);
//            String direction = sc.nextLine();
//            System.out.println(direction);
//            root.logic.makeMove(tileIndex, direction);
//            System.out.println(root.hashCode());
////            System.out.println(getStateFromTable(root.hashCode(), table));
//            System.out.println("Exit?");
//        }while(!sc.nextLine().equalsIgnoreCase("exit"));
        System.out.println("-------------------------------");
    }
}
