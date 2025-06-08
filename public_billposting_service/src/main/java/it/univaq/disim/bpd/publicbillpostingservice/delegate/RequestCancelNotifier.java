package it.univaq.disim.bpd.publicbillpostingservice.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.univaq.disim.bpd.publicbillpostingservice.model.CheckAvailabilityResponse;

@Component("requestCancelNotifierDelegate")
public class RequestCancelNotifier extends BaseDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(RequestCancelNotifier.class);

	@Override
	public void execute(DelegateExecution execution) {
		CheckAvailabilityResponse checkAvailabilityResponse = getRequiredVariable(execution,
				"checkAvailabilityResponse", CheckAvailabilityResponse.class);

		String requestId = checkAvailabilityResponse.getRequestId();

		logger.info("Request '{}' successfully cancelled!", requestId);
	}
}
