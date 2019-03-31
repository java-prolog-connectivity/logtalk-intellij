package org.logtalk.parser

import com.intellij.psi.PsiElement
import org.logtalk.intellij.psi.decorator.OperationView
import spock.lang.Specification

import static org.logtalk.intellij.psi.decorator.ExtendedPsiView.getExtendedPsiView
import static org.logtalk.parser.TestParserHelper.parseOperation

class OperatorParserSpec extends Specification {

    def "parse object directive"() {
        setup:
        PsiElement operationNode = parseOperation(":- dynamic")

        when:
        OperationView operationView = getExtendedPsiView(operationNode.getFirstChild()).get().operationView()
        OperationView root = operationView.getRoot()

        then:
        root.getText() == ":-"
    }

    def "parse simple unification"() {
        setup:
        PsiElement operationNode = parseOperation("A = x")

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
        PsiElement operationNode = parseOperation("= = =")

        when:
        OperationView operationView = getExtendedPsiView(operationNode.getFirstChild()).get().operationView()
        OperationView root = operationView.getRoot()

        then:
        root.getText() == "="
        root.leftArgument.get().getText() == "="
        root.rightArgument.get().getText() == "="
    }

    //A is 2 -1
    def "parse binary operator also defined as unary"() {
        setup:
        PsiElement operationNode = parseOperation("2 - 1")

        when:
        OperationView operationView = getExtendedPsiView(operationNode.getFirstChild()).get().operationView()
        OperationView root = operationView.getRoot()

        then:
        root.getText() == "-"
        root.leftArgument.get().getText() == "2"
        root.rightArgument.get().getText() == "1"
    }

    def "parse binary operator also defined as unary and no spaces present"() {
        setup:
        PsiElement operationNode = parseOperation("2-1")

        when:
        OperationView operationView = getExtendedPsiView(operationNode.getFirstChild()).get().operationView()
        OperationView root = operationView.getRoot()

        then:
        root.getText() == "-"
        root.leftArgument.get().getText() == "2"
        root.rightArgument.get().getText() == "1"
    }
}
