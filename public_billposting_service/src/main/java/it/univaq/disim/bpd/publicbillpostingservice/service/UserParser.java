package it.univaq.disim.bpd.publicbillpostingservice.service;

import org.camunda.spin.json.SpinJsonNode;
import org.springframework.stereotype.Service;

import it.univaq.disim.bpd.publicbillpostingservice.model.User;

@Service("userParserService")
public class UserParser {

	public User unmarshallUser(SpinJsonNode userJSON) {
		return userJSON.mapTo(User.class);
	}

}
