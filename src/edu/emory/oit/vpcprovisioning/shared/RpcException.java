package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RpcException extends RuntimeException implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
     * Default constructor.
     **/
    public RpcException() {
    }


    /**
     * Constructor.
     * @param message text descibing the exception
     **/
    public RpcException(String message) {
        super(message);
    }


    /**
     * Constructor.
     * @param rootCause the Exception that caused this exception to be thrown
     **/
    public RpcException(Throwable rootCause) {
    	super(rootCause);
    }


    /**
     * Constructor.
     * @param reason an text message describing the exception
     * @param parent the Exception that caused this exception to be thrown
     **/
    public RpcException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
