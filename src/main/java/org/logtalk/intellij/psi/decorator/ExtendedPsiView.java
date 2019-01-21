package org.logtalk.intellij.psi.decorator;

import java.util.Optional;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import org.logtalk.intellij.psi.helper.SentenceCorrectness;
import org.logtalk.parser.operator.OperatorType;

public class ExtendedPsiView extends PsiElementDecorator {

    private static final Key<ExtendedPsiView> KEY = new Key<>("Extended Psi View");
    private static final String ATOM_KEYWORD_DESCRIPTION = "Atom keyword";
    private static final String FUNCTOR_KEYWORD_DESCRIPTION = "Functor keyword";
    private static final String WRONG_OPERATION_DESCRIPTION = "Wrong operation";

    private final KeywordView keywordView;
    private final OperationView operationView;

    public static Optional<ExtendedPsiView> getExtendedPsiView(PsiElement element) {
        return Optional.ofNullable(element.getUserData(KEY));
    }

    public static ExtendedPsiView getOrCreateExtendedPsiView(PsiElement element) {
        return getExtendedPsiView(element).orElseGet(() -> new ExtendedPsiView(element));
    }

    public static ExtendedPsiView sentencePsiView(PsiElement element) {
        return new ExtendedPsiView(element, new SentenceCorrectness());
    }

    private static Optional<SentenceCorrectness> getParentSentenceCorrectness(PsiElement element) {
        if (element.getParent() != null) {
            Optional<ExtendedPsiView> parentExtendedViewOpt = getExtendedPsiView(element.getParent());
            if (parentExtendedViewOpt.isPresent()) {
                return Optional.of(parentExtendedViewOpt.get().operationView.getSentenceCorrectness());
            }
        }
        return Optional.empty();
    }

    public ExtendedPsiView(PsiElement element) {
        this(element, getParentSentenceCorrectness(element).orElseGet(SentenceCorrectness::new));
    }

    private ExtendedPsiView(PsiElement element, SentenceCorrectness sentenceCorrectness) {
        super(element);
        element.putUserData(KEY, this);
        keywordView = new KeywordView(element);
        operationView = new OperationView(element, sentenceCorrectness);
    }

    public KeywordView keywordView() {
        return keywordView;
    }

    public OperationView operationView() {
        return operationView;
    }


    @Override
    public String toString() {
        if (operationView.isSyntaxError()) {
            return WRONG_OPERATION_DESCRIPTION;
        }
        if (operationView.getOperator().isPresent()) {
            return operationView.getOperator().get().toString();
        } else if (keywordView.isAtomKeyword()) {
            return ATOM_KEYWORD_DESCRIPTION;
        } else if (keywordView.isCompoundKeyword()) {
            return FUNCTOR_KEYWORD_DESCRIPTION;
        }
        return "";
    }

    public void init() {
        keywordView.init();
        operationView.init();
    }

}
