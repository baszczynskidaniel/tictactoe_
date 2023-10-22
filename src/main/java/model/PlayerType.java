package model;

public enum PlayerType {
    EASY_AI,
    MEDIUM_AI,
    HARD_AI,
    RANDOM_AI,
    HUMAN_PLAYER;

    public static PlayerType getPlayerTypeFromString(String str) throws IllegalArgumentException {
        for(PlayerType value : PlayerType.values()) {
            if(str.equals(value.toString()))
                return value;
        }
        throw new IllegalArgumentException("str do not match to any Player type");
    }
}
