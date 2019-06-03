package Team5.Fitnesspeaker.AlexaCommunication;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

import Team5.Fitnesspeaker.AlexaCommunication.Handlers.*;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.AgeHandlers.*;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CigarettesHandlers.*;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CustomMealHandlers.AddCustomMealIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CustomMealHandlers.AddIngredientToMealIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CustomMealHandlers.EatCustomMealIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.CustomMealHandlers.RemoveCustomMealIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.DrinkHandlers.*;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.DrinkHandlers.HowManyDrinksIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.EmailHandlers.*;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.FoodHandlers.*;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.FoodHandlers.AddFoodIntentHandler;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.GoalsHandlers.*;
import Team5.Fitnesspeaker.AlexaCommunication.Handlers.WeightHeightBMIHandlers.*;

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
				.addRequestHandlers(new WhatDidIEatIntentHandler(), new AddFoodIntentHandler(),
						new LaunchRequestHandler(), new CancelandStopIntentHandler(), new SessionEndedRequestHandler(),
						new HelpIntentHandler(), new FallbackIntentHandler(), new AddDrinkIntentHandler(),
						new HowManyDrinksIntentHandler(), new SetAgeIntentHandler(), new GetAgeIntentHandler(),
						new AddWeightIntentHandler(), new AddHeightIntentHandler(), new GetWeightIntentHandler(),
						new GetHeightIntentHandler(), new SendDailyMailIntentHandler(), new AddSmokeIntentHandler(),
						new HowMuchSmokedIntentHandler(), new HowMuchNutritionalValuesIntentHandler(),
						new SendWeightProgressMailIntentHandler(), new SendWeeklyMailIntentHandler(),
						new BMIIntentHandler(), new SetGoalIntentHandler(), new GetGoalIntentHandler(),
						new SetCigarLimitIntentHandler(), new GetCigarLimitIntentHandler(),
						new AddWeightGoalIntentHandler(), new PeriodicWeightProgressIntentHandler(),
						new AddCustomMealIntentHandler(), new RemoveCustomMealIntentHandler(),
						new AddIngredientToMealIntentHandler(), new EatCustomMealIntentHandler())
				.withSkillId(skillID).build();
	}

	public FitnesspeakerStreamHandler() {
		super(getSkill());
	}

}