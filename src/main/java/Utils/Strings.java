package Utils;

public class Strings {
	public static final String GLOBAL_SESSION_NAME = "FitnessSpeakerSession";

	public static class IntentsNames {
		public static final String ADD_AGE_INTENT = "AddAgeIntent";
		public static final String ADD_BLOOD_PRESSURE_INTENT = "AddBloodPressureIntent";
		public static final String ADD_DRINK_INTENT = "addDrinkIntent";
		public static final String ADD_FOOD_INTENT = "AddFoodIntent";
		public static final String ADD_HEIGHT_INTENT = "AddHeightIntent";
		public static final String ADD_NAME_INTENT = "AddNameIntent";
		public static final String ADD_SMOKE_INTENT = "AddSmokeIntent";
		public static final String ADD_WEIGHT_INTENT = "AddWeightIntent";
		public static final String BMI_INTENT = "BMIIntent";
		public static final String CAN_I_DRIVE_INTENT = "CanIDriveIntent";
		public static final String CIGAR_LIMIT_INTENT = "SetCigarLimitIntent";
		public static final String DAILY_MAIL_INTENT = "SendDailyMailIntent";
		public static final String DRINK_ALCOHOL_INTENT = "DrinkAlchohol";
		public static final String FALL_BACK_INTENT = "AMAZON.FallbackIntent";
		public static final String GET_BLOOD_PRESSURE_INTENT = "GetBloodPressureIntent";
		public static final String GET_CIGAR_LIMIT_INTENT = "GetCigarLimitIntent";
		public static final String GET_GOAL_INTENT = "GetGoalIntent";
		public static final String HELP_INTENT = "AMAZON.HelpIntent";
		public static final String HOW_MUCH_DRANK_INTENT = "HowMuchIDrankIntent";
		public static final String HOW_MUCH_HEIGHT_INTENT = "HowMuchHeightIntent";
		public static final String HOW_MUCH_MEASURE_INTENT = "HowMuchMeasureIntent";
		public static final String HOW_MUCH_SMOKE_INTENT = "HowMuchSmokedIntent";
		public static final String HOW_MUCH_WEIGHT_INTENT = "HowMuchWeightIntent";
		public static final String HOW_OLD_INTENT = "HowOldIntent";
		public static final String SET_GOAL_INTENT = "SetGoalIntent";
		public static final String WEEKLY_MAIL_INTENT = "WeeklyMailReport";
		public static final String WEIGHT_PROGRESS_INTENT = "weightProgressIntent";
		public static final String WHAT_I_ATE_INTENT = "WhatIAteIntent";
		public static final String WHAT_IS_MY_NAME_INTENT = "WhatIsMyNameIntent";
	}

	public static class AgeStrings {
		public static final String AGE_IS = "you are %d years old";
		public static final String ASK_AGE_REPEAT = "I will repeat, You can ask me how old are you saying, how old am i?";
		public static final String DIDNT_TELL_AGE = "you didn't tell me your age yet";
		public static final String TELL_AGE_AGAIN = "I'm not sure how old are you. Please tell me again";
		public static final String TELL_AGE_AGAIN_REPEAT = "I will repeat, I'm not sure how old are you. Please tell me again";
	}

	public static class BloodPressureString {
		public static final String BLODD_PRESSURE_LOGGED = "blood pressure's measure was logged successfully";
		public static final String NO_LOGGED_BLODD_PRESSURE = "You haven't logged any blood pressure measure today yet";
		public static final String TELL_BLODD_PRESSURE_AGAIN = "there was a problem with the blood pressure's logging, Please tell me again";
		public static final String TELL_BLODD_PRESSURE_AGAIN_REPEAT = "I'll repeat, there was a problem with the blood pressure's logging, Please tell me again";
	}

