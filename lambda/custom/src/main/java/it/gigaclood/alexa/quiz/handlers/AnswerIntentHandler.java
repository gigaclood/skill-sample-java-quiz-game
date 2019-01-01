package it.gigaclood.alexa.quiz.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gigaclood.alexa.quiz.model.Attributes;
import it.gigaclood.alexa.quiz.model.Constants;
import it.gigaclood.alexa.quiz.model.QuestionDto;
import it.gigaclood.alexa.quiz.model.State;
import it.gigaclood.alexa.quiz.model.StateProperty;
import it.gigaclood.alexa.quiz.util.QuestionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;
import static it.gigaclood.alexa.quiz.util.QuestionUtils.getPropertyOfState;

public class AnswerIntentHandler implements RequestHandler {

    private static final Random RANDOM = new Random();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean canHandle(HandlerInput input) {
        return (input.matches(intentName("AnswerIntent")) || input.matches(intentName("DontKnowIntent")));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();

        String responseText;
        String speechOutputAnalysis;

        List<Map> gameQuestionsMap = (List<Map>) sessionAttributes.get(Attributes.GAME_QUESTIONS);
        List<QuestionDto> gameQuestions = new ObjectMapper().convertValue(gameQuestionsMap,new TypeReference<List<QuestionDto>>(){});
        Map questonDtoMap = (Map)sessionAttributes.get(Attributes.CURRENT_QUESTION);
		QuestionDto currentQuestion = new ObjectMapper().convertValue(questonDtoMap, QuestionDto.class);
		Integer currentQuestionIndex = (Integer)sessionAttributes.get(Attributes.CURRENT_QUESTION_INDEX);
        
		int answerIndex = isAnswerSlotValid(input);

		
		if (answerIndex>0 && answerIndex == currentQuestion.getCorrect())
		{
//		    currentScore += 1;
			speechOutputAnalysis = Constants.ANSWER_CORRECT_MESSAGE;
		} else {
		    if (!false) {
		      speechOutputAnalysis = Constants.ANSWER_WRONG_MESSAGE;
		    }

		    speechOutputAnalysis +=  Constants.CORRECT_ANSWER_MESSAGE+currentQuestion.getCorrect()+". "+
		    	currentQuestion.getAnswers().get(currentQuestion.getCorrect()-1);
		}
		int counter = (int) sessionAttributes.get(Attributes.COUNTER_KEY);
        int quizScore = (int) sessionAttributes.get(Attributes.QUIZ_SCORE_KEY);

        if (currentQuestionIndex == 1) {
            String speechOutput = Constants.ANSWER_IS_MESSAGE;
            speechOutput += speechOutputAnalysis + " Hai finito!";
            return input.getResponseBuilder()
                    .withSpeech(speechOutput)
                    .withShouldEndSession(true)
                    .build();
          }
        else {
        	currentQuestionIndex++;

    		StringBuilder speechQuestion = new StringBuilder(Constants.TELL_QUESTION_MESSAGE);
    		speechQuestion.append(currentQuestionIndex + 1).append(" ");
    		speechQuestion.append(gameQuestions.get(currentQuestionIndex).getQuestion());

    		int i = 1;
    		List<String> answers = gameQuestions.get(currentQuestionIndex).getAnswers();
    		for (String answer : answers) {
    			speechQuestion.append(i).append(". ").append(answer).append(". ");
    			i++;
    		}

    		String speechOutput = Constants.ANSWER_IS_MESSAGE;
            speechOutput += speechOutputAnalysis + " . "+speechQuestion;
            		
    		if (sessionAttributes != null) {
    			sessionAttributes.put(Attributes.CURRENT_QUESTION, gameQuestions.get(currentQuestionIndex));
    			sessionAttributes.put(Attributes.CURRENT_QUESTION_INDEX, currentQuestionIndex);
    		}
    		return input.getResponseBuilder()
                    .withSpeech(speechOutput)
                    .withShouldEndSession(false)
                    .build();
        	
        }

    }

    private int isAnswerSlotValid(HandlerInput input) {
        IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        if (
        		intentRequest!=null &&
        		intentRequest.getIntent()!=null &&
        		intentRequest.getIntent().getSlots()!=null &&
        		intentRequest.getIntent().getSlots().get("Answer") !=null &&
        		intentRequest.getIntent().getSlots().get("Answer").getValue()!=null
        	)
        {
        	int answerInt = Integer.parseInt(intentRequest.getIntent().getSlots().get("Answer").getValue());
        		
        	if(answerInt>0 && answerInt<7)
        		return answerInt;
        	
    	}   
    	return -1;
    }
    	
}
