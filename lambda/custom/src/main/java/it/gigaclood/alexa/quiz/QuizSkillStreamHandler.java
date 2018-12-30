package it.gigaclood.alexa.quiz;

import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

import it.gigaclood.alexa.quiz.handlers.AnswerIntentHandler;
import it.gigaclood.alexa.quiz.handlers.ExitSkillHandler;
import it.gigaclood.alexa.quiz.handlers.HelpIntentHandler;
import it.gigaclood.alexa.quiz.handlers.LaunchRequestHandler;
import it.gigaclood.alexa.quiz.handlers.NoAnswerIntentHandler;
import it.gigaclood.alexa.quiz.handlers.RepeatIntentHandler;
import it.gigaclood.alexa.quiz.handlers.SessionEndedHandler;

public class QuizSkillStreamHandler extends SkillStreamHandler {

    public QuizSkillStreamHandler() {
        super(Skills.standard()
                .addRequestHandlers(new LaunchRequestHandler(), 
//                					new QuizAndStartOverIntentHandler(),
                					new NoAnswerIntentHandler(),
                             new AnswerIntentHandler(), new RepeatIntentHandler(), new HelpIntentHandler(),
                             new ExitSkillHandler(), new SessionEndedHandler())
                .build());
    }

}
