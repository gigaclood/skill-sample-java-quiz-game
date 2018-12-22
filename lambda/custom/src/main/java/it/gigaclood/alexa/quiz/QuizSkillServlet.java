/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package it.gigaclood.alexa.quiz;

import java.util.logging.Logger;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.servlet.SkillServlet;

import it.gigaclood.alexa.quiz.handlers.AnswerIntentHandler;
import it.gigaclood.alexa.quiz.handlers.ExitSkillHandler;
import it.gigaclood.alexa.quiz.handlers.HelpIntentHandler;
import it.gigaclood.alexa.quiz.handlers.LaunchRequestHandler;
import it.gigaclood.alexa.quiz.handlers.NoAnswerIntentHandler;
import it.gigaclood.alexa.quiz.handlers.QuizAndStartOverIntentHandler;
import it.gigaclood.alexa.quiz.handlers.RepeatIntentHandler;
import it.gigaclood.alexa.quiz.handlers.SessionEndedHandler;

public class QuizSkillServlet extends SkillServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuizSkillServlet() {
		super(getSkill());
	}

	@SuppressWarnings("unchecked")
	private static Skill getSkill() {
		Logger log = Logger.getLogger("CSS");
		log.info("getSkill");
		return Skills.standard()
				.addRequestHandlers(
						new LaunchRequestHandler(), 
						new QuizAndStartOverIntentHandler(),
						new NoAnswerIntentHandler(), 
						new AnswerIntentHandler(), 
						new RepeatIntentHandler(),
						new HelpIntentHandler(), 
						new ExitSkillHandler(), 
						new SessionEndedHandler())
				// Add your skill id below
				.withSkillId("amzn1.ask.skill.a2579df0-30da-49c6-a9cb-095b818e724a").build();
	}

}
