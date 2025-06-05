package it.univaq.disim.bpd.publicbillpostingservice.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import it.univaq.disim.bpd.publicbillpostingservice.model.CheckAvailabilityResponse;

@Component("requestCancelNotifierDelegate")
public class RequestCancelNotifier implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// Get process variables
		CheckAvailabilityResponse checkAvailabilityResponse = (CheckAvailabilityResponse) execution
				.getVariable("checkAvailabilityResponse");

		System.out.println("Request \'" + checkAvailabilityResponse.getRequestId() + "\' successfully cancelled!");
	}

}
