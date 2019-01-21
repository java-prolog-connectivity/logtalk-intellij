package org.logtalk.parser.operator;

import static org.logtalk.parser.operator.Operator.op;
import static org.logtalk.parser.operator.OperatorType.FX;
import static org.logtalk.parser.operator.OperatorType.FY;
import static org.logtalk.parser.operator.OperatorType.XFX;
import static org.logtalk.parser.operator.OperatorType.XFY;
import static org.logtalk.parser.operator.OperatorType.YFX;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class OperatorSet {

    private static final OperatorSet DEFAULT_OPERATOR_SET = createDefaultOperators();

    public static OperatorSet getDefault() {
        return DEFAULT_OPERATOR_SET;
    }

    private static OperatorSet createDefaultOperators() {
        OperatorSet operatorSet = new OperatorSet();
        operatorSet.addAll(
                op(200, FY, "-"),
                op(400, YFX, ">>"),
                op(200, FY, "++"),
                op(600, FY, "^^"),
                op(600, FY, "::"),
                op(600, XFY, "::"),
                op(200, FY, "@"),
                op(200, FY, "?"),
                op(250, YFX, "?"),
                op(400, YFX, "<<"),
                op(700, XFX, "as"),
                op(200, FY, "+"),
                op(1, FX, "$"),
                op(990, XFX, ":="),
                op(200, FY, "--"),
                op(600, FY, ":"),
                op(1000, XFY, ","),
                op(700, XFX, ">"),
                op(1150, FX, "public"),
                op(1200, FX, ":-"),
                op(1200, XFX, ":-"),
                op(500, YFX, "/\\"),
                op(400, YFX, "xor"),
                op(700, XFX, "=="),
                op(700, XFX, "=\\="),
                op(700, XFX, "=:="),
                op(500, YFX, "-"),
                op(700, XFX, "=@="),
                op(400, YFX, "mod"),
                op(100, YFX, "."),
                op(700, XFX, "@=<"),
                op(400, YFX, "rem"),
                op(700, XFX, "@>"),
                op(700, XFX, "@>="),
                op(700, XFX, "\\=@="),
                op(700, XFX, "@<"),
                op(1150, FX, "meta_predicate"),
                op(1200, FX, "?-"),
                op(1200, XFX, "-->"),
                op(1150, FX, "module_transparent"),
                op(700, XFX, ">:<"),
                op(400, YFX, "rdiv"),
                op(700, XFX, ":<"),
                op(400, YFX, "//"),
                op(200, FY, "\\"),
                op(400, YFX, "div"),
                op(1105, XFY, "|"),
                op(400, YFX, "/"),
                op(1150, FX, "discontiguous"),
                op(1100, XFY, ";"),
                op(700, XFX, "\\="),
                op(1050, XFY, "->"),
                op(1150, FX, "thread_local"),
                op(500, YFX, "\\/"),
                op(900, FY, "\\+"),
                op(700, XFX, "\\=="),
                op(200, XFX, "**"),
                op(1150, FX, "thread_initialization"),
                op(1150, FX, "multifile"),
                op(200, XFY, "^"),
                op(700, XFX, "=.."),
                op(1150, FX, "initialization"),
                op(1150, FX, "dynamic"),
                op(1150, FX, "volatile"),
                op(700, XFX, "is"),
                op(400, YFX, "*"),
                op(700, XFX, "="),
                op(700, XFX, ">="),
                op(1050, XFY, "*->"),
                op(700, XFX, "<"),
                op(700, XFX, "=<"),
                op(500, YFX, "+"),
                op(600, XFY, ":"));
        return operatorSet;
    }

    private final Map<String, OperatorFamily> operatorsMap = new ConcurrentHashMap<>();

    private void register(OperatorFamily operatorFamily) {
        if (operatorFamily.size() == 0) {
            operatorsMap.remove(operatorFamily.getName());
        } else {
            operatorsMap.put(operatorFamily.getName(), operatorFamily);
        }
    }

    public void add(Operator op) {
        Optional<OperatorFamily> operatorFamilyOpt = getOperatorFamily(op.getName());
        OperatorFamily operatorFamily;
        if (operatorFamilyOpt.isPresent()) {
            operatorFamily = OperatorFamily.builder(operatorFamilyOpt.get()).add(op).build();
        } else {
            operatorFamily = OperatorFamily.builder(op.getName()).add(op).build();
        }
        register(operatorFamily);
    }

    public void addAll(Operator... operators) {
        for (Operator op : operators) {
            add(op);
        }
    }

    public boolean contains(String name) {
        return operatorsMap.containsKey(name);
    }

    public Optional<OperatorFamily> getOperatorFamily(String name) {
        return Optional.ofNullable(operatorsMap.get(name));
    }

    public void remove(String name) {
        operatorsMap.remove(name);
    }

}
