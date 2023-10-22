package model;

public interface AIStrategy {

    public IndexesPair getMove(Board board) throws NoPossibleMoveToMakeException;

}


