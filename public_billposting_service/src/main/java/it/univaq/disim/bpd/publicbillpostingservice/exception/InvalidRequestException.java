package it.univaq.disim.bpd.publicbillpostingservice.exception;

public class InvalidRequestException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8910322918282697940L;

	public InvalidRequestException(String message) {
		super(message);
	}
}
