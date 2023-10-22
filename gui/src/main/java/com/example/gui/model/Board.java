package com.example.gui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Board {
    private int width;
    private int height;
    private BoardState state;
    private int marksToWin;
    public static final int MIN_BOARD_SIZE = 3;
    public static final int MAX_BOARD_SIZE = 15;
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    private FieldValue[][] fields;

    public Board(int width, int height) throws IllegalArgumentException{
        this(width, height, Math.min(width, height));
    }
    public int getNumberOfNonFreeField() {
        int numberOfNonFreeField = 0;
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (fields[i][j] != FieldValue.FREE)
                    numberOfNonFreeField++;
            }
        }
        return numberOfNonFreeField;
    }
    public Board(int width, int height, int marksToWin) throws IllegalArgumentException {
        if(isSizeInBounds(width, height))
            throw new IllegalArgumentException("the value of width and height is not between defined boundaries");

        if(width < marksToWin && height < marksToWin)
            throw new IllegalArgumentException("marksToWin must be equal or smaller to height or width");

        this.width = width;
        this.height = height;
        this.marksToWin = marksToWin;

        fields = new FieldValue[height][width];
        fillAllFieldsWithFieldValue(FieldValue.FREE);
        updateBoardState();
    }

    public Board(String[] fields, int marksToWin) throws IllegalArgumentException {
        this.height = fields.length;

        Pattern pattern = Pattern.compile("[XO ]+");
        if(fields.length == 0)
            throw new IllegalArgumentException("fields cannot be empty");


        this.width = fields[0].length();
        if(isSizeInBounds(width, height)) {
            throw new IllegalArgumentException("the value of width and height is not between defined boundaries");
        }
        for(String field : fields) {
            if(field.length() != width){
                throw new IllegalArgumentException("each element in fields must be the same size");
            }
            Matcher matcher = pattern.matcher(field);
            if(!matcher.find()) {
                throw new IllegalArgumentException("fields can contain only this characters: 'X', 'O', ' ' ");
            }
        }
        if(width < marksToWin && height < marksToWin)
            throw new IllegalArgumentException("marksToWin must be equal or smaller to height or width");

        this.fields = new FieldValue[height][width];
        this.marksToWin = marksToWin;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                FieldValue value = FieldValue.FREE;
                if(fields[i].charAt(j) == 'X') {
                    value = FieldValue.CROSS;
                }
                if(fields[i].charAt(j) == 'O') {
                    value = FieldValue.NOUGHT;
                }
                this.fields[i][j] = value;
            }
        }
        updateBoardState();
    }

    public BoardState getState(){
        return state;
    }

    private boolean isSizeInBounds(int width, int height) {
        return width < MIN_BOARD_SIZE || height < MIN_BOARD_SIZE || width > MAX_BOARD_SIZE || height > MAX_BOARD_SIZE;
    }
    public void fillAllFieldsWithFieldValue(FieldValue value){
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                fields[i][j] = value;
            }
        }
    }
    public void updateBoardState() {
        Map<FieldValue, Boolean> result = new TreeMap<>();
        result.put(FieldValue.NOUGHT, false);
        result.put(FieldValue.CROSS, false);
        int numberOfFreeFields = 0;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                FieldValue value = fields[i][j];
                if(value == FieldValue.FREE) {
                    numberOfFreeFields++;
                    continue;
                }
                if(isStrike(i, j))
                    result.put(value, true);
            }
        }
        state = getBoardState(result, numberOfFreeFields);
    }

    public boolean isStrike(int x, int y) {
        if(isHorizontalStrikeFromIndex(x, y)) {
            return true;
        }
        if(isVerticalStrikeFromIndex(x, y)){
            return true;
        }
        if(isDiagonalRightDownStrikeFromIndex(x, y)){
            return true;
        }
        return isDiagonalLeftDownStrikeFromIndex(x, y);
    }

    private BoardState getBoardState(Map<FieldValue, Boolean> result, int numberOfFreeFields){
        if(result.get(FieldValue.CROSS) && !result.get(FieldValue.NOUGHT))
            return BoardState.CROSS_WIN;
        if(!result.get(FieldValue.CROSS) && result.get(FieldValue.NOUGHT))
            return BoardState.NOUGHT_WIN;
        if(result.get(FieldValue.CROSS) && result.get(FieldValue.NOUGHT))
            return BoardState.DOUBLE_WIN;
        if(!result.get(FieldValue.CROSS) && !result.get(FieldValue.NOUGHT) && numberOfFreeFields == 0)
            return BoardState.DRAW;

        return BoardState.PENDING;
    }


    private boolean isHorizontalStrikeFromIndex(int x, int y) {
        if(!doesFieldExists(x, y))
            return false;
        if(x + marksToWin > height)
            return false;
        if(y >= width)
            return false;

        FieldValue value = fields[x][y];

        for(int i = 1; i < marksToWin; i++){
            if(fields[x + i][y] != value)
                return false;
        }
        return true;
    }
    public Board copy(){
        Board copy = null;
        try {
            copy = new Board(width, height, marksToWin);
        } catch (IllegalArgumentException ignored) {}
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                try {
                    assert copy != null;
                    copy.setFieldValue(i, j, fields[i][j]);
                } catch (FieldWithIndexesDoNotExistException ignored) {}
            }
        }
        assert copy != null;
        copy.updateBoardState();
        return copy;
    }
    // used in copy method for improving performance
    private void setFieldValue(int x, int y, FieldValue value) throws FieldWithIndexesDoNotExistException {
        if(!doesFieldExists(x, y)) {
            String errorMessage = String.format("The board size is %d by %d found index x = %d, y = %d",
                    MIN_BOARD_SIZE, MAX_BOARD_SIZE, x, y);
            throw new FieldWithIndexesDoNotExistException(errorMessage);
        }
        fields[x][y] = value;
    }
    private boolean isVerticalStrikeFromIndex(int x, int y) {
        if(!doesFieldExists(x, y))
            return false;
        if(y + marksToWin > width)
            return false;
        if(x >= height)
            return false;

        FieldValue value = fields[x][y];
        for(int i = 1; i < marksToWin; i++){
            if(fields[x][y + i] != value)
                return false;
        }
        return true;
    }
    private boolean isDiagonalRightDownStrikeFromIndex(int x, int y) {
        if(!doesFieldExists(x, y))
            return false;
        if(x + marksToWin > height)
            return false;
        if(y + marksToWin > width)
            return false;
        FieldValue value = fields[x][y];
        for(int i = 1; i < marksToWin; i++){
            if(fields[x + i][y + i] != value)
                return false;
        }
        return true;
    }
    private boolean isDiagonalLeftDownStrikeFromIndex(int x, int y) {
        if(!doesFieldExists(x, y))
            return false;
        if(x - (marksToWin - 1) < 0)
            return false;
        if(y + marksToWin > width)
            return false;
        FieldValue value = fields[x][y];
        for(int i = 1; i < marksToWin; i++){
            if(fields[x - i][y + i] != value)
                return false;
        }
        return true;
    }

    public boolean isFieldFree(int x, int y) {
        if(!doesFieldExists(x, y))
            return  false;
        return fields[x][y] == FieldValue.FREE;
    }
    public List<IndexesPair> getCoordinatesOfFieldValue(FieldValue value) {
        List<IndexesPair> coordinates = new ArrayList<IndexesPair>();
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(fields[i][j] == value) {
                    IndexesPair element = new IndexesPair(i, j);
                    coordinates.add(element);
                }
            }
        }
        return coordinates;
    }
    public int getMarksToWin() {
        return marksToWin;
    }
    public void setFieldValueWithStateUpdate(int x, int y, FieldValue value) throws FieldWithIndexesDoNotExistException{
        setFieldValue(x, y, value);
        updateBoardState();
    }
    public boolean isThereAnyFreeField() {
        if(state == BoardState.PENDING)
            return  true;
        else
            return false;
    }

    public String toString() {
        StringBuilder stringBoard = new StringBuilder();
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                stringBoard.append(fields[i][j].getFieldChar());
            }
            stringBoard.append("\n");
        }
        return stringBoard.toString();
    }


    public boolean doesFieldExists(int x, int y){
        if(x < 0 || y < 0)
            return false;
        return x < height && y < width;
    }
    public FieldValue getFieldValueByIndexes(int x, int y) throws FieldWithIndexesDoNotExistException {
        if(!doesFieldExists(x, y)) {
            String errorMessage = String.format("The board size is %d by %d found index x = %d, y = %d",
                    MIN_BOARD_SIZE, MAX_BOARD_SIZE, x, y);
            throw new FieldWithIndexesDoNotExistException(errorMessage);
        }
        return fields[x][y];
    }
}
