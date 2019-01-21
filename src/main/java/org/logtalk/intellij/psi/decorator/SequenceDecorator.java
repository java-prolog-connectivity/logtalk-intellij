package org.logtalk.intellij.psi.decorator;


import static org.logtalk.intellij.psi.decorator.OperationDecorator.isOperation;

import java.util.Iterator;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.intellij.psi.PsiElement;

public class SequenceDecorator extends PsiElementDecorator {

    public static boolean isSequence(PsiElement psiElement) {
        return isOperation(psiElement) /*&&
                operationDecorator(psiElement).getOperatorSymbol().equals(SEQUENCE_SEPARATOR) &&
                psiElement.getChildren().length <= 2*/;
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
        return psiElement.getFirstChild().getLastChild();
    }

    private static class SequenceMembersIterator extends AbstractIterator<PsiElement> {
        private PsiElement sequence;

        public SequenceMembersIterator(PsiElement sequence) {
            this.sequence = sequence;
        }



        @Override
        public PsiElement computeNext() {
            /*PsiElement firstSequenceChild = getFirstSequenceChild(sequence);
            members.add(firstSequenceChild);
            PsiElement secondSequenceChild = getSecondSequenceChild(sequence);
            if (isSequence(secondSequenceChild.getFirstChild())) {
                sequence = secondSequenceChild.getFirstChild();
            } else {
                sequence = null;
                members.add(secondSequenceChild);
            }*/
            return null;
        }
    }
}
