package com.belatrix.exceptions;

import java.io.Serializable;

/**
 * Timeout exception. Designed as a replacement of {@link TimeOutException}. <br/>
 * This exception has no side effects.
 *
 * @author <a href="mailto:jeanpaul.manjarres@payulatam.com">Jean Paul Manjarres Correal</a><br/>
 *         20/02/2014
 *
 */
public class PayUTimeoutException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = -2853933256409127912L;

	/**
	 * @param message
	 * @param cause
	 */
	public PayUTimeoutException(String message, Throwable cause) {

		super(message, cause);
	}

}
