package org.logtalk.intellij;


import org.logtalk.intellij.psi.LogtalkFile;

import com.intellij.lang.Language;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;

public class LogtalkParserDefinition extends LogicParserDefinition {

    public static final IFileElementType FILE =
            new IFileElementType(Language.findInstance(LogtalkLanguage.class));

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new LogtalkFile(viewProvider);
    }
}
