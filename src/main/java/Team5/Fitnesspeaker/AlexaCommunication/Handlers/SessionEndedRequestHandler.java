package Team5.Fitnesspeaker.AlexaCommunication.Handlers;

import static com.amazon.ask.request.Predicates.requestType;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;

/** this class handles session ending
 * @author Shalev Kuba
 * @since 2018-12-07
 * */
public class SessionEndedRequestHandler implements RequestHandler {
	@Override
	public boolean canHandle(final HandlerInput i) {
		return i.matches(requestType(SessionEndedRequest.class));
	}

	@Override
	public Optional<Response> handle(final HandlerInput i) {
		return i.getResponseBuilder().build();
	}
}