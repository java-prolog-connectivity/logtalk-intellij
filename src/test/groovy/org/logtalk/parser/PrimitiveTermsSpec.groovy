package org.logtalk.parser

import com.intellij.psi.PsiElement
import org.logtalk.intellij.psi.decorator.AtomDecorator
import org.logtalk.intellij.psi.decorator.CompoundDecorator
import spock.lang.Specification

import static org.logtalk.intellij.psi.decorator.AtomDecorator.atomDecorator
import static org.logtalk.intellij.psi.decorator.CompoundDecorator.compoundDecorator
import static org.logtalk.intellij.psi.decorator.SequenceDecorator.isSequence
import static org.logtalk.parser.StandAloneParser.parseTerm
import static org.logtalk.parser.TestParserHelper.parseOperation

class PrimitiveTermsSpec extends Specification {

    def "parse quoted and unquoted atoms"() {
        when:
        AtomDecorator unquotedAtom = atomDecorator(StandAloneParser.parseAtom("x"))
        AtomDecorator quotedAtom = atomDecorator(StandAloneParser.parseAtom("'x'"))
        then:
        !unquotedAtom.isQuoted()
        quotedAtom.isQuoted()
    }

    def "parse atoms witn underscore"() {
        when:
        AtomDecorator atom = atomDecorator(StandAloneParser.parseAtom("x_y"))
        then:
        atom.getText() == "x_y"
    }

    def "parse sequence"() {
        expect:
        !isSequence(parseTerm("a"))
        !isSequence(parseOperation("a + b"))
        isSequence(parseOperation("a, b"))
        isSequence(parseOperation("a, B, _"))
        isSequence(parseOperation("a(x), b(x), c(x)"))
    }

    def "parse compound with one argument"() {
        setup:
        PsiElement term = parseTerm("a(b)")

        when:
        CompoundDecorator compound = compoundDecorator(term.getFirstChild().getFirstChild());
        Iterator<PsiElement> args = compound.getArgs()

        then:
        compound.getName().getText() == "a"
        args.next().getText() == "b"
    }

    def "parse compound with multiple arguments"() {
        setup:
        PsiElement term = parseTerm("a(b, c)")

        when:
        CompoundDecorator compound = compoundDecorator(term.getFirstChild().getFirstChild());
        Iterator<PsiElement> args = compound.getArgs()

        then:
        compound.getName().getText() == "a"
        args.next().getText() == "b"
        args.next().getText() == "c"
    }

    def "parse compound with symbolic name"() {
        setup:
        PsiElement term = parseTerm("=..(b, c)")

        when:
        CompoundDecorator compound = compoundDecorator(term.getFirstChild().getFirstChild());
        Iterator<PsiElement> args = compound.getArgs()

        then:
        compound.getName().getText() == "=.."
        args.next().getText() == "b"
        args.next().getText() == "c"
    }

}
