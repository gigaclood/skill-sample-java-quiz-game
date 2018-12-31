package it.gigaclood.alexa.quiz.handlers;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gigaclood.alexa.quiz.model.Attributes;
import it.gigaclood.alexa.quiz.model.Constants;
import it.gigaclood.alexa.quiz.model.QuestionDto;
import it.gigaclood.alexa.quiz.model.QuestionsDto;

public class LaunchRequestHandler implements RequestHandler {


	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(requestType(LaunchRequest.class)) || input.matches(intentName("AMAZON.StartOverIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
		sessionAttributes.put(Attributes.STATE_KEY, Attributes.QUIZ_STATE);
		sessionAttributes.put(Attributes.RESPONSE_KEY, "");
		sessionAttributes.put(Attributes.COUNTER_KEY, 0);
		sessionAttributes.put(Attributes.QUIZ_SCORE_KEY, 0);

		String speechOutput=startGame(true, sessionAttributes);
		
		return input.getResponseBuilder().withSpeech(speechOutput).withReprompt(Constants.HELP_MESSAGE)
				.withShouldEndSession(false).build();
	}

	private String startGame(boolean newGame, Map<String, Object> sessionAttributes) {
		//Logger log = Logger.getLogger("startGame");
		String speechOutput = newGame ? Constants.NEW_GAME_MESSAGE + Constants.WELCOME_MESSAGE : "";

		QuestionsDto questions = null;
		ObjectMapper obj = new ObjectMapper();
		try {
			questions = obj.readValue(this.getClass().getClassLoader().getResourceAsStream("questions.json"),
					QuestionsDto.class);
		} catch (IOException e) {
			//log.log(Level.SEVERE, "Error reading questions", e);
			e.printStackTrace();
		}

		List<QuestionDto> gameQuestions = populateGameQuestions(questions);
		
		int currentQuestionIndex = 0;
		
		StringBuilder speechQuestion = new StringBuilder(Constants.TELL_QUESTION_MESSAGE);
		speechQuestion.append(currentQuestionIndex+1);
		speechQuestion.append(gameQuestions.get(currentQuestionIndex).getQuestion());
		
		int i = 1;
		List<String> answers = gameQuestions.get(currentQuestionIndex).getAnswers();
		for (String answer : answers) {
			speechQuestion.append(i).append(". ").append(answer).append(". ");
		}
		
		speechOutput += speechQuestion.toString();
		
		sessionAttributes.put(Attributes.GAME_QUESTIONS, gameQuestions);
		sessionAttributes.put(Attributes.CURRENT_QUESTION,gameQuestions.get(currentQuestionIndex));
		sessionAttributes.put(Attributes.CURRENT_QUESTION_INDEX,currentQuestionIndex);
		
		return speechOutput;

	}

	private List<QuestionDto> populateGameQuestions(QuestionsDto questions) {
		List<QuestionDto> gameQuestions = new ArrayList<QuestionDto>();
		// TODO extract random questions and shuffle answers
		gameQuestions = questions.getQuestions();
		return gameQuestions;
	}

	public static void main(String[] args) {
		LaunchRequestHandler l = new LaunchRequestHandler();
		l.startGame(true, null);
	}
	
	
	
}
