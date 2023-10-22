package model;

import java.util.Scanner;

public class View {
    private char fieldSeparator = '|';

    public void displayGameResult(BoardState state) {
        if(state == BoardState.NOUGHT_WIN)
            System.out.println("Nought win");
        if(state == BoardState.CROSS_WIN)
            System.out.println("Cross win");
        if(state == BoardState.DRAW)
            System.out.println("it is a draw");
        if(state == BoardState.PENDING)
            System.out.println("the game is still going");
        if(state == BoardState.DOUBLE_WIN)
            System.out.println("Nought and Cross have mark in line");
    }
    public String getUserMove() {
        System.out.println("type your move (eg. A1)...");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }


    public void displayMessage(String message) {
        System.out.println(message);
    }
    public void displayMenu() {
        System.out.println("""
                1. Start game 3 X 3 
                2. Start game custom game
                    define own board size and number of marks to win
                3. Load game
                4. exit                        
                """
        );
    }
    public void displayPlayerTypeMenu() {
        System.out.println("""
                chose CROSS player
                1. human player
                2. ai random
                3. ai easy
                4. ai medium
                5. ai hard
                
                """
        );
    }
    public void displayUnrecognizedCommand(String comnand) {
        System.out.println(String.format("'%s' is not recognized", comnand));
    }
    public String getUserInput(String message) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
    public void displayDivider() {
        System.out.println("=======================");
    }

    public String getBoardWidth() {
        System.out.println("type board width...");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public String getBoardHeight() {
        System.out.println("type board height...");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public String getNumberOfGames() {
        System.out.println("type how many games would you like to play");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }




    public void displayWhoseTurn(FieldValue playerFieldValue) {
        System.out.println(playerFieldValue + " move");
    }
    public void displayBoard(Board board) {
        System.out.println(getUpperTextLine(board.getWidth()));
        for(int i = 0; i < board.getHeight(); i++) {
            StringBuilder line = new StringBuilder();
            for(int j = 0; j < board.getWidth(); j++) {
                line.append(fieldSeparator);
                try {
                    line.append(board.getFieldValueByIndexes(i, j).getFieldChar());
                }
                catch (FieldWithIndexesDoNotExistException error) {
                    System.out.println(error.getMessage());
                }
            }
            line.append(fieldSeparator);
            line.append(" ");
            line.append(i + 1);
            System.out.println(line);
        }
    }
    private String getUpperTextLine(int height) {
        StringBuilder upperIndexLine = new StringBuilder(" ");
        char startLetter = 'A';
        for(int i = 0; i < height; i++) {
            upperIndexLine.append(startLetter++);
            upperIndexLine.append(" ");
        }
        return upperIndexLine.toString();
    }

}
