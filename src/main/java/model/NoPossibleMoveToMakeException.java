package model;

public class NoPossibleMoveToMakeException extends Exception{
    public NoPossibleMoveToMakeException(String message) {
        super(message);
    }
}
