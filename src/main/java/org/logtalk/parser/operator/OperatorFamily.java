package org.logtalk.parser.operator;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class OperatorFamily {

    private final String name;
    private final Map<OperatorPosition, Operator> family;

    private OperatorFamily(String name, Map<OperatorPosition, Operator> family) {
        this.name = name;
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public Optional<Operator> getOperator(OperatorPosition position) {
        return Optional.ofNullable(family.get(position));
    }

    public Collection<Operator> getOperators() {
        return family.values();
    }

    public int size() {
        return family.size();
    }

    Map<OperatorPosition, Operator> toMap() {
        return new EnumMap<>(family);
    }

    public boolean requiresLeftArgument() {
        for (Operator op : family.values()) {
            if (!op.requiresLeftArgument()) {
                return false;
            }
        }
        return true;
    }

    public boolean requiresRightArgument() {
        for (Operator op : family.values()) {
            if (!op.requiresRightArgument()) {
                return false;
            }
        }
        return true;
    }

    public static Builder builder(String name) {
        return new Builder(name, new EnumMap<>(OperatorPosition.class));
    }

    public static Builder builder(OperatorFamily operatorFamily) {
        return new Builder(operatorFamily.getName(), operatorFamily.toMap());
    }

    public int minPrecedence() {
        return family.values().stream().mapToInt(Operator::getPrecedence).min().getAsInt();
    }



    public static class Builder {

        private final String name;
        private final Map<OperatorPosition, Operator> family;

        private Builder(String name, Map<OperatorPosition, Operator> family) {
            this.name = name;
            this.family = family;
        }

        public Builder add(Operator op) {
            if (op.getPrecedence() == 0) {
                family.remove(op.getType().getPosition());
            } else {
                family.put(op.getType().getPosition(), op);
            }
            return this;
        }

        public OperatorFamily build() {
            return new OperatorFamily(name, family);
        }
    }
}
