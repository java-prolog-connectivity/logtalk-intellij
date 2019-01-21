package org.logtalk.intellij;


import com.intellij.lexer.FlexAdapter;

public class LogtalkLexerAdapter extends FlexAdapter {

    public LogtalkLexerAdapter() {
        super(new LogtalkLexer(null));
    }

}
