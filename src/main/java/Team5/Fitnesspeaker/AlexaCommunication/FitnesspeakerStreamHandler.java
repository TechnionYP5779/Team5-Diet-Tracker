/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at
         http://aws.amazon.com/apache2.0/
     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package Team5.Fitnesspeaker.AlexaCommunication;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AddFoodIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CancelandStopIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.FallbackIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.HelpIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.LaunchRequestHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.SessionEndedRequestHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WhatIAteIntentHandler;

public class FitnesspeakerStreamHandler extends SkillStreamHandler {

	@SuppressWarnings("unchecked")
	private static Skill getSkill() {
		return Skills.standard()
				.addRequestHandlers(new WhatIAteIntentHandler(), new AddFoodIntentHandler(), new LaunchRequestHandler(),
						new CancelandStopIntentHandler(), new SessionEndedRequestHandler(), new HelpIntentHandler(),
						new FallbackIntentHandler())
				.withSkillId("amzn1.ask.skill.eb18e4d0-f135-4c9b-a227-18ea0a7bd0e9").build();
	}

	public FitnesspeakerStreamHandler() {
		super(getSkill());
	}

}