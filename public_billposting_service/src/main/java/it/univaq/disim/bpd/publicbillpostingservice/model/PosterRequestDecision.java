package it.univaq.disim.bpd.publicbillpostingservice.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class PosterRequestDecision {

	@NotBlank(message = "requestId is mandatory")
	private String requestId;

	@NotBlank(message = "decision is mandatory")
	@Pattern(regexp = "confirm|cancel", flags = Pattern.Flag.CASE_INSENSITIVE, message = "decision must be either 'confirm' or 'cancel'")
	private String decision;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[Poster Request Decision Info]");
		stringBuilder.append("\n");
		stringBuilder.append("request id: " + requestId);
		stringBuilder.append("\n");
		stringBuilder.append("decision: " + decision);
		return stringBuilder.toString();
	}

}
