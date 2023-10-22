package model;

import java.io.*;
import java.util.HashMap;
import java.util.IllformedLocaleException;
import java.util.Map;

public class GameFileManager {

    private static final String MODEL_LOAD_ERROR_MSG = "Cannot load model from file";
    private static final String MODEL_SAVE_ERROR_MSG = "Cannot save model to file";


    static private Map<String, String> getModelAsMap(Model model) {
        Map<String, String> map = new HashMap<>();
        map.put("Whose turn", model.getPlayerTurn().toString());
        map.put("Nought player type", model.getNoughtPlayerType().toString());
        map.put("Cross player type", model.getCrossPlayerType().toString());
        map.put("Turn", String.valueOf(model.getTurn()));
        map.put("model.Board", model.getBoardCopy().toString());
        map.put("Marks to win", String.valueOf(model.getBoardCopy().getMarksToWin()));
        return map;
    }


    static public void saveModel(Model model, String fileName) throws ModelFileManagerException {
        Map<String, String> modelMap = getModelAsMap(model);
        File savedGameMap = new File(fileName);
        System.out.println(modelMap.toString());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedGameMap));
            oos.writeObject(modelMap);
            oos.close();
        } catch (Exception e) {
            throw new ModelFileManagerException(MODEL_SAVE_ERROR_MSG);
        }
    }

    static public Model getModelFromFile(String fileName) throws ModelFileManagerException {
        Map<String, String> modelMap = loadModelMapFromFile(fileName);

        String[] boardStr = modelMap.get("model.Board").split("\n");
        int turn = parseModelMapValue(modelMap, "Turn");
        int marksToWin = parseModelMapValue(modelMap, "Marks to win");
        Board board = createBoard(boardStr, marksToWin);

        PlayerType noughtPlayerType = getPlayerTypeFromString(modelMap, "Nought player type");
        PlayerType crossPlayerType = getPlayerTypeFromString(modelMap, "Cross player type");
        FieldValue whoseTurn = getFieldValueFromString(modelMap, "Whose turn");

        return new Model(board, noughtPlayerType, crossPlayerType, whoseTurn, turn, GameState.GAME);
    }

    private static Map<String, String> loadModelMapFromFile(String fileName) throws ModelFileManagerException {
        Map<String, String> modelMap = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Object readMap = ois.readObject();
            if (readMap instanceof HashMap) {
                modelMap.putAll((HashMap) readMap);
            }
        } catch (Exception e) {
            throw new ModelFileManagerException(MODEL_LOAD_ERROR_MSG);
        }
        return modelMap;
    }

    private static int parseModelMapValue(Map<String, String> modelMap, String key) throws ModelFileManagerException {
        try {
            return Integer.parseInt(modelMap.get(key));
        } catch (NumberFormatException e) {
            throw new ModelFileManagerException(MODEL_LOAD_ERROR_MSG);
        }
    }

    private static Board createBoard(String[] boardStr, int marksToWin) throws ModelFileManagerException {
        try {
            return new Board(boardStr, marksToWin);
        } catch (IllformedLocaleException e) {
            throw new ModelFileManagerException(MODEL_LOAD_ERROR_MSG);
        }
    }

    private static PlayerType getPlayerTypeFromString(Map<String, String> modelMap, String key) throws ModelFileManagerException {
        try {
            return PlayerType.getPlayerTypeFromString(modelMap.get(key));
        } catch (IllegalArgumentException e) {
            throw new ModelFileManagerException(MODEL_LOAD_ERROR_MSG);
        }
    }

    private static FieldValue getFieldValueFromString(Map<String, String> modelMap, String key) throws ModelFileManagerException {
        try {
            return FieldValue.getFieldValueFromString(modelMap.get(key));
        } catch (IllegalArgumentException e) {
            throw new ModelFileManagerException(MODEL_LOAD_ERROR_MSG);
        }
    }
}
