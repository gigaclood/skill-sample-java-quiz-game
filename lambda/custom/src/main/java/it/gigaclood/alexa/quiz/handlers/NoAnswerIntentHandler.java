package it.gigaclood.alexa.quiz.handlers;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import it.gigaclood.alexa.quiz.model.Attributes;
import it.gigaclood.alexa.quiz.model.Constants;
import it.gigaclood.alexa.quiz.model.State;

public class NoAnswerIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AnswerIntent").and(sessionAttribute(Attributes.STATE_KEY, Attributes.QUIZ_STATE).negate()));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        sessionAttributes.put(Attributes.STATE_KEY, Attributes.START_STATE);

        IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
       
            String unknownAnswerText = "I'm sorry. That is not something I know very much about in this skill. " + Constants.HELP_MESSAGE;
            return input.getResponseBuilder()
                    .withSpeech(unknownAnswerText)
                    .withReprompt(unknownAnswerText)
                    .withShouldEndSession(false)
                    .build();

    }

    private String getTextDescription(State state) {
        return "Abbreviation: " + state.getAbbreviation() + "\n"
                + "Capital: " + state.getCapital() + "\n"
                + "Statehood Year: " + state.getStatehoodYear() + "\n"
                + "Statehood Order: " + state.getStatehoodOrder();
    }

    private String getSpeechDescription(State state) {
        return state.getName() + " is the " + state.getStatehoodOrder() + "th state, admitted to the Union in "
                + state.getStatehoodYear() + ".  The capital of " + state.getStatehoodOrder() + " is " + state.getCapital()
                + ", and the abbreviation for " + state.getName() + " is <break strength='strong'/><say-as interpret-as='spell-out'>"
                + state.getAbbreviation() + "</say-as>.  I've added " + state.getName() + " to your Alexa app.  Which other state or capital would you like to know about?";
    }

    private String getSmallImage(State state) {
        return "https://m.media-amazon.com/images/G/01/mobile-apps/dex/alexa/alexa-skills-kit/tutorials/quiz-game/state_flag/720x400/" + state.getAbbreviation() + "._TTH_.png";
    }

    private String getLargeImage(State state) {
        return "https://m.media-amazon.com/images/G/01/mobile-apps/dex/alexa/alexa-skills-kit/tutorials/quiz-game/state_flag/1200x800/" + state.getAbbreviation() + "._TTH_.png";
    }

}
