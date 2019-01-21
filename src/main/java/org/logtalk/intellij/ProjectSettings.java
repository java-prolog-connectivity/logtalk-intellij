package org.logtalk.intellij;

import org.logtalk.parser.operator.OperatorSet;

/**
 * The operators configured for a given project.
 */
public class ProjectSettings {

    //the current implementation just returns the default operators.
    public static OperatorSet getOperators() {
        return OperatorSet.getDefault();
    }

}
