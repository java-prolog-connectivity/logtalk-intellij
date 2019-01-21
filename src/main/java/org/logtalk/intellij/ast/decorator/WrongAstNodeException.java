package org.logtalk.intellij.ast.decorator;


import com.intellij.psi.tree.IElementType;

public class WrongAstNodeException extends RuntimeException {

    private final IElementType actual;
    private final IElementType expected;

    public WrongAstNodeException(IElementType actual, IElementType expected) {
        this.actual = actual;
        this.expected = expected;
    }

    public IElementType getActual() {
        return actual;
    }

    public IElementType getExpected() {
        return expected;
    }

    @Override
    public String getMessage() {
        return expected + " expected. Actual: " + actual;
    }

}
