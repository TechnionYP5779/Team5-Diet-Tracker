package Team5.Fitnesspeaker.AlexaCommunication;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

import Team5.Fitnesspeaker.AlexaCommunication.Handlers.*;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AgeHandlers.SetAgeIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AgeHandlers.GetAgeIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers.AddSmokeIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers.GetCigarLimitIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers.HowMuchSmokedIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers.SetCigarLimitIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.DrinkHandlers.AddDrinkIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.DrinkHandlers.HowManyDrinksIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.EmailHandlers.SendDailyMailIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.EmailHandlers.SendWeeklyMailIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.EmailHandlers.SendWeightProgressMailIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.FoodHandlers.AddFoodIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.FoodHandlers.HowMuchNutritionalValuesIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.FoodHandlers.WhatDidIEatIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.GoalsHandlers.GetGoalIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.GoalsHandlers.SetGoalIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.AddHeightIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.AddWeightGoalIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.AddWeightIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.BMIIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.GetHeightIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.GetWeightIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.PeriodicWeightProgressIntentHandler;

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
				.addRequestHandlers(new WhatDidIEatIntentHandler(), new AddFoodIntentHandler(), new LaunchRequestHandler(),
						new CancelandStopIntentHandler(), new SessionEndedRequestHandler(), new HelpIntentHandler(),
						new FallbackIntentHandler(), new AddDrinkIntentHandler(), new HowManyDrinksIntentHandler(),
						new SetAgeIntentHandler(), new GetAgeIntentHandler(), new AddWeightIntentHandler(),
						new AddHeightIntentHandler(), new GetWeightIntentHandler(), new GetHeightIntentHandler(),
						new SendDailyMailIntentHandler(), new AddSmokeIntentHandler(), new HowMuchSmokedIntentHandler(),
						new HowMuchNutritionalValuesIntentHandler(), new SendWeightProgressMailIntentHandler(),
						new SendWeeklyMailIntentHandler(), new BMIIntentHandler(), new SetGoalIntentHandler(),
						new GetGoalIntentHandler(), new SetCigarLimitIntentHandler(), new GetCigarLimitIntentHandler(),
						new AddWeightGoalIntentHandler(), new PeriodicWeightProgressIntentHandler())
				.withSkillId(skillID).build();
	}

	public FitnesspeakerStreamHandler() {
		super(getSkill());
	}

}