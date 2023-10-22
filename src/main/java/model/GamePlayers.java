package model;

public class GamePlayers {
    public PlayerType getNoughtPlayerType() {
        return noughtPlayerType;
    }

    public void setNoughtPlayerType(PlayerType noughtPlayerType) {
        this.noughtPlayerType = noughtPlayerType;
    }

    public PlayerType getCrossPlayerType() {
        return crossPlayerType;
    }

    public void setCrossPlayerType(PlayerType crossPlayerType) {
        this.crossPlayerType = crossPlayerType;
    }

    public FieldValue getWhoseMove() {
        return whoseMove;
    }

    public void setWhoseMove(FieldValue whoseMove) {
        this.whoseMove = whoseMove;
    }

    private PlayerType noughtPlayerType;
    private PlayerType crossPlayerType;
    private FieldValue whoseMove;

    public void toggleWhoseMove() {
        whoseMove = whoseMove.toggle();
    }
}
