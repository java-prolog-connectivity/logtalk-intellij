package org.logtalk.intellij.psi.decorator;


import static org.logtalk.intellij.psi.decorator.ExtendedPsiView.getOrCreateExtendedPsiView;
import static org.logtalk.intellij.psi.decorator.TermDecorator.isBasicTerm;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

import com.google.common.collect.AbstractIterator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.logtalk.intellij.psi.LogtalkBasicTerm;
import org.logtalk.intellij.psi.LogtalkOperation;
import org.logtalk.intellij.psi.LogtalkTypes;
import org.logtalk.intellij.psi.helper.LogtalkPsiUtil;

public class OperationDecorator extends PsiElementDecorator {

    public static boolean isOperation(PsiElement psiElement) {
        return psiElement instanceof LogtalkOperation;
    }

    private OperationDecorator(PsiElement psiElement) {
        super(psiElement);
    }

    public static OperationDecorator operationDecorator(PsiElement psiElement) {
        if (!isOperation(psiElement)) {
            throw new WrongPsiElementException(psiElement, LogtalkOperation.class);
        }
        return psiElement instanceof OperationDecorator ? (OperationDecorator) psiElement : new OperationDecorator(psiElement);
    }

    public Iterator<OperationPart> iterator() {
        return new AbstractIterator<OperationPart>() {
            Optional<OperationPart> nextOpt = Optional.of(new OperationPart(OperationDecorator.this.getFirstChild()));
            @Override
            protected OperationPart computeNext() {
                OperationPart next;
                if (nextOpt.isPresent()) {
                    next = nextOpt.get();
                    nextOpt = next.next();
                } else {
                    next = endOfData();
                }
                return next;
            }
        };
    }

    public void init() {
        for (OperationPart part : (Iterable<OperationPart>) this::iterator) {
            part.operationView().init();
        }
    }

    public static class OperationPart {

        public static boolean isOperationPart(PsiElement element) {
            return isBasicTerm(element) &&
                    (LogtalkPsiUtil.hasParentWithType(element, LogtalkTypes.OPERATION) ||
                            (element.getParent() != null  &&
                            LogtalkPsiUtil.hasParentWithType(element.getParent(), LogtalkTypes.OPERATION)));
        }

        private final LogtalkBasicTerm element;
        private final ExtendedPsiView extendedView;

        public OperationPart(PsiElement element) {
            if (!isOperationPart(element)) {
                throw new IllegalArgumentException();
            }
            this.element = (LogtalkBasicTerm) element;
            extendedView = getOrCreateExtendedPsiView(element);
        }

        public LogtalkBasicTerm getElement() {
            return element;
        }

        public boolean isFirst() {
            PsiElement operation = element.getParent();
            if (!LogtalkPsiUtil.getElementType(operation).equals(LogtalkTypes.OPERATION)) {
                return false;
            }
            PsiElement term = operation.getParent();
            if (!LogtalkPsiUtil.getElementType(term).equals(LogtalkTypes.TERM)) {
                return false;
            }
            return !LogtalkPsiUtil.hasParentWithType(term, LogtalkTypes.OPERATION);
        }

        public boolean isLast() {
            return LogtalkPsiUtil.hasParentWithType(element, LogtalkTypes.TERM);
        }

        private boolean isLastButOne() {
            if (isLast()) {
                return false;
            }
            PsiElement term = getNextSiblingWithType(element, LogtalkTypes.TERM);
            return isBasicTerm(term.getFirstChild());
        }

        private PsiElement getPrevSiblingWithType(PsiElement element, IElementType type) {
            PsiElement prevSibling = element.getPrevSibling();
            while (!LogtalkPsiUtil.getElementType(prevSibling).equals(type)) {
                prevSibling = prevSibling.getPrevSibling();
            }
            return prevSibling;
        }

        private PsiElement getNextSiblingWithType(PsiElement element, IElementType type) {
            PsiElement nextSibling = element.getNextSibling();
            while (!LogtalkPsiUtil.getElementType(nextSibling).equals(type)) {
                nextSibling = nextSibling.getNextSibling();
            }
            return nextSibling;
        }

        public Optional<OperationPart> previous() {
            if (isFirst()) {
                return Optional.empty();
            } else {
                PsiElement term;
                if (!isLast()) {
                    PsiElement operation = element.getParent();
                    term = operation.getParent();
                } else {
                    term = element.getParent();
                }
                return Optional.of(new OperationPart(getPrevSiblingWithType(term, LogtalkTypes.BASIC_TERM)));
            }
        }

        public Optional<OperationPart> next() {
            if (isLast()) {
                return Optional.empty();
            } else {
                PsiElement term = getNextSiblingWithType(element, LogtalkTypes.TERM);
                if (!isLastButOne()) {
                    PsiElement operation = term.getFirstChild();
                    try {
                        return Optional.of(new OperationPart(operation.getFirstChild()));
                    } catch(Exception e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    return Optional.of(new OperationPart(term.getFirstChild()));
                }
            }
        }

        public boolean hasPrevious() {
            return previous().isPresent();
        }

        public boolean hasNext() {
            return next().isPresent();
        }

        public OperationView operationView() {
            return extendedView.operationView();
        }

        @Override
        public String toString() {
            return element.getText();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (!(o instanceof OperationPart)) return false;
            final OperationPart that = (OperationPart) o;
            return Objects.equals(element, that.element);
        }

        @Override
        public int hashCode() {
            return Objects.hash(element);
        }
    }

}
