package it.univaq.disim.bpd.publicbillpostingservice.exception;

public class ResourceNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2121846823069424411L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
