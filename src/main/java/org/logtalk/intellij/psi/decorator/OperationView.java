package org.logtalk.intellij.psi.decorator;

import static org.logtalk.intellij.psi.decorator.AtomDecorator.atomDecorator;

import java.util.Optional;

import com.intellij.psi.PsiElement;
import org.logtalk.intellij.psi.decorator.OperationDecorator.OperationPart;
import org.logtalk.intellij.psi.helper.SentenceCorrectness;
import org.logtalk.parser.operator.Operator;
import org.logtalk.parser.operator.OperatorFamily;
import org.logtalk.parser.operator.OperatorSet;

public class OperationView extends PsiElementDecorator {

    private SentenceCorrectness sentenceCorrectness;
    private boolean syntaxError;
    private OperationPart operationPart;
    private OperationContext operationContext;
    private Optional<Operator> operatorOpt = Optional.empty();
    private Optional<OperationView> parentOperatorOpt = Optional.empty();
    private Optional<OperationView> leftArgumentOpt = Optional.empty();
    private Optional<OperationView> rightArgumentOpt = Optional.empty();

    public OperationView(final PsiElement psiElement, SentenceCorrectness sentenceCorrectness) {
        super(psiElement);
        this.sentenceCorrectness = sentenceCorrectness;
        try {
            operationPart = new OperationPart(psiElement);
            if (operationPart.previous().isPresent()) {
                operationContext = operationPart.previous().get().operationView().operationContext;
            } else {
                operationContext = new OperationContext(this);
            }
        } catch(IllegalArgumentException e) { //not an operation part.
        }
    }

    public boolean isSyntaxError() {
        return syntaxError;
    }

    public void setSyntaxError(final boolean syntaxError) {
        this.syntaxError = syntaxError;
        if (syntaxError) {
            sentenceCorrectness.flagError();
        }
    }

    public OperationView getRoot() {
        return operationContext != null ? operationContext.root : null;
    }

    private void setRoot(OperationView root) {
        operationContext.root = root;
    }

    public Optional<OperationView> getParentOperator() {
        return parentOperatorOpt;
    }

    public Optional<OperationView> getLeftArgument() {
        return leftArgumentOpt;
    }

    public void setLeftArgument(OperationView leftArgument) {
        this.leftArgumentOpt = Optional.of(leftArgument);
        leftArgument.parentOperatorOpt = Optional.of(this);
    }

    public Optional<OperationView> getRightArgument() {
        return rightArgumentOpt;
    }

    public void setRightArgument(OperationView rightArgument) {
        this.rightArgumentOpt = Optional.of(rightArgument);
        rightArgument.parentOperatorOpt = Optional.of(this);
    }

    public boolean isOperationPart() {
        return operationPart != null;
    }

    public void setOperationPart(OperationPart operationPart) {
        this.operationPart = operationPart;
    }

    public boolean isOperator() {
        return operatorOpt.isPresent();
    }

    public Optional<Operator> getOperator() {
        return operatorOpt;
    }

    public void setOperator(final Operator operator) {
        this.operatorOpt = Optional.of(operator);
    }

    public SentenceCorrectness getSentenceCorrectness() {
        return sentenceCorrectness;
    }

    public void setSentenceCorrectness(final SentenceCorrectness correctness) {
        this.sentenceCorrectness = correctness;
    }

    private Optional<OperatorFamily> getOperatorFamily() {
        PsiElement atomElement = getDecorated().getFirstChild();
        if (AtomDecorator.isAtom(atomElement) && !atomDecorator(atomElement).isQuoted()) {
            String atomText = atomElement.getText();
            return OperatorSet.getDefault().getOperatorFamily(atomText);
        } else {
            return Optional.empty();
        }
    }


