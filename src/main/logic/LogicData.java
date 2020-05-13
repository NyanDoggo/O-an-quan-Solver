package main.logic;

import main.pojo.Board;

public class LogicData {
    public Board board;
    final int quanPoint = 5;
    int capturedQuanCounter;

    public LogicData(Logic logic){
        this.board = logic.board;
        this.capturedQuanCounter = logic.capturedQuanCounter;
    }
}