	public static class DrinkStrings {
		public static final String DIDNT_DRINKED_TODAY = "You haven't drink %s today yet";
		public static final String DRINKED_SO_FAR_MANY = "so far, you have drank %d cups of %s";
		public static final String DRINKED_SO_FAR_ONE = "so far, you have drank a single cup of %s";
		public static final String DRINKS_LOGGING_PROBLEM = "There was a problem with the water logging, Please tell me again";
		public static final String DRINKS_LOGGING_PROBLEM_REPEAT = "I'll repeat, there was a problem with the water logging,  Please tell me again";
		public static final String MANY_DRINKS_LOGGED = "You logged %d cups of %s, to your health!";
		public static final String ONE_DRINKS_LOGGED = "You logged one cup of %s, to your health!";
		public static final String SITTING_TIP = " Remember to drink while sitting down. ";
		public static final String TELL_DRINKS_AGAIN = "I'm not sure what did you drink. Please tell me again";
		public static final String TELL_DRINKS_AGAIN_REPEAT = "I will repeat, I'm not sure what did you  drink. Please tell me again";
		public static final String TELL_DRINKS_AMOUNT_AGAIN = "I'm not sure how many cups you drank. Please tell me again";
		public static final String TELL_DRINKS_AMOUNT_AGAIN_REPEAT = "I will repeat, I'm not sure how many cups you drank. Please tell me again";
	}

	public static class FoodStrings {
		public static final String DIDNT_EAT_ANYTHING = "you haven't eaten anything today yet. Please Tell me when you do";
		public static final String DRINK_LIQUID_TIP = " Remember to drink liquids after the meal. ";
		public static final String FOOD_LOGGED = "You logged %d %s of %s, bon appetit!";
		public static final String FOOD_LOGGING_PROBLEM = "There was a problem with the portion logging, Please tell me again";
		public static final String FOOD_LOGGING_PROBLEM_REPEAT = "I'll repeat, there was a problem with the portion logging,  Please tell me again";
		public static final String FOOD_UNITS_PROBLEM = "There was a problem with the units you provided, Please try to tell me the food in different units, in grams for example";
		public static final String FOOD_UNITS_PROBLEM_REPEAT = "I'll repeat, there was a problem with the units you provided, Please try to tell me the food in different units, in grams for example";
		public static final String SITTING_TIP = " Remember to eat your meals while sitting down comfortably. ";
		public static final String SLOWLY_TIP = " Remember to eat slowly. ";
		public static final String TELL_FOOD_AGAIN = "I'm not sure what you ate. Please tell me again";
		public static final String TELL_FOOD_AGAIN_REPEAT = "I will repeat, I'm not sure what you ate. Please tell me again";
		public static final String TELL_FOOD_AMOUNT_AGAIN = "I'm not sure about the amount you ate, Please tell me again";
		public static final String TELL_FOOD_AMOUNT_AGAIN_REPEAT = "I will repeat, I'm not sure about the amount you ate, Please tell me again";
		public static final String TELL_FOOD_UNITS_AGAIN = "I'm not sure about the units of what you ate, Please tell me again";
		public static final String TELL_FOOD_UNITS_AGAIN_REPEAT = "I will repeat, I'm not sure about the units of what you ate, Please tell me again";
		public static final String WATER_BEFORE_MEAL_TIP = " Remember to drink a cup of water before the meal. ";
	}

	public static class HeightStrings {
		public static final String ASK_HEIGHT = "I will repeat, You can ask me for your height saying, what is my height?";
		public static final String DIDNT_TELL_HEIGHT = "you didn't tell me what is your height yet";
		public static final String HEIGHT_IS = "your height is %d centimeters";
		public static final String HEIGHT_LOGGED = "your height is %d centimeters";
		public static final String TELL_HEIGHT_AGAIN = "I'm not sure what is your height. Please tell me again";
		public static final String TELL_HEIGHT_AGAIN_REPEAT = "I will repeat, I'm not sure what is your height. Please tell me again";
	}

