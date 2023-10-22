package model;

public enum FieldValue {
    NOUGHT,
    CROSS,
    FREE;

    public char getFieldChar() {
        return switch (this) {
            case NOUGHT -> 'O';
            case CROSS -> 'X';
            case FREE -> ' ';
        };
    }
    public FieldValue toggle() {
        return this == NOUGHT ? CROSS : NOUGHT;
    }

    public static FieldValue getFieldValueFromString(String str) throws IllegalArgumentException {
        for(FieldValue value : FieldValue.values()) {
            if(str.equals(value.toString()))
                return value;
        }
        throw new IllegalArgumentException("str do not match to any model.FieldValue");
    }
}
