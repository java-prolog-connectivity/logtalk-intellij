package org.logtalk.intellij.psi.decorator;

import com.intellij.psi.PsiElement;
import org.logtalk.intellij.psi.LogtalkCompound;

import java.util.Collections;
import java.util.Iterator;

public class CompoundDecorator extends PsiElementDecorator {

    public static boolean isCompound(PsiElement psiElement) {
        return psiElement instanceof LogtalkCompound;
    }

    public static CompoundDecorator compoundDecorator(PsiElement psiElement) {
        if (!isCompound(psiElement)) {
            throw new WrongPsiElementException(psiElement, LogtalkCompound.class);
        }
        return psiElement instanceof CompoundDecorator ? (CompoundDecorator) psiElement : new CompoundDecorator(psiElement);
    }

    private CompoundDecorator(PsiElement psiElement) {
        super(psiElement);
    }

    public PsiElement getName() {
        return getDecorated().getFirstChild();
    }

    public Iterator<PsiElement> getArgs() {
        PsiElement children = getDecorated().getChildren()[1].getFirstChild();
        if (SequenceDecorator.isSequence(children)) {
            return SequenceDecorator.sequenceDecorator(children).getMembersIterator();
        } else {
            return Collections.singleton(children).iterator();
        }
    }
}