	public static class WeightStrings {
		public static final String ASK_WEIGHT = "I will repeat, You can ask me for your weight saying, what is my weight?";
		public static final String DIDNT_TELL_WEIGHT = "you didn't tell me your weight yet";
		public static final String TELL_WEIGHT_AGAIN_REPEAT = "I will repeat, I'm not sure what is your weight. Please tell me again";
		public static final String TELL_WEIGHT_GOAL_AGAIN = "I'm not sure what is your weight goal. Please tell me again";
		public static final String TELL_WEIGHT_GOAL_AGAIN_REPEAT = "I will repeat, I'm not sure what is your weight goal. Please tell me again";
		public static final String TELL_WEIGHT_AGAIN = "I'm not sure what is your weight. Please tell me again";
		public static final String WEIGHT_IS = "your weight is %d kilograms";
		public static final String WEIGHT_LOGGED = "your weight is %d kilograms";
	}

	public static class NameStrings {
		public static final String ASK_NAME = "I will repeat, You can ask me what is your name by saying, what is my name?";
		public static final String DIDNT_TELL_NAME = "you didn't tell me your name yet";
		public static final String NAME_IS = "your name is %s";
		public static final String TELL_NAME_AGAIN = "I'm not sure what is your name. Please tell me again";
		public static final String TELL_NAME_AGAIN_REPEAT = "I will repeat, I'm not sure what is your name. Please tell me again";
		public static final String WELCOME_NAME = "Welcome %s!";
	}

	public static class AlcoholStrings {
		public static final String CAN_DRIVE = "you are allowed to drive, go safely";
		public static final String CAN_DRIVE_TELL_GENDER = "you didn't tell me your gender yet";
		public static final String CAN_DRIVE_TELL_WEIGHT = "you didn't tell me your weight yet";
		public static final String CANT_DRIVE = "you can't drive right now, you drank too much";
		public static final String CHEERS = "Cheers! you drank %s";
		public static final String DRINK_ALCOHOL = "You can drink again saying cheers and the alcoholic drink name";
		public static final String NOT_ALCOHOL = "This is not an alcoholic drink";
	}

	public static class CigarettesStrings {
		public static final String ASK_CIGS = "I will repeat, You can ask me how many cigarettes you have smoked so far saying, how many cigarettes i smoked?";
		public static final String CIGS_LOGGING_PROBLEM = "There was a problem with the smoking logging, Please tell me again";
		public static final String CIGS_LOGGING_PROBLEM_REPEAT = "I'll repeat, there was a problem with the smoking logging,  Please tell me again";
		public static final String DIDNT_SMOKED = "you did not smoke today, Well Done";
		public static final String DIDNT_TELL_CIGS_LIMIT = "you didn't tell me what is your cigarettes limit";
		public static final String MANY_CIGS_LOGGED = "you logged %d cigarettes. You can ask me how many cigarettes you have smoked so far saying, how many cigarettes i smoked?";
		public static final String ONE_CIG_LOGGED = "you logged one cigarette. You can ask me how many cigarettes you have smoked so far saying, how many cigarettes i smoked?";
		public static final String SMOKED_MANY = "you have already smoked %d cigarettes today";
		public static final String SMOKED_ONE = "you smoked a single cigarette today";
		public static final String TELL_CIGS_AGAIN = "I'm not sure how many cigarettes you smoked. Please tell me again";
		public static final String TELL_CIGS_AGAIN_REPEAT = "I will repeat, I'm not sure how many cigarettes you smoked. Please tell me again";
		public static final String TELL_CIGS_LIMIT = "you are allowed to smoke %d cigarettes every day";
		public static final String TELL_CIGS_LIMIT_AGAIN = "I'm not sure what is your cigarettes limit. Please tell me again";
		public static final String TELL_CIGS_LIMIT_AGAIN_REPEAT = "I will repeat, I'm not sure what is your cigarettes limit. Please tell me again";
	}

