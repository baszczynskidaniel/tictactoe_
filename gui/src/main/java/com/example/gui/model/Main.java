package com.example.gui.model;

public class Main {
    public static void main(String[] args) throws ModelFileManagerException, FieldWithIndexesDoNotExistException {
        Board board = new Board(10, 10, 5);
        View view = new View();
        Model model = new Model(board);


        Controller controller = new Controller(model, view);
        controller.gameLoop();
    }
}