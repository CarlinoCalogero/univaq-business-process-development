package it.univaq.disim.bpd.publicbillpostingservice.service;

import org.camunda.spin.Spin;
import org.springframework.stereotype.Service;

import it.univaq.disim.bpd.publicbillpostingservice.exception.InvalidRequestException;
import it.univaq.disim.bpd.publicbillpostingservice.model.User;

@Service("userParserService")
public class UserParser {

	public User unmarshallUser(String userJSON) {
		if (userJSON.isEmpty()) {
			throw new InvalidRequestException("User does not Exist");
		}

		try {
			return Spin.JSON(userJSON).mapTo(User.class);
		} catch (Exception e) {
			throw new InvalidRequestException("Failed to parse User JSON Response");
		}

	}

}
