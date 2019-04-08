package Team5.Fitnesspeaker.AlexaCommunication;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

import Team5.Fitnesspeaker.AlexaCommunication.Handlers.*;

/** this class initializes the intents handlers
 * @author Shalev Kuba
 * @since 2018-12-07
 * */
public class FitnesspeakerStreamHandler extends SkillStreamHandler {
	static final String skillID = "amzn1.ask.skill.8204b920-c87b-453b-8bee-b10cd79945a8";

	@SuppressWarnings("unchecked")
	private static Skill getSkill() {
		return Skills.standard()
				.addRequestHandlers(new WhatIAteIntentHandler(), new AddFoodIntentHandler(), new LaunchRequestHandler(),
						new CancelandStopIntentHandler(), new SessionEndedRequestHandler(), new HelpIntentHandler(),
						new FallbackIntentHandler(), new AddDrinkIntent(), new HowManyIDrankIntent(), new AddAgeIntentHandler(),
						new HowOldIntentHandler(), new AddNameIntentHandler(),
						new AddWeightIntentHandler(), new AddHeightIntentHandler(), new HowMuchWeightIntentHandler(),
						new HowMuchHeightIntent(), new SendDailyEmailHandler(), new AddSmokeIntentHandler(), new HowMuchSmokedIntentHandler(),
            new HowMuchCaloriesIntentHandler(), new WeightProgressIntentHandler(), 
            new WeeklyMailReportHandler(),new AddBloodPressureIntentHandler(), new GetBloodPressureIntentHandler(),
            new BMIIntentHandler(), new SetGoalIntentHandler(), new GetGoalIntentHandler(),new SetCigarLimitIntentHandler(), new GetCigarLimitIntentHandler(),new AddWeightGoalIntentHandler(),new PeriodicWeightProgressHandler()).withSkillId(skillID).build();
	}

	public FitnesspeakerStreamHandler() {
		super(getSkill());
	}

}