package org.logtalk.parser.operator;

public enum PrecedenceType {
    LOWER, // Y
    STRICTLY_LOWER; //X

    public static PrecedenceType of(char c) {
        switch (c) {
            case 'Y':
                return LOWER;
            case 'X':
                return STRICTLY_LOWER;
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean hasValidArgumentPrecedence(int operatorPrecedence, int argumentPrecedence) {
        if (equals(STRICTLY_LOWER)) {
            return argumentPrecedence < operatorPrecedence;
        } else {
            return argumentPrecedence <= operatorPrecedence;
        }
    }
}