	public static class BMIStrings {
		public static final String BMI_TELL_HEIGHT_AND_WEIGHT = "Please Tell me your weight and height, So i can calculate your BMI";
		public static final String BMI_TELL_WEIGHT = "Please Tell me your weight, So i can calculate your BMI";
		public static final String BMI_TELL_HEIGHT = "Please Tell me your height, So i can calculate your BMI";
		public static final String TELL_BMI = "your BMI is %.2f";

	}

	public static class EmailStrings {
		public static final String DAILY_MAIL_SENT = "I sent you a mail with a daily report";
		public static final String WEEKLY_MAIL_SENT = "I sent you a mail with a weekly report";
		public static final String WEIGHT_MAIL_SENT = "I sent you a mail describing your weight progress";
		public static final String WEIGHT_MAIL_NOT_SENT = "I don't have enough measurements";
	}

	public static class GoalsAndMeasuresStrings {
		public static final String DIDNT_EAT = "you didn't eat anything today. ";
		public static final String DIDNT_TELL_GOAL_YET = "You didn't tell me your goal yet! ";
		public static final String DIDNT_TELL_MEASURE_GOAL = "you didn't tell me what is your %s goal";
		public static final String DIDNT_TELL_WEIGHT = "you didn't tell me what is your weight";
		public static final String DIDNT_TELL_WEIGHT_GOAL = "you didn't tell me your weight goal yet";
		public static final String DOING_GREAT = "you are doing great, keep going";
		public static final String FAR_FROM_GOAL = "you are still far from your goal";
		public static final String GOAL_ACHIEVED = "You achieved your %s goal, Well Done!";
		public static final String GOAL_CLOSE = " You are close to your goal. try to eat less during the rest of the day ";
		public static final String GOAL_FAR = " You are far from your goal. Try to eat more during the rest of the day ";
		public static final String GOAL_KEEP = " Keep going! ";
		public static final String GOAL_PASSED = " You passed your %s goal by %d %s . Try to eat less during the rest of the day ";
		public static final String GOAL_PASSED_GRAMS = " You passed your %s goal by %d grams . Try to eat less during the rest of the day";
		public static final String LEFT_FOR_GOAL = "There are %d %s left for your goal! ";
		public static final String LEFT_FOR_GOAL_GRAMS = "There are %d grams of %s left for your goal! ";
		public static final String MEASURE_GOAL_LOGGED_CALORIES = "your %s goal is %d calories";
		public static final String MEASURE_GOAL_LOGGED_GRAMS = "your %s goal is %d grams";
		public static final String MEASURE_AMOUNT_ATE = "You ate %d %s today. ";
		public static final String MEASURE_AMOUNT_ATE_GRAMS = "You ate %d grams of %s today. ";
		public static final String TELL_MEASURE_AGAIN = "I'm not sure which goal do you want. Please tell me again";
		public static final String TELL_MEASURE_AGAIN_REPEAT = "I will repeat, I'm not sure which goal do you want. Please tell me again";
		public static final String TELL_MEASURE_GOAL_AGAIN = "I'm not sure what is your goal. Please tell me again";
		public static final String TELL_MEASURE_GOAL_AGAIN_REPEAT = "I will repeat, I'm not sure what is your goal. Please tell me again";
	}

	public static class GeneralString {
		public static final String LOGGED_SUCCESSFULLY = "logged succesfully";
		public static final String LOGGED_SUCCESSFULLY_REPEAT = "I will repeat, logged successfully";
	}

	public static class FallbackString {
		public static final String TRY_HELP = "Sorry, I don't know that. You can say try saying help!";
	}

	public static class HelpString {
		public static final String SAY_HELP = "You can tell me what you ate, for example, i ate twenty grams of pasta or you can tell me everytime you drink,"
				+ " for example, i drank 3 cups of water.";
		public static final String SAY_HELP_REPEAT = "I will repeat, " +SAY_HELP;
	}

}
