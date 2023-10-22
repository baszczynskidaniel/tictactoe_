package model;

public enum BoardState {
    DRAW,
    NOUGHT_WIN,
    CROSS_WIN,
    PENDING,
    // this state should not happen
    DOUBLE_WIN,
}
