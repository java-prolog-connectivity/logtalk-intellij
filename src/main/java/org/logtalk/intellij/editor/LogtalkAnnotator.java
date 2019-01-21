package org.logtalk.intellij.editor;

import static org.logtalk.intellij.psi.decorator.ExtendedPsiView.sentencePsiView;
import static org.logtalk.intellij.psi.decorator.SentenceDecorator.isSentence;

import java.util.Optional;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.logtalk.intellij.psi.decorator.ExtendedPsiView;
import org.logtalk.intellij.psi.helper.SentenceCorrectness;

public class LogtalkAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (isSentence(element)) {
            sentencePsiView(element);
        } else {
            ExtendedPsiView extendedView = new ExtendedPsiView(element);
            if (extendedView.operationView().getSentenceCorrectness().isOk()) {
                extendedView.init();
                highlightTokens(element, holder, new LogtalkSyntaxHighlighter());
            }
        }
    }

    private static void highlightTokens(PsiElement element, AnnotationHolder holder, LogtalkSyntaxHighlighter highlighter) {
        Optional<ExtendedPsiView> extendedViewOpt = ExtendedPsiView.getExtendedPsiView(element);
        extendedViewOpt.ifPresent(extendedView -> {
            if (extendedView.operationView().isSyntaxError()) {
                Lexer lexer = highlighter.getHighlightingLexer();
                int start = lexer.getTokenStart() + element.getTextRange().getStartOffset();
                int end = lexer.getTokenEnd() + element.getTextRange().getStartOffset();
                TextRange textRange = new TextRange(start, end);
                Annotation annotation = holder.createErrorAnnotation(textRange, extendedView.toString());
                //maybe text attributes are needed here ?
            } else {
                TextAttributesKey[] keys = highlighter.getTokenHighlights(extendedView);
                if (keys.length > 0) {
                    Annotation annotation = holder.createInfoAnnotation(element.getNode(), extendedView.toString());
                    for (TextAttributesKey key : keys) {
                        TextAttributes attributes = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(key);
                        annotation.setEnforcedTextAttributes(attributes);
                    }
                }
            }
        });
    }



/*    private static void highlightTokens(final Property property, final ASTNode node, final AnnotationHolder holder, PropertiesHighlighter highlighter) {
        Lexer lexer = highlighter.getHighlightingLexer();
        final String s = node.getText();
        lexer.start(s);

        while (lexer.getTokenType() != null) {
            IElementType elementType = lexer.getTokenType();
            TextAttributesKey[] keys = highlighter.getTokenHighlights(elementType);
            for (TextAttributesKey key : keys) {
                Pair<String,HighlightSeverity> pair = PropertiesHighlighter.DISPLAY_NAMES.get(key);
                String displayName = pair.getFirst();
                HighlightSeverity severity = pair.getSecond();
                if (severity != null) {
                    int start = lexer.getTokenStart() + node.getTextRange().getStartOffset();
                    int end = lexer.getTokenEnd() + node.getTextRange().getStartOffset();
                    TextRange textRange = new TextRange(start, end);
                    final Annotation annotation;
                    if (severity == HighlightSeverity.WARNING) {
                        annotation = holder.createWarningAnnotation(textRange, displayName);
                    }
                    else if (severity == HighlightSeverity.ERROR) {
                        annotation = holder.createErrorAnnotation(textRange, displayName);
                    }
                    else {
                        annotation = holder.createInfoAnnotation(textRange, displayName);
                    }
                    TextAttributes attributes = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(key);
                    annotation.setEnforcedTextAttributes(attributes);
                    if (key == PropertiesHighlighter.PROPERTIES_INVALID_STRING_ESCAPE) {
                        annotation.registerFix(new IntentionAction() {
                            @NotNull
                            public String getText() {
                                return PropertiesBundle.message("unescape");
                            }

                            @NotNull
                            public String getFamilyName() {
                                return getText();
                            }

                            public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
                                if (!property.isValid() || !property.getManager().isInProject(property)) return false;

                                String text = property.getPropertiesFile().getContainingFile().getText();
                                int startOffset = annotation.getStartOffset();
                                return text.length() > startOffset && text.charAt(startOffset) == '\\';
                            }

                            public void invoke(@NotNull Project project, Editor editor, PsiFile file) {
                                int offset = annotation.getStartOffset();
                                if (property.getPropertiesFile().getContainingFile().getText().charAt(offset) == '\\') {
                                    editor.getDocument().deleteString(offset, offset+1);
                                }
                            }

                            public boolean startInWriteAction() {
                                return true;
                            }
                        });
                    }
                }
            }
            lexer.advance();
        }
    }*/





}
