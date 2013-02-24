package org.thelq.stackarchive.bot.se;

/**
 *
 * @author Leon
 */
public class SEException extends RuntimeException {
	public SEException(String message) {
		super(message);
	}

	public SEException(String message, Throwable cause) {
		super(message, cause);
	}
}
