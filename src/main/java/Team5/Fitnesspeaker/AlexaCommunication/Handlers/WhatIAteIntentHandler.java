package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import Utils.DBUtils;
import Utils.DBUtils.DBException;
import Utils.Portion;
import Utils.Portion.Type;
import Utils.Strings;
import Utils.Strings.FoodStrings;
import Utils.Strings.IntentsNames;

/** this class handles eating history querying
 * @author Shalev Kuba
 * @since 2018-12-26
 * */
public class WhatIAteIntentHandler implements RequestHandler {

	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(intentName(IntentsNames.WHAT_I_ATE_INTENT));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {

		String speechText = "";

		// initialize database object with the user mail
		final DBUtils db = new DBUtils(i.getServiceClientFactory().getUpsService().getProfileEmail());

		List<Portion> FoodList = new LinkedList<>();

		// retrieving the information
		try {
			FoodList = db.DBGetTodayFoodList().stream().map(p -> p.getValue()).filter(p -> p.getType() == Type.FOOD)
					.collect(Collectors.toList());
		} catch (final DBException e) {
			// no need to do anything
		}
    
    if (FoodList.isEmpty())
			speechText = FoodStrings.DIDNT_EAT_ANYTHING;
    else{
		  Portion.Meal m = FoodList.get(0).getMeal();
		  speechText += "For " + m.name();
		  for (final Portion p : FoodList) {
		  	if (! (p.getMeal().name().equals(m.name()))) {
		  		speechText += "For " + p.getMeal().name();
		  		m = p.getMeal();
		  	}
			  String[] splited2 =p.getTime().toString().split(" ")[3].split(":");
			  speechText += ", at "+Integer.parseInt(splited2[0]) + ":" + Integer.parseInt(splited2[1])+" you ate " + Integer.valueOf((int) p.getAmount()) + " grams of " + p.getName() ;
		  }
    }
    
		return i.getResponseBuilder().withSimpleCard(Strings.GLOBAL_SESSION_NAME, speechText).withSpeech(speechText)
				.withShouldEndSession(Boolean.TRUE).build();

	}
}