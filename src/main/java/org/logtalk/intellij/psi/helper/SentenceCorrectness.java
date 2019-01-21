package org.logtalk.intellij.psi.helper;

public class SentenceCorrectness {

    private boolean correct = true;

    public void flagError() {
        correct = false;
    }

    public boolean isOk() {
        return correct;
    }

}
