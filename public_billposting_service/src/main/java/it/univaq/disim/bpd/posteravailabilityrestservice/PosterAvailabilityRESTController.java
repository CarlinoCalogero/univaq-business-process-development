package it.univaq.disim.bpd.posteravailabilityrestservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResultWithVariables;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.univaq.disim.bpd.publicbillpostingservice.model.Bill;
import it.univaq.disim.bpd.publicbillpostingservice.model.CheckAvailabilityResponse;
import it.univaq.disim.bpd.publicbillpostingservice.model.PosterRequestDecision;
import it.univaq.disim.bpd.publicbillpostingservice.model.Zone;

@RestController
@RequestMapping("/poster-availability")
public class PosterAvailabilityRESTController {

	private final String PROCESS_KEY = "PublicBillpostingService";

	@Autowired
	private RuntimeService runtimeService;

	@PostMapping("/request")
	public ResponseEntity<?> postPosterAvailabilityRequest(@RequestBody PosterAvailabilityRequest request) {

		System.out.println("Poster availability request received!");

		ProcessInstanceWithVariables instance = runtimeService.createProcessInstanceByKey(PROCESS_KEY)
				.setVariableLocal("posterAvailabilityRequest", request).executeWithVariablesInReturn();

		// Safely retrieve variables from the process instance
		Map<String, Object> variables = instance.getVariables();

		CheckAvailabilityResponse checkAvailabilityResponse = (CheckAvailabilityResponse) variables
				.get("checkAvailabilityResponse");

		if (checkAvailabilityResponse == null) {
			return ResponseEntity.status(500).body("checkAvailabilityResponse was not set by the process.");
		}

		String requestId = checkAvailabilityResponse.getRequestId();

		Map<String, List<Zone>> selectedZones = (Map<String, List<Zone>>) variables.get("selectedZones");

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("requestId", requestId);
		responseBody.put("selectedZones", selectedZones);

		return ResponseEntity.ok(responseBody);
	}

	@PostMapping("/decide")
	public ResponseEntity<?> postPosterRequestDecision(@RequestBody PosterRequestDecision decision) throws Exception {

		try {

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

		} catch (Exception e) {
			throw e;
		}

	}
}