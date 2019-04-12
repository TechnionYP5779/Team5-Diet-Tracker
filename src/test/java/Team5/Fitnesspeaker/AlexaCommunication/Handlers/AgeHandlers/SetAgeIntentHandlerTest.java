package Team5.Fitnesspeaker.AlexaCommunication.Handlers.AgeHandlers;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Slot;

import Utils.Strings.AgeStrings;
import Utils.Strings.SlotString;

public class SetAgeIntentHandlerTest {

	@Test
	public void test() {
		HandlerInput input = Mockito.mock(HandlerInput.class);
//		RequestEnvelope re = Mockito.mock(RequestEnvelope.class);
		Request r = Mockito.mock(Request.class);
		Intent i = Mockito.mock(Intent.class);
//		Map<String, Slot> ss = Mockito.mock(Map.class);
//		
//		Mockito.when(input.getRequestEnvelope()).then(new RequestEnvelope(null));
//		Mockito.when(re.getRequest()).thenReturn(ir);
//		Mockito.when(ir.getIntent()).thenReturn(i);
//		Mockito.when(i.getSlots()).thenReturn(ss);
//		Mockito.when(ss.get(SlotString.NUMBER_SLOT)).thenReturn(null);
		
		Mockito.when(((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots()
				.get(SlotString.NUMBER_SLOT)).thenReturn(null);

		Mockito.verify(input.getResponseBuilder().withSimpleCard("FitnessSpeakerSession", AgeStrings.TELL_AGE_AGAIN)
				.withSpeech(AgeStrings.TELL_AGE_AGAIN));
		// fail("Not yet implemented");
	}

}
