package com.example.gui.model;

import java.util.List;
import java.util.Random;

public class RandomAI implements AIStrategy {
    @Override
    public IndexesPair getMove(Board board) throws NoPossibleMoveToMakeException {
        List<IndexesPair> availableMoves = board.getCoordinatesOfFieldValue(FieldValue.FREE);
        if(availableMoves.isEmpty())
            throw new NoPossibleMoveToMakeException("there is no possible move to make");

        Random random = new Random();
        int index = random.nextInt(availableMoves.size());
        return availableMoves.get(index);
    }
}
