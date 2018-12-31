package it.gigaclood.alexa.quiz.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.gigaclood.alexa.quiz.model.Attributes;
import it.gigaclood.alexa.quiz.model.Constants;
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

public class AnswerNumberIntentHandler implements RequestHandler {

    private static final Random RANDOM = new Random();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AnswerNumberIntent").and(sessionAttribute(Attributes.STATE_KEY, Attributes.QUIZ_STATE)));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();

        String responseText;
        String speechOutput;

        Map<String, String> quizItem = (LinkedHashMap<String, String>)sessionAttributes.get(Attributes.QUIZ_ITEM_KEY);
        State state = MAPPER.convertValue(quizItem, State.class);

        StateProperty stateProperty = StateProperty.valueOf((String) sessionAttributes.get(Attributes.QUIZ_PROPERTY_KEY));
        int counter = (int) sessionAttributes.get(Attributes.COUNTER_KEY);
        int quizScore = (int) sessionAttributes.get(Attributes.QUIZ_SCORE_KEY);

        IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        boolean correct = compareSlots(intentRequest.getIntent().getSlots(), getPropertyOfState(stateProperty, state));

        if (correct) {
            quizScore++;
            responseText = getSpeechCon(true);
            sessionAttributes.put(Attributes.QUIZ_SCORE_KEY, quizScore);
        } else {
            responseText = getSpeechCon(false);
        }

        responseText += getAnswerText(stateProperty, state);

        if (counter < 10) {
            responseText += "Your current score is " + quizScore + " out of " + counter + ". ";
            sessionAttributes.put(Attributes.RESPONSE_KEY, responseText);
            return QuestionUtils.generateQuestion(input);
        } else {
            responseText += "Your final score is " + quizScore + " out of " + counter + ". ";
            speechOutput = responseText + " " + Constants.EXIT_SKILL_MESSAGE;
            return input.getResponseBuilder()
                    .withSpeech(speechOutput)
                    .withShouldEndSession(true)
                    .build();
        }
        
//        
//        const { requestEnvelope, attributesManager, responseBuilder } = handlerInput;
//        const { intent } = requestEnvelope.request;
//
//        const answerSlotValid = isAnswerSlotValid(intent);
//
//        let speechOutput = '';
//        let speechOutputAnalysis = '';
//
//        const sessionAttributes = attributesManager.getSessionAttributes();
//        const gameQuestions = sessionAttributes.questions;
//        let correctAnswerIndex = parseInt(sessionAttributes.correctAnswerIndex, 10);
//        let currentScore = parseInt(sessionAttributes.score, 10);
//        let currentQuestionIndex = parseInt(sessionAttributes.currentQuestionIndex, 10);
//        const { correctAnswerText } = sessionAttributes;
//        const requestAttributes = attributesManager.getRequestAttributes();
//        const translatedQuestions = requestAttributes.t('QUESTIONS');
//
//
//        if (answerSlotValid
//          && parseInt(intent.slots.Answer.value, 10) === sessionAttributes.correctAnswerIndex) {
//          currentScore += 1;
//          speechOutputAnalysis = requestAttributes.t('ANSWER_CORRECT_MESSAGE');
//        } else {
//          if (!userGaveUp) {
//            speechOutputAnalysis = requestAttributes.t('ANSWER_WRONG_MESSAGE');
//          }
//
//          speechOutputAnalysis += requestAttributes.t(
//            'CORRECT_ANSWER_MESSAGE',
//            correctAnswerIndex,
//            correctAnswerText
//          );
    }

    private String getAnswerText(StateProperty stateProperty, State state) {
        switch(stateProperty) {
            case ABBREVIATION:
                return "The " + stateProperty.getValue() + " of " + state.getName() + " is <say-as interpret-as='spell-out'>" + getPropertyOfState(stateProperty, state) + "</say-as>. ";
            default:
                return "The " + stateProperty.getValue() + " of " + state.getName() + " is " + getPropertyOfState(stateProperty, state) + ". ";
        }
    }

    private String getSpeechCon(boolean correct) {
        if (correct) {
            return "<say-as interpret-as='interjection'>" + getRandomItem(Constants.CORRECT_RESPONSES) + "! </say-as><break strength='strong'/>";
        } else {
            return "<say-as interpret-as='interjection'>" + getRandomItem(Constants.INCORRECT_RESPONSES) + " </say-as><break strength='strong'/>";
        }
    }

    private <T> T getRandomItem(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }

    private boolean compareSlots(Map<String, Slot> slots, String correctAnswer) {
        for (Slot slot : slots.values()) {
            if (slot.getValue() != null && slot.getValue().toLowerCase().equals(correctAnswer.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
