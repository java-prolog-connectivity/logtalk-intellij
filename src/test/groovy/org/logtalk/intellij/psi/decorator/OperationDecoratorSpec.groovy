package org.logtalk.intellij.psi.decorator

import com.intellij.psi.PsiElement
import org.logtalk.parser.StandAloneParser
import spock.lang.Specification

import static org.logtalk.intellij.psi.decorator.OperationDecorator.operationDecorator

class OperationDecoratorSpec extends Specification {

    def "operation decomposition"() {
        setup:
        PsiElement termNode = StandAloneParser.parseTerm("val1+val2=val3")
        PsiElement operationNode = termNode.getFirstChild()
        when:
        OperationDecorator.OperationPart val1Element =
                new OperationDecorator.OperationPart(operationNode.getFirstChild())
        OperationDecorator.OperationPart plusElement = val1Element.next().get()
        OperationDecorator.OperationPart val2Element = plusElement.next().get()
        OperationDecorator.OperationPart equalsElement = val2Element.next().get()
        OperationDecorator.OperationPart val3Element = equalsElement.next().get()

        then:
        !val1Element.hasPrevious()
        val1Element.hasNext()
        val1Element.isFirst()
        !val1Element.isLast()
        val1Element.toString().equals("val1")

        plusElement.hasPrevious()
        plusElement.hasNext()
        !plusElement.isFirst()
        !plusElement.isLast()
        plusElement.toString().equals("+")

        val2Element.hasPrevious()
        val2Element.hasNext()
        !val2Element.isFirst()
        !val2Element.isLast()
        val2Element.toString().equals("val2")

        equalsElement.hasPrevious()
        equalsElement.hasNext()
        !equalsElement.isFirst()
        !equalsElement.isLast()
        equalsElement.toString().equals("=")

        val3Element.hasPrevious()
        !val3Element.hasNext()
        !val3Element.isFirst()
        val3Element.isLast()
        val3Element.toString().equals("val3")
    }


    def "iterator"() {
        setup:
        PsiElement termNode = StandAloneParser.parseTerm("val1+val2=val3")
        PsiElement operationNode = termNode.getFirstChild()
        when:
        Iterator<OperationDecorator.OperationPart> it = operationDecorator(operationNode).iterator()
        OperationDecorator.OperationPart val1Element =
                new OperationDecorator.OperationPart(operationNode.getFirstChild())
        OperationDecorator.OperationPart plusElement = val1Element.next().get()
        OperationDecorator.OperationPart val2Element = plusElement.next().get()
        OperationDecorator.OperationPart equalsElement = val2Element.next().get()
        OperationDecorator.OperationPart val3Element = equalsElement.next().get()

        then:
        it.next() == val1Element
        it.next() == plusElement
        it.next() == val2Element
        it.next() == equalsElement
        it.next() == val3Element
    }

}
