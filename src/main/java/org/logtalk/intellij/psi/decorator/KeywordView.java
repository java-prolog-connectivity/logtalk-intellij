package org.logtalk.intellij.psi.decorator;

import com.intellij.psi.PsiElement;
import org.logtalk.intellij.psi.helper.LogtalkPsiUtil;

public class KeywordView extends PsiElementDecorator {

    private boolean atomKeyword;
    private boolean compoundKeyword;

    public KeywordView(final PsiElement psiElement) {
        super(psiElement);
    }

    public boolean isAtomKeyword() {
        return atomKeyword;
    }

    public void setAtomKeyword(final boolean atomKeyword) {
        this.atomKeyword = atomKeyword;
    }

    public boolean isCompoundKeyword() {
        return compoundKeyword;
    }

    public void setCompoundKeyword(final boolean compoundKeyword) {
        this.compoundKeyword = compoundKeyword;
    }

    public void init() {
        if (LogtalkPsiUtil.isAtomKeyword(getDecorated())) {
            setAtomKeyword(true);
        } else if (LogtalkPsiUtil.isCompoundNameKeyword(getDecorated())) {
            setCompoundKeyword(true);
        }
    }
}
