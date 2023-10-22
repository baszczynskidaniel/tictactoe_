package com.example.gui.model;

public class Controller {
    private View view;
    private Model model;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
    }
    public void gameLoop() {
        while(true) {
            if(model.getState() == GameState.MENU) {

                view.displayMenu();
                String input = view.getUserInput("type menu option between 1 and 8");
                switch (input) {
                    case "1":
                        setupClassicBoard();
                        setupPlayers();
                        model.setState(GameState.GAME);
                        break;
                    case "2":
                        setupBoard();
                        setupPlayers();
                        model.setState(GameState.GAME);
                        break;
                    case "3":
                        Model model;
                        try {
                            model = ModelFileManager.getModelFromFile("autosave.list");
                        } catch (ModelFileManagerException e) {
                            view.displayMessage(e.getMessage());
                            break;
                        }
                        this.model = model;

                        break;

                    default:
                        view.displayUnrecognizedCommand(input);

                }
            }

            if(model.getState() == GameState.GAME) {
                if (model.getBoardCopy().getState() != BoardState.PENDING) {
                    view.displayGameResult(model.getBoardCopy().getState());
                    view.displayBoard(model.getBoardCopy());
                    break;
                }


                IndexesPair indexesPair = new IndexesPair();
                view.displayBoard(model.getBoardCopy());
                view.displayWhoseTurn(model.getPlayerTurn());

                switch (model.getPlayerTypeWhichHasTurn()) {
                    case HUMAN_PLAYER -> {
                        try {
                            indexesPair = getHumanPlayerMove();
                        } catch (CoordinatesException e) {
                            view.displayMessage(e.getMessage());
                            continue;
                        }
                    }
                    case RANDOM_AI -> {
                        try {
                            indexesPair = getRandomAIMove();
                        } catch (NoPossibleMoveToMakeException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case EASY_AI -> {
                        indexesPair = getMinMaxAIMove(3);
                    }
                    case MEDIUM_AI -> {
                        indexesPair = getMinMaxAIMove(5);
                    }
                    case HARD_AI -> {
                        indexesPair = getMinMaxAIMove(7);
                    }
                }
                try {
                    model.makeMove(indexesPair.getX(), indexesPair.getY());
                } catch (Exception e) {
                    view.displayMessage(e.getMessage());
                }
                try {
                    ModelFileManager.saveModel(model, "autosave.list");
                } catch (ModelFileManagerException e) {
                    view.displayMessage(e.getMessage());
                }
            }
        }
    }
    private void setupGame() {

    }
    private void setupBoard() {
        int width = getUserInputNumberInBoundries("Type board width", Board.MIN_BOARD_SIZE, Board.MAX_BOARD_SIZE);
        int height = getUserInputNumberInBoundries("Type board height", Board.MIN_BOARD_SIZE, Board.MAX_BOARD_SIZE);
        int marksToWin = getUserInputNumberInBoundries("Type number of marks to win", Board.MIN_BOARD_SIZE, Math.max(width, height));
        Board board = new Board(width, height, marksToWin);
        model.setBoard(board);
    }
    private int getUserInputNumberInBoundries(String inputMessage, int start, int end) {
        int result;
        while(true) {
            String input = view.getUserInput(inputMessage);
            try {
                result = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                view.displayUnrecognizedCommand(input);
                continue;
            }
            if(result >= start && result <= end)
                break;
        }
        return result;
    }

    private int getBoardDimensionWithInput(String input) throws CoordinatesException {
        int dimension = 0;
        try {
            dimension = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            view.displayUnrecognizedCommand(input);
        }
        if(dimension > Board.MAX_BOARD_SIZE && dimension < Board.MIN_BOARD_SIZE) {
            throw new CoordinatesException("wrong board size");
        }

        return dimension;
    }
    private void setupPlayers() {
        setupPlayerType(FieldValue.NOUGHT);
        setupPlayerType(FieldValue.CROSS);
    }
    private void setupClassicBoard() {
        Board board = new Board(3, 3, 3);
        model.setBoard(board);
    }
    private void setupPlayerType(FieldValue playerType) {
        while(true) {
            view.displayPlayerTypeMenu();
            String input = view.getUserInput("choose player type");
            if(input.equals("1")) {
                model.updatePlayersType(PlayerType.HUMAN_PLAYER, playerType);
                break;
            }
            if(input.equals("2")) {
                model.updatePlayersType(PlayerType.RANDOM_AI, playerType);
                break;
            }
            if(input.equals("3")) {
                model.updatePlayersType(PlayerType.EASY_AI, playerType);
                break;
            }
            if(input.equals("4")) {
                model.updatePlayersType(PlayerType.MEDIUM_AI, playerType);
                break;
            }
            if(input.equals("5")) {
                model.updatePlayersType(PlayerType.HARD_AI, playerType);
                break;
            }
            view.displayUnrecognizedCommand(input);
        }
    }
    private IndexesPair getMinMaxAIMove(int difficulty) {
        IndexesPair move = new IndexesPair();
        MinMaxAI minMax = new MinMaxAI(difficulty, model.getPlayerTurn());
        try {
            move = minMax.getMove(model.getBoardCopy());
        } catch (NoPossibleMoveToMakeException e) {
            view.displayMessage(e.getMessage());
        }
        return move;
    }
    private IndexesPair getRandomAIMove() throws NoPossibleMoveToMakeException {
        RandomAI randomAI = new RandomAI();
        return randomAI.getMove(model.getBoardCopy());
    }
    private IndexesPair getHumanPlayerMove() throws CoordinatesException {
        String userMove = view.getUserMove();
        return IndexesPair.getIndexesPairFromString(userMove);
    }



}
