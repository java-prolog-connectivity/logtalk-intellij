package org.logtalk.parser

import com.intellij.psi.PsiElement
import org.logtalk.intellij.psi.decorator.OperationDecorator
import org.logtalk.intellij.psi.helper.SentenceCorrectness

import static org.logtalk.intellij.psi.decorator.OperationDecorator.operationDecorator

class TestParserHelper {

    static void init(PsiElement operationNode) {
        SentenceCorrectness correctness = new SentenceCorrectness()
        for (OperationDecorator.OperationPart part : (Iterable<OperationDecorator.OperationPart>) { -> operationDecorator(operationNode).iterator()}) {
            part.operationView().setSentenceCorrectness(correctness)
        }
        operationDecorator(operationNode).init()
    }
}
