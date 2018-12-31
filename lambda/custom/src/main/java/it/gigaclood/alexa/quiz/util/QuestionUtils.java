package it.gigaclood.alexa.quiz.util;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import it.gigaclood.alexa.quiz.model.Attributes;
import it.gigaclood.alexa.quiz.model.State;
import it.gigaclood.alexa.quiz.model.StateProperty;


import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class QuestionUtils {

    private static final Random RANDOM = new Random();

   

    public static String getQuestionText(int counter, StateProperty stateProperty, State state) {
        return "Here is your " + counter + "th question.  What is the " + stateProperty.getValue() + " of "  + state.getName() + "?";
    }


    public static String getPropertyOfState(StateProperty stateProperty, State state) {
        switch (stateProperty) {
            case NAME:
                return state.getName();
            case ABBREVIATION:
                return state.getAbbreviation();
            case CAPITAL:
                return state.getCapital();
            case STATEHOOD_YEAR:
                return state.getStatehoodYear();
            case STATEHOOD_ORDER:
                return state.getStatehoodOrder();
        }
        throw new IllegalStateException("Invalid stateProperty");
    }


    private static StateProperty getRandomProperty() {
        return StateProperty.values()[RANDOM.nextInt(StateProperty.values().length -1) + 1];
    }

}
