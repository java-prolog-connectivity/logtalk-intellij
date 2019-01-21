package org.logtalk.parser.operator

import org.logtalk.parser.operator.OperatorSet
import spock.lang.Specification

import static org.logtalk.parser.operator.Operator.op
import static org.logtalk.parser.operator.OperatorType.*

class OperatorSetSpec extends Specification {

    def opSet = new OperatorSet()

    def "add existing operator"() {
        when:
        opSet.add(op(1000, XFY, ","))
        then:
        assert opSet.contains(",")
        1 == opSet.getOperatorFamily(",").get().size()

        when:
        opSet.add(op(1000, FX, ","))
        then:
        2 == opSet.getOperatorFamily(",").get().size()

        when:
        opSet.add(op(1000, FY, ","))
        then:
        2 == opSet.getOperatorFamily(",").get().size()

        when:
        opSet.add(op(0, FX, ","))
        then:
        1 == opSet.getOperatorFamily(",").get().size()

        when:
        opSet.add(op(0, YFX, ","))
        then:
        !opSet.getOperatorFamily(",").isPresent()
    }

    def "add operator with zero precedence"() {
        when:
        opSet.add(op(0, XFY, ","))
        then:
        !opSet.getOperatorFamily(",").isPresent()
    }
}
