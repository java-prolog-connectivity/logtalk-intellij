package org.logtalk.parser

import com.intellij.psi.PsiElement
import org.logtalk.intellij.psi.decorator.OperationView
import org.logtalk.parser.StandAloneParser
import spock.lang.Specification

import static org.logtalk.intellij.psi.decorator.ExtendedPsiView.getExtendedPsiView
import static org.logtalk.parser.TestParserHelper.init

class OperatorParserSpec extends Specification {

    def "parse object directive"() {
        setup:
        PsiElement termNode = StandAloneParser.parseTerm(":- dynamic")
        PsiElement operationNode = termNode.getFirstChild()
        init(operationNode)

        when:
        OperationView operationView = getExtendedPsiView(operationNode.getFirstChild()).get().operationView()
        OperationView root = operationView.getRoot()

        then:
        root.getText() == ":-"
    }

    def "parse simple unification"() {
        setup:
        PsiElement termNode = StandAloneParser.parseTerm("A = x")
        PsiElement operationNode = termNode.getFirstChild()
        init(operationNode)

        when:
        OperationView operationView = getExtendedPsiView(operationNode.getFirstChild()).get().operationView()
        OperationView root = operationView.getRoot()

        then:
        root.getText() == "="
        root.leftArgument.get().getText() == "A"
        root.rightArgument.get().getText() == "x"
    }

    def "parse operator symbol used as atom"() {
        setup:
        PsiElement termNode = StandAloneParser.parseTerm("= = =")
        PsiElement operationNode = termNode.getFirstChild()
        init(operationNode)

        when:
        OperationView operationView = getExtendedPsiView(operationNode.getFirstChild()).get().operationView()
        OperationView root = operationView.getRoot()

        then:
        root.getText() == "="
        root.leftArgument.get().getText() == "="
        root.rightArgument.get().getText() == "="
    }

}
