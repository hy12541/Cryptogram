package edu.gatech.seclass.crypto6300;

import java.io.Serializable;

public class Cryptogram implements Serializable {
    private String title, solution, encodedPhrase, hint, creatorName;
    private Boolean isActive;
    private int numCompleted, numSolved;


    public Cryptogram(String cryptogramTitle, String cryptogramSolution,
                      String cryptogramEncodedPhrase, String cryptogramHint,
                      String cryptogramCreatorName) {
        title = cryptogramTitle;
        solution = cryptogramSolution;
        encodedPhrase = cryptogramEncodedPhrase;
        hint = cryptogramHint;
        creatorName = cryptogramCreatorName;
        isActive = true;
        numCompleted = 0;
        numSolved = 0;
    }

    public Cryptogram(String cryptogramTitle, String cryptogramSolution,
                      String cryptogramEncodedPhrase, String cryptogramHint,
                      String cryptogramCreatorName, Boolean cryptogramIsActive,
                      Integer cryptogramNumCompleted, Integer cryptogramNumSolved) {
        title = cryptogramTitle;
        solution = cryptogramSolution;
        encodedPhrase = cryptogramEncodedPhrase;
        hint = cryptogramHint;
        creatorName = cryptogramCreatorName;
        isActive = cryptogramIsActive;
        numCompleted = cryptogramNumCompleted;
        numSolved = cryptogramNumSolved;
    }

    public void setTitle(String cryptogramTitle) {
        title = cryptogramTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setSolution(String cryptogramSolution) {
        solution = cryptogramSolution;
    }

    public String getSolution() {
        return solution;
    }

    public void setEncodedPhrase(String cryptogramEncodedPhrase) {
        encodedPhrase = cryptogramEncodedPhrase;
    }

    public String getEncodedPhrase() {
        return encodedPhrase;
    }

    public void setHint(String cryptogramHint) {
        hint = cryptogramHint;
    }

    public String getHint() {
        return hint;
    }

    public void setCreatorName(String cryptogramCreatorName) {
        creatorName = cryptogramCreatorName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setIsActive(Boolean cryptogramIsActive) {
        isActive = cryptogramIsActive;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void updateNumCompleted(Integer i) {
        numCompleted += i;
    }

    public Integer getNumCompleted() {
        return numCompleted;
    }

    public void updateNumSolved(Integer i) {
        numSolved += i;
    }

    public Integer getNumSolved() {
        return numSolved;
    }
}
