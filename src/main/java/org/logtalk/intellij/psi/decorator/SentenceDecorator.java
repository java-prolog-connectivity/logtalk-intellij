package org.logtalk.intellij.psi.decorator;

import com.intellij.psi.PsiElement;
import org.logtalk.intellij.psi.LogtalkSentence;


public class SentenceDecorator extends PsiElementDecorator {

    public static boolean isSentence(PsiElement psiElement) {
        return psiElement instanceof LogtalkSentence;
    }

    protected SentenceDecorator(PsiElement psiElement) {
        super(psiElement);
    }

    public static SentenceDecorator sentenceDecorator(PsiElement psiElement) {
        if (!isSentence(psiElement)) {
            throw new WrongPsiElementException(psiElement, LogtalkSentence.class);
        }
        return psiElement instanceof SentenceDecorator ? (SentenceDecorator) psiElement : new SentenceDecorator(psiElement);
    }

}
