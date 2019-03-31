package org.logtalk.parser;


import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.intellij.grammar.LightPsi;
import org.logtalk.intellij.LogtalkParserDefinition;

/**
 * https://jitpack.io/p/jetbrains/Grammar-Kit
 * https://github.com/JetBrains/Grammar-Kit
 * https://github.com/JetBrains/Grammar-Kit/blob/master/HOWTO.md
 * https://github.com/JetBrains/Grammar-Kit/blob/master/TUTORIAL.md
 * https://intellij-support.jetbrains.com/hc/en-us/community/posts/205973424-Standalone-Parser-Dependencies
 * https://intellij-support.jetbrains.com/hc/en-us/community/posts/203374950-Use-Grammar-Kit-parser-standalone
 *
 * Examples Stand-alone parsers:
 * - https://github.com/intellij-rust/intellij-rust/blob/master/build.gradle.kts
 * - https://github.com/minecraft-dev/MinecraftDev/blob/dev/build.gradle.kts
 *
 *
 * Prolog operators:
 * http://www.swi-prolog.org/pldoc/man?section=operators
 */
public class StandAloneParser {

    public static final ParserDefinition PARSER_DEFINITION = new LogtalkParserDefinition();

    private static final String sentence(String fileContent) {
        return fileContent + " ."; //the space is needed since the last symbol may be a symbolic atom.
    }

    public static PsiElement parseAtom(String atom) {
        return parseBasicTerm(atom).getFirstChild();
    }

    private static PsiElement parseBasicTerm(String basicTerm) {
        return parseTerm(basicTerm).getFirstChild();
    }

    public static PsiElement parseTerm(String term) {
        return parseSentence(term).getFirstChild();
    }

    public static PsiElement parseSentence(String sentenceTerm) {
        PsiFile psiFile = parseFileContent(sentence(sentenceTerm));
        return psiFile.getFirstChild();
    }

    public static PsiFile parseFileContent(String fileContent) {
        return parseFile(ParserUtil.fileWithContent(fileContent));
    }

    public static PsiFile parseFile(File file) {
        try {
            return LightPsi.parseFile(file, PARSER_DEFINITION);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static ASTNode parse(String text) {
        return LightPsi.parseText(text, PARSER_DEFINITION);
    }

}
