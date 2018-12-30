package it.gigaclood.alexa.quiz.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import it.gigaclood.alexa.quiz.model.Attributes;
import it.gigaclood.alexa.quiz.model.Constants;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class)) 
                || input.matches(intentName("AMAZON.StartOverIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
//        sessionAttributes.put(Attributes.STATE_KEY, Attributes.START_STATE);
        sessionAttributes.put(Attributes.STATE_KEY, Attributes.QUIZ_STATE);
        sessionAttributes.put(Attributes.RESPONSE_KEY, "");
        sessionAttributes.put(Attributes.COUNTER_KEY, 0);
        sessionAttributes.put(Attributes.QUIZ_SCORE_KEY, 0);

        return input.getResponseBuilder()
                .withSpeech(Constants.WELCOME_MESSAGE)
                .withReprompt(Constants.HELP_MESSAGE)
                .withShouldEndSession(false)
                .build();
    }

    
    
//    function startGame(newGame, handlerInput) {
//    	  const requestAttributes = handlerInput.attributesManager.getRequestAttributes();
//    	  let speechOutput = newGame
//    	    ? requestAttributes.t('NEW_GAME_MESSAGE', requestAttributes.t('GAME_NAME'))
//    	      + requestAttributes.t('WELCOME_MESSAGE', GAME_LENGTH.toString())
//    	    : '';
//    	  const translatedQuestions = requestAttributes.t('QUESTIONS');
//    	  const gameQuestions = populateGameQuestions(translatedQuestions);
//    	  const correctAnswerIndex = Math.floor(Math.random() * (ANSWER_COUNT));
//
//    	  const roundAnswers = populateRoundAnswers(
//    	    gameQuestions,
//    	    0,
//    	    correctAnswerIndex,
//    	    translatedQuestions
//    	  );
//    	  const currentQuestionIndex = 0;
//    	  const spokenQuestion = Object.keys(translatedQuestions[gameQuestions[currentQuestionIndex]])[0];
//    	  let repromptText = requestAttributes.t('TELL_QUESTION_MESSAGE', '1', spokenQuestion);
//    	  for (let i = 0; i < ANSWER_COUNT; i += 1) {
//    	    repromptText += `${i + 1}. ${roundAnswers[i]}. `;
//    	  }
//
//    	  speechOutput += repromptText;
//    	  const sessionAttributes = {};
//
//    	  const translatedQuestion = translatedQuestions[gameQuestions[currentQuestionIndex]];
//
//    	  Object.assign(sessionAttributes, {
//    	    speechOutput: repromptText,
//    	    repromptText,
//    	    currentQuestionIndex,
//    	    correctAnswerIndex: correctAnswerIndex + 1,
//    	    questions: gameQuestions,
//    	    score: 0,
//    	    correctAnswerText: translatedQuestion[Object.keys(translatedQuestion)[0]][0]
//    	  });
//
//    	  handlerInput.attributesManager.setSessionAttributes(sessionAttributes);
//
//    	  return handlerInput.responseBuilder
//    	    .speak(speechOutput)
//    	    .reprompt(repromptText)
//    	    .withSimpleCard(requestAttributes.t('GAME_NAME'), repromptText)
//    	    .getResponse();
//    	}
}

