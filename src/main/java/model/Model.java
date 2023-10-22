package model;

public class Model {
    private Board board;
    private PlayerType noughtPlayerType = PlayerType.HUMAN_PLAYER;
    private PlayerType crossPlayerType = PlayerType.HUMAN_PLAYER;
    private int turn = 0;
    private FieldValue whoseTurn = FieldValue.NOUGHT;
    private GameState state = GameState.MENU;

    public void setState(GameState state) {
        this.state = state;
    }
    public GameState getState() {
        return state;
    }
    public void makeMove(int x, int y) throws Exception {
        if(!board.doesFieldExists(x, y)) {
            throw new Exception("This field do not exist");
        }
        if(!board.isFieldFree(x, y)) {
            throw new Exception("Field with this coordinates is already occupied");
        }
        FieldValue value;
        if(whoseTurn == FieldValue.NOUGHT)
            value = FieldValue.NOUGHT;
        else
            value = FieldValue.CROSS;
        board.setFieldValueWithStateUpdate(x, y, value);
        turn++;
        whoseTurn = whoseTurn.toggle();
    }

    public Board getBoard() {
        return this.board;
    }

    public void setNoughtPlayerType(PlayerType noughtPlayerType) {
        this.noughtPlayerType = noughtPlayerType;
    }

    public void setCrossPlayerType(PlayerType crossPlayerType) {
        this.crossPlayerType = crossPlayerType;
    }

    public void makeAIMove(PlayerType aiType) throws Exception {
        switch (aiType) {

            case EASY_AI -> {
                makeMinMaxAIMove(3);
            }
            case MEDIUM_AI -> {
                makeMinMaxAIMove(4);
            }
            case HARD_AI -> {
                makeMinMaxAIMove(3);
            }
            case RANDOM_AI -> {
                makeRandomAIMove();
            }
            case HUMAN_PLAYER -> {
                throw new Exception("cannot invoke function for human player");
            }
        }
    }
    private void makeMinMaxAIMove(int difficulty) {
        IndexesPair move = new IndexesPair();
        MinMaxAI minMax = new MinMaxAI(difficulty, whoseTurn);
        try {
            move = minMax.getMove(board);
            makeMove(move.getX(), move.getY());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void makeRandomAIMove() throws NoPossibleMoveToMakeException {
        RandomAI randomAI = new RandomAI();
        IndexesPair move = randomAI.getMove(board);
        try {
            makeMove(move.getX(), move.getY());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setBoard(Board board) {
        this.board = board;
        turn = 0;
        whoseTurn = FieldValue.NOUGHT;
    }
    public PlayerType getPlayerTypeWhichHasTurn() {
        if(whoseTurn == FieldValue.NOUGHT)
            return noughtPlayerType;
        return crossPlayerType;
    }
    public void updatePlayersType(PlayerType type, FieldValue whoUpdate) {
        if(whoUpdate == FieldValue.CROSS)
            crossPlayerType = type;
        if(whoUpdate == FieldValue.NOUGHT)
            noughtPlayerType = type;

    }
    public int getTurn() {
        return turn;
    }
    public FieldValue getPlayerTurn() {
        return whoseTurn;
    }

    public Model(Board board, PlayerType noughtPlayerType, PlayerType crossPlayerType, FieldValue whoseTurn, int turn, GameState state) {
        this.board = board;
        this.noughtPlayerType = noughtPlayerType;
        this.crossPlayerType = crossPlayerType;
        this.whoseTurn = whoseTurn;
        this.turn = turn;
        this.state = state;
    }

    public PlayerType getNoughtPlayerType() {
        return noughtPlayerType;
    }
    public PlayerType getCrossPlayerType() {
        return crossPlayerType;
    }

    public Model(Board board) {
        this.board = board;
    }

    public Board getBoardCopy() {
        return board.copy();
    }

    public void resetGame() {
        board.fillAllFieldsWithFieldValue(FieldValue.FREE);
        turn = 0;
        whoseTurn = FieldValue.NOUGHT;
    }



}


