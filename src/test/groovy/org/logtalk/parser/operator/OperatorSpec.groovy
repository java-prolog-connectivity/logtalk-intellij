package org.logtalk.parser.operator

import spock.lang.Specification

import static org.logtalk.parser.operator.Operator.op
import static org.logtalk.parser.operator.OperatorType.FY

class OperatorSpec extends Specification {

    def "operator toString produces the command needed to define the operator in prolog"() {
        expect:
        "op(100, fy, '@')" == op(100, FY, "@").toString()
    }

}
