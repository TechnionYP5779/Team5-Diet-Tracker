package Team5.Fitnesspeaker.AlexaCommunication;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

import Team5.Fitnesspeaker.AlexaCommunication.Handlers.*;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AgeHandlers.AddAgeIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AgeHandlers.HowOldIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers.AddSmokeIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers.GetCigarLimitIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers.HowMuchSmokedIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers.SetCigarLimitIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.DrinkHandlers.AddDrinkIntent;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.DrinkHandlers.HowManyIDrankIntent;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.EmailHandlers.SendDailyEmailHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.EmailHandlers.WeeklyMailReportHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.EmailHandlers.WeightProgressIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.FoodHandlers.AddFoodIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.FoodHandlers.HowMuchCaloriesIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.FoodHandlers.WhatIAteIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.GoalsHandlers.GetGoalIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.GoalsHandlers.SetGoalIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.AddHeightIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.AddWeightGoalIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.AddWeightIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.BMIIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.HowMuchHeightIntent;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.HowMuchWeightIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.PeriodicWeightProgressHandler;

/**
 * this class initializes the intents handlers
 * 
 * @author Shalev Kuba
 * @since 2018-12-07
 */
public class FitnesspeakerStreamHandler extends SkillStreamHandler {
	static final String skillID = "amzn1.ask.skill.8204b920-c87b-453b-8bee-b10cd79945a8";

	@SuppressWarnings("unchecked")
	private static Skill getSkill() {
		return Skills.standard()
				.addRequestHandlers(new WhatIAteIntentHandler(), new AddFoodIntentHandler(), new LaunchRequestHandler(),
						new CancelandStopIntentHandler(), new SessionEndedRequestHandler(), new HelpIntentHandler(),
						new FallbackIntentHandler(), new AddDrinkIntent(), new HowManyIDrankIntent(),
						new AddAgeIntentHandler(), new HowOldIntentHandler(), new AddWeightIntentHandler(),
						new AddHeightIntentHandler(), new HowMuchWeightIntentHandler(), new HowMuchHeightIntent(),
						new SendDailyEmailHandler(), new AddSmokeIntentHandler(), new HowMuchSmokedIntentHandler(),
						new HowMuchCaloriesIntentHandler(), new WeightProgressIntentHandler(),
						new WeeklyMailReportHandler(), new BMIIntentHandler(), new SetGoalIntentHandler(),
						new GetGoalIntentHandler(), new SetCigarLimitIntentHandler(), new GetCigarLimitIntentHandler(),
						new AddWeightGoalIntentHandler(), new PeriodicWeightProgressHandler())
				.withSkillId(skillID).build();
	}

	public FitnesspeakerStreamHandler() {
		super(getSkill());
	}

}