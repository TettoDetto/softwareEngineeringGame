package client.utility.exceptions;

public class NetworkErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8855401682490591004L;

	public NetworkErrorException(String errorMessage) {
		super(errorMessage);
	}

}
