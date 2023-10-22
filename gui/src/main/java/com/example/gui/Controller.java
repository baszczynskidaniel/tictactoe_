package com.example.gui;

import com.example.gui.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;
import java.util.stream.IntStream;

public class Controller {
    public Button restartButton;
    public GridPane boardGrid;
    public ComboBox<Integer> colsComboBox;
    public ComboBox<Integer> marksToWinComboBox;
    public ComboBox<Integer> rowsComboBox;
    public ComboBox<PlayerType> noughtPlayerComboBox;
    public ComboBox<PlayerType> crossPlayerComboBox;
    public Label gameResult;
    private Model model;

    public Controller() {
        Platform.runLater(() -> {
            Board board = new Board(5, 5, 3);
            model = new Model(board);
            initComboBoxes();
            initBoard();
            setGameResult();

        });

    }

    private void initBoard() {

        Platform.runLater(() -> {
            System.out.println(boardGrid.getChildren().size());
            boardGrid.getChildren().remove(0, boardGrid.getChildren().size());
            Board board = model.getBoardCopy();
            boardGrid.setAlignment(Pos.CENTER);
            boardGrid.setVgap(10);
            boardGrid.setHgap(10);
            for (int i = 0; i < board.getHeight(); i++) {
                for (int j = 0; j < board.getWidth(); j++) {
                    int finalI = i;
                    int finalJ = j;
                    StackPane stackPane = new StackPane();
                    stackPane.setStyle("-fx-background-color: #EEEEEE;");
                    ImageView imageView = null;
                    try {
                        imageView = createImageView(board.getFieldValueByIndexes(i, j));
                    } catch (FieldWithIndexesDoNotExistException e) {
                        throw new RuntimeException(e);
                    }
                    stackPane.setMinSize(120, 120);
                    stackPane.getChildren().add(imageView);
                    stackPane.setOnMouseClicked(

                            event -> {
                                if (model.getBoardCopy().getState() == BoardState.PENDING) {
                                    if (model.getPlayerTypeWhichHasTurn() == PlayerType.HUMAN_PLAYER) {
                                        Platform.runLater(() -> {
                                            try {
                                                model.makeMove(finalI, finalJ);
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                            initBoard();
                                        });
                                    }

                                }
                            }
                    );
                    boardGrid.add(stackPane, j, i); // add image to cell (j, i)
                }
            }
            setGameResult();
        });
        if (model.getPlayerTypeWhichHasTurn() != PlayerType.HUMAN_PLAYER && model.getBoardCopy().getState() == BoardState.PENDING) {
            Platform.runLater(() -> {
                try {
                    model.makeAIMove(model.getPlayerTypeWhichHasTurn());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                initBoard();
            });
        }
    }

    private ImageView createImageView(FieldValue value) {
        Image image = getFieldValueImage(value);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100); // set the width of the image
        imageView.setFitHeight(100); // set the height of the image
        return imageView;
    }

    private Image getFieldValueImage(FieldValue value) {
        Image image;
        switch (value) {
            case NOUGHT -> {
                image = getNoughtImage();
            }
            case CROSS -> {
                image = getCrossImage();
            }
            case FREE -> {
                image = getFreeImage();
            }
            default -> throw new IllegalStateException("Unexpected value: " + value);
        }
        return image;
    }

    private Image getNoughtImage() {
        return new Image("com/example/gui/circle.png");
    }

    private Image getCrossImage() {
        return new Image("com/example/gui/cross.png");
    }

    private Image getFreeImage() {
        return new Image("com/example/gui/free.png");
    }

    private boolean isSettingValueProgrammatically = true;

    private void initComboBoxes() {
        List<Integer> boardSizeRange = IntStream.rangeClosed(Board.MIN_BOARD_SIZE, Board.MAX_BOARD_SIZE)
                .boxed().toList();
        model.setCrossPlayerType(model.getCrossPlayerType());
        model.setNoughtPlayerType(model.getNoughtPlayerType());
        crossPlayerComboBox.getItems().addAll(PlayerType.values());
        crossPlayerComboBox.setValue(model.getCrossPlayerType());
        noughtPlayerComboBox.getItems().addAll(PlayerType.values());
        noughtPlayerComboBox.setValue(model.getNoughtPlayerType());
        colsComboBox.getItems().addAll(boardSizeRange);
        colsComboBox.setValue(model.getBoardCopy().getHeight());
        rowsComboBox.getItems().addAll(boardSizeRange);
        rowsComboBox.setValue(model.getBoardCopy().getWidth());
        List<Integer> marksToWinRange = IntStream.rangeClosed(Board.MIN_BOARD_SIZE, Math.max(colsComboBox.getValue(), rowsComboBox.getValue()))
                .boxed().toList();
        marksToWinComboBox.getItems().addAll(marksToWinRange);

        marksToWinComboBox.setValue(model.getBoardCopy().getMarksToWin());
        isSettingValueProgrammatically = false;
    }

    @FXML
    public void restartGame(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            model.resetGame();
            initBoard();
        });

    }

    @FXML void setGameResult() {
        Platform.runLater(() -> {
            gameResult.setText(model.getBoardCopy().getState().toString());
        });
    }

    @FXML
    public void setBoard(ActionEvent actionEvent) {
        if (!isSettingValueProgrammatically) {
            Platform.runLater(() -> {
                isSettingValueProgrammatically = true;
                int height = colsComboBox.getValue();
                int width = rowsComboBox.getValue();
                int marksToWin = Math.min(Math.max(height, width), marksToWinComboBox.getValue());

                List<Integer> marksToWinRange = IntStream.rangeClosed(Board.MIN_BOARD_SIZE, Math.max(colsComboBox.getValue(), rowsComboBox.getValue()))
                        .boxed().toList();
                marksToWinComboBox.getItems().clear();
                marksToWinComboBox.getItems().addAll(marksToWinRange);

                marksToWinComboBox.setValue(marksToWin);

                Board board = new Board(height, width, marksToWin);
                model.setBoard(board);
                model.resetGame();
                initBoard();
                isSettingValueProgrammatically = false;
            });

        }
    }

    @FXML
    public void setNoughtPlayer(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            model.resetGame();
            model.setNoughtPlayerType(noughtPlayerComboBox.getValue());
            initBoard();
        });

    }

    @FXML
    public void setCrossPlayer(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            model.resetGame();
            model.setCrossPlayerType(crossPlayerComboBox.getValue());
            initBoard();
        });
    }
}