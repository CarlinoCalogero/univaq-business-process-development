package it.univaq.disim.bpd.publicbillpostingservice.endpoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResultWithVariables;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.univaq.disim.bpd.publicbillpostingservice.exception.InvalidRequestException;
import it.univaq.disim.bpd.publicbillpostingservice.exception.ResourceNotFoundException;
import it.univaq.disim.bpd.publicbillpostingservice.model.Bill;
import it.univaq.disim.bpd.publicbillpostingservice.model.CheckAvailabilityResponse;
import it.univaq.disim.bpd.publicbillpostingservice.model.PosterAvailabilityRequest;
import it.univaq.disim.bpd.publicbillpostingservice.model.PosterRequestDecision;
import it.univaq.disim.bpd.publicbillpostingservice.model.Zone;

@RestController
@RequestMapping("/poster-availability")
public class RESTController {

	private final String PROCESS_KEY = "PublicBillpostingService";

	@Autowired
	private RuntimeService runtimeService;

	@PostMapping("/request")
	public ResponseEntity<?> postPosterAvailabilityRequest(@RequestBody @Valid PosterAvailabilityRequest request) {

		System.out.println("Poster availability request received!");

		ProcessInstanceWithVariables instance = runtimeService.createProcessInstanceByKey(PROCESS_KEY)
				.setVariableLocal("posterAvailabilityRequest", request).executeWithVariablesInReturn();

		// Safely retrieve variables from the process instance
		Map<String, Object> variables = instance.getVariables();

		CheckAvailabilityResponse checkAvailabilityResponse = (CheckAvailabilityResponse) variables
				.get("checkAvailabilityResponse");

		if (checkAvailabilityResponse == null) {
			throw new ResourceNotFoundException("CheckAvailabilityResponse was not set by the process.");
		}

		String requestId = checkAvailabilityResponse.getRequestId();

		Map<String, List<Zone>> selectedZones = (Map<String, List<Zone>>) variables.get("selectedZones");

		if (selectedZones == null) {
			throw new InvalidRequestException("Selected zones were not returned by the process.");
		}

		Object totalPriceObj = variables.get("totalPrice");

		if (!(totalPriceObj instanceof Number)) {
			throw new InvalidRequestException("Total price is missing or not numeric.");
		}

		double totalPrice = ((Number) totalPriceObj).doubleValue();

		if (totalPrice < 0.0) {
			throw new InvalidRequestException("Total price is negative, which is invalid.");
		}

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("selectedZones", selectedZones);
		responseBody.put("totalPrice", totalPrice);
		responseBody.put("requestId", requestId);

		return ResponseEntity.ok(responseBody);
	}

	@PostMapping("/decide")
	public ResponseEntity<?> postPosterRequestDecision(@RequestBody @Valid PosterRequestDecision decision)
			throws Exception {

		System.out.println("Poster request decision received!");

		MessageCorrelationResultWithVariables instance = runtimeService
				.createMessageCorrelation("poster-request-decision")
				.localVariableEquals("correlationVariable", decision.getRequestId())
				.setVariable("posterRequestDecision", decision).correlateWithResultAndVariables(true);

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("accountHolder", "");
		responseBody.put("invoiceNumber", "");
		responseBody.put("amountDue", null);

		System.out.println("Decision: " + decision.getDecision());

		if (decision.getDecision().equals("confirm")) {

			// Fetch before process ends!
			Map<String, Object> variables = instance.getVariables();

			Bill orderInformation = (Bill) variables.get("orderInformation");

			if (orderInformation == null) {
				throw new IllegalStateException("Order information is missing after confirmation.");
			}

			System.out.println("orderInformation: " + orderInformation);
			System.out.println("Poster request confirmed from inside!");

			responseBody.put("accountHolder", orderInformation.getAccountHolder());
			responseBody.put("invoiceNumber", orderInformation.getInvoiceNumber());
			responseBody.put("amountDue", orderInformation.getAmountDue());

		} else {

			System.out.println("Poster request cancelled from inside!");

		}

		System.out.println("Now returning control!");

		return ResponseEntity.ok(responseBody);

	}
}