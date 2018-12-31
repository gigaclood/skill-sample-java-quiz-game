package it.gigaclood.alexa.quiz.model;

import java.util.List;

public class QuestionDto {

	
	private String question;
	private List<String> answers;
	private int correct;
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public List<String> getAnswers() {
		return answers;
	}
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}
	@Override
	public String toString() {
		return "QuestionDto [question=" + question + ", answers=" + answers + ", correct=" + correct + "]";
	}
	public int getCorrect() {
		return correct;
	}
	public void setCorrect(int correct) {
		this.correct = correct;
	}
}
