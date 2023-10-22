package com.example.gui.model;

import java.util.Collections;
import java.util.List;

public class MinMaxAI implements AIStrategy {
    private final int depth;
    private final int winPoints = 10;
    private final FieldValue player;
    private final FieldValue playerField;
    private final FieldValue opponentField;
    private final int drawPoints = 0;
    public MinMaxAI(int depth, FieldValue startFromThisPlayer){
        this.depth = depth;
        this.player = startFromThisPlayer;
        if(player == FieldValue.CROSS) {
            playerField = FieldValue.CROSS;
            opponentField = FieldValue.NOUGHT;
        }
        else {
            playerField = FieldValue.NOUGHT;
            opponentField = FieldValue.CROSS;
        }
    }

    @Override
    public IndexesPair getMove(Board board) throws NoPossibleMoveToMakeException {

        try {
            return findBestMove(board);
        } catch (FieldWithIndexesDoNotExistException e) {
            throw new RuntimeException(e);
        }
    }


    private IndexesPair findBestMove(Board board) throws FieldWithIndexesDoNotExistException {
        List<IndexesPair> availableMoves = board.getCoordinatesOfFieldValue(FieldValue.FREE);
        Collections.shuffle(availableMoves);
        IndexesPair bestMove = new IndexesPair();

        int bestMoveValue = Integer.MIN_VALUE;
        for (IndexesPair move : availableMoves) {
            board.setFieldValueWithStateUpdate(move.getX(), move.getY(), playerField);

            int moveScore = 0;
            try {
                moveScore = minMax(board, depth, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            } catch (FieldWithIndexesDoNotExistException e) {
                throw new RuntimeException(e);
            }
            if(bestMoveValue < moveScore) {
                bestMoveValue = moveScore;
                bestMove = move;
            }
            board.setFieldValueWithStateUpdate(move.getX(), move.getY(), FieldValue.FREE);
        }
        return bestMove;
    }

    private int minMax(Board board, int depth, boolean isMax, int alpha, int beta) throws FieldWithIndexesDoNotExistException {
        int score = evaluateBoard(board, depth);
        List<IndexesPair> availableMoves = board.getCoordinatesOfFieldValue(FieldValue.FREE);
        if(score != 0 || availableMoves.isEmpty() || depth == 0)
            return score;

        int bestScore;
        if(isMax) {
            bestScore = Integer.MIN_VALUE;
            for(IndexesPair move: availableMoves) {
                board.setFieldValueWithStateUpdate(move.getX(), move.getY(), playerField);
                bestScore = Math.max(bestScore, minMax(board, depth - 1, false, alpha, beta));
                // undo move
                board.setFieldValueWithStateUpdate(move.getX(), move.getY(), FieldValue.FREE);
                alpha = Math.max(alpha, bestScore);
                if(alpha >= beta)
                    return bestScore;
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for(IndexesPair move: availableMoves) {
                board.setFieldValueWithStateUpdate(move.getX(), move.getY(), opponentField);
                bestScore = Math.min(bestScore, minMax(board, depth - 1, true, alpha, beta));
                // undo move
                board.setFieldValueWithStateUpdate(move.getX(), move.getY(), FieldValue.FREE);
                beta = Math.min(beta, bestScore);
                if(alpha >= beta)
                    return bestScore;
            }
        }
        return bestScore;
    }
    private int evaluateBoard(Board board, int depthFromRecursion) {
        int deltaDepth = this.depth - depthFromRecursion;
        if(board.getState() == BoardState.NOUGHT_WIN) {
            if(player == FieldValue.NOUGHT)
                return winPoints - deltaDepth;
            else
                return deltaDepth - winPoints;
        }
        if(board.getState() == BoardState.CROSS_WIN) {
            if(player == FieldValue.CROSS)
                return winPoints - deltaDepth;
            else
                return deltaDepth - winPoints;
        }
        if(board.getState() == BoardState.DRAW)
            return drawPoints;

        return 0;
    }
}
