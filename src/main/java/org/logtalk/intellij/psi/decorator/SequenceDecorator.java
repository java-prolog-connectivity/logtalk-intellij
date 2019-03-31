package org.logtalk.intellij.psi.decorator;


import static org.logtalk.intellij.psi.decorator.OperationDecorator.isOperation;
import static org.logtalk.intellij.psi.decorator.OperationDecorator.operationDecorator;
import static org.logtalk.parser.operator.Operator.SEQUENCE_SEPARATOR;

import java.util.Iterator;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.intellij.psi.PsiElement;

public class SequenceDecorator extends PsiElementDecorator {

    public static boolean isSequence(PsiElement psiElement) {
        if (!isOperation(psiElement)) {
            return false;
        } else {
            OperationDecorator operationDecorator = operationDecorator(psiElement);
            OperationDecorator.OperationPart operationPart = operationDecorator.iterator().next().next().get();
            return operationPart.getElement().getText().equals(SEQUENCE_SEPARATOR);
        }
    }

    private SequenceDecorator(PsiElement psiElement) {
        super(psiElement);
    }

    public static SequenceDecorator sequenceDecorator(PsiElement psiElement) {
        if (!isSequence(psiElement)) {
            throw new WrongPsiElementException(psiElement, "Sequence");
        }
        return psiElement instanceof SequenceDecorator ? (SequenceDecorator) psiElement : new SequenceDecorator(psiElement);
    }

    public int size() {
        return Iterators.size(getMembersIterator());
    }

    public Iterator<PsiElement> getMembersIterator() {
        return new SequenceMembersIterator(getDecorated());
    }


    private static PsiElement getFirstSequenceChild(PsiElement psiElement) {
        return psiElement.getFirstChild().getFirstChild();
    }

    private static PsiElement getSecondSequenceChild(PsiElement psiElement) {
        return psiElement.getLastChild().getFirstChild().getLastChild();
    }

    private static class SequenceMembersIterator extends AbstractIterator<PsiElement> {
        private PsiElement sequence;

        public SequenceMembersIterator(PsiElement sequence) {
            this.sequence = sequence;
        }

        @Override
        public PsiElement computeNext() {
            if (sequence == null) {
                return endOfData();
            } else if (!isSequence(sequence)) {
                PsiElement last = sequence;
                sequence = null;
                return last;
            } else {
                PsiElement next = getFirstSequenceChild(sequence);
                sequence = getSecondSequenceChild(sequence);
                return next;
            }
        }
    }
}