    private boolean lookAheadSaysOperatorShouldBeAtom(Operator operator) {
        if (operator.requiresRightArgument()) {
            Optional<OperationPart> nextOpt = operationPart.next();
            if (nextOpt.isPresent()) {
                OperationPart next = nextOpt.get();
                Optional<OperatorFamily> nextOperatorFamilyOpt = next.operationView().getOperatorFamily();
                if (nextOperatorFamilyOpt.isPresent()) {
                    OperatorFamily nextOperatorFamily = nextOperatorFamilyOpt.get();
                    return nextOperatorFamily.requiresLeftArgument() && nextOperatorFamily.minPrecedence() > operator.getPrecedence();
                }
            }
        }
        return false;
    }

    public void init() {
        if (operationPart == null) { //not an operation part
            return;
        }
        Optional<OperationPart> previousOpt;
        setOperationPart(operationPart);
        previousOpt = operationPart.previous();

        Optional<OperatorFamily> operatorFamilyOpt = getOperatorFamily();
        if (operatorFamilyOpt.isPresent()) { //this psi element is possibly an operator
            OperatorFamily operatorFamily = operatorFamilyOpt.get();
            Optional<Operator> operatorOpt = chooseBest(operatorFamily);
            if (operatorOpt.isPresent()) {
                if (lookAheadSaysOperatorShouldBeAtom(operatorOpt.get())) {
                    if (operationPart.previous().isPresent() && !operationPart.previous().get().operationView().requiresRightArgument()) {
                        setSyntaxError(true);
                    }
                } else {
                    setOperator(operatorOpt.get());
                }
            } else if (previousOpt.isPresent() && !previousOpt.get().operationView().requiresRightArgument()) { //otherwise consider the operator as an atom
                setOperator(operatorFamily.getOperators().iterator().next()); //just pick the first one
                setSyntaxError(true);
            }
        } else { //non operator found
            if (previousOpt.isPresent() && !previousOpt.get().operationView().requiresRightArgument()) {
                setSyntaxError(true);
            }
        }
        // at this stage the operation members have been identified. The tree structure still needs to be created.
        if (!isSyntaxError()) {
            if (previousOpt.isPresent()) {
                OperationPart previous = previousOpt.get();
                if (previous.operationView().requiresRightArgument()) {
                    previous.operationView().setRightArgument(this);
                } else { // this node should be operator expecting a left argument (there is a previous that does not require a right argument)
                    OperationView leftArgument = previous.operationView();
                    while (leftArgument.getParentOperator().isPresent() &&
                            getOperator().get().acceptsLeftArgument(leftArgument.getParentOperator().get().getOperator().get().getPrecedence()) &&
                            !(leftArgument.requiresRightArgument() && leftArgument.getOperator().get().acceptsRightArgument(getOperator().get().getPrecedence()))) {
                        leftArgument = leftArgument.getParentOperator().get();
                    }
                    if (leftArgument.getParentOperator().isPresent()) {
                        leftArgument.getParentOperator().get().setRightArgument(this);
                    } else {
                        setRoot(this);
                    }
                    setLeftArgument(leftArgument);
                }
            }
        }
    }

    private Optional<Operator> chooseBest(OperatorFamily operatorFamily) {
        for (Operator operator : operatorFamily.getOperators()) {
            if ( !((operationPart.isLast() && operator.requiresRightArgument()) ||
                    (operationPart.isFirst() && operator.requiresLeftArgument()) ||
                    (!operationPart.isFirst() && !operationPart.previous().get().operationView().acceptsRight(operator)))) {
                return Optional.of(operator);
            }
        }
        return Optional.empty();
    }



    private boolean requiresRightArgument() {
        return isOperator() && getOperator().get().requiresRightArgument();
    }

    private boolean acceptsRight(Operator operator) {
        if (!isOperator()) {
            return operator.requiresLeftArgument();
        } else {
            if (getOperator().get().getType().requiresRightArgument()) {
                if (operator.requiresLeftArgument()) {
                    return false;
                } else {
                    return getOperator().get().acceptsRightArgument(operator.getPrecedence());
                }
            } else if (operator.requiresLeftArgument()) {
                return operator.acceptsLeftArgument(getOperator().get().getPrecedence());
            } else {
                return false;
            }
        }
    }

    private static class OperationContext {
        private OperationView root;

        public OperationContext(final OperationView root) {
            this.root = root;
        }
    }

}
