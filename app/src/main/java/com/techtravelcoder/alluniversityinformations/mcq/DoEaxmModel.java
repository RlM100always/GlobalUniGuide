package com.techtravelcoder.alluniversityinformations.mcq;

public class DoEaxmModel {
    String explanation, optionA, optionB, optionC, optionD, title, rightAnswer, mcqKey;
    String studentAns; // Add this field to store the student's selected answer
    boolean isAnswered; // Add this field to indicate if the question has been answered

    public DoEaxmModel() {
        this.studentAns = "";
        this.isAnswered = false;
    }

    // Getters and Setters for all fields

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getMcqKey() {
        return mcqKey;
    }

    public void setMcqKey(String mcqKey) {
        this.mcqKey = mcqKey;
    }

    public String getStudentAns() {
        return studentAns;
    }

    public void setStudentAns(String studentAns) {
        this.studentAns = studentAns;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }
}
