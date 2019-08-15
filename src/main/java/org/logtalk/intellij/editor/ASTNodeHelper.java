package org.logtalk.intellij.editor;

import org.logtalk.intellij.psi.LogtalkTypes;

import com.intellij.lang.ASTNode;


public class ASTNodeHelper {

    public static boolean isComment(ASTNode node) {
        return node.getElementType() == LogtalkTypes.COMMENT;
    }

}
