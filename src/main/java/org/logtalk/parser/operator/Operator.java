package org.logtalk.parser.operator;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.Objects;

public class Operator {

    public static final String SEQUENCE_SEPARATOR = ",";
    public static final String RULE_OPERATOR = ":-"; //rules and directives
    public static final String GRAMMAR__RULE_OPERATOR = "-->";

    private final int precedence;
    private final OperatorType type;
    private final String name;

    private Operator(int precedence, OperatorType type, String name) {
        checkArgument(precedence >= 0 && precedence <= 1200);
        this.precedence = precedence;
        this.type = type;
        this.name = name;
    }

    public static Operator op(int precedence, OperatorType type, String name) {
        requireNonNull(type);
        requireNonNull(name);
        return new Operator(precedence, type, name);
    }

    public int getPrecedence() {
        return precedence;
    }

    public OperatorType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean requiresLeftArgument() {
        return type.requiresLeftArgument();
    }

    public boolean requiresRightArgument() {
        return type.requiresRightArgument();
    }

    public boolean acceptsLeftArgument(int precedence) {
        return requiresLeftArgument() && (type.getLeftArgumentPrecedence().hasValidArgumentPrecedence(this.precedence, precedence));
    }

    public boolean acceptsRightArgument(int precedence) {
        return requiresRightArgument() && (type.getRightArgumentPrecedence().hasValidArgumentPrecedence(this.precedence, precedence));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Operator)) return false;
        final Operator operator = (Operator) o;
        return precedence == operator.precedence &&
                type == operator.type &&
                Objects.equals(name, operator.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(precedence, type, name);
    }

    @Override
    public String toString() {
        return String.format("op(%s, %s, '%s')", getPrecedence(), getType(), getName());
    }

}
