package it.gigaclood.alexa.quiz.model;

import java.util.List;

public class QuestionsDto {
	
	private List<QuestionDto> questions;

	public List<QuestionDto> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDto> questions) {
		this.questions = questions;
	}

	@Override
	public String toString() {
		return "QuestionsDto [questions=" + questions + "]";
	}
}
