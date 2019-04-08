package Team5.Fitnesspeaker.AlexaCommunication;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

import Team5.Fitnesspeaker.AlexaCommunication.Handlers.HowMuchCaloriesIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.HowMuchHeightIntent;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.HowMuchSmokedIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.HowMuchWeightIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AddWeightIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.BMIIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AddHeightIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WhatIsMyNameIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AddNameIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AddSmokeIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AddWeightGoalIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.HowOldIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AddAgeIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AddBloodPressureIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AddDrinkIntent;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AddFoodIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CancelandStopIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.FallbackIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.GetCigarLimitIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.GetGoalIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.GetBloodPressureIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.HelpIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.HowManyIDrankIntent;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.LaunchRequestHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.PeriodicWeightProgressHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.SendDailyEmailHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.SessionEndedRequestHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.SetCigarLimitIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.SetGoalIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeeklyMailReportHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightProgressIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WhatIAteIntentHandler;

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
						new HowOldIntentHandler(), new WhatIsMyNameIntentHandler(), new AddNameIntentHandler(),
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