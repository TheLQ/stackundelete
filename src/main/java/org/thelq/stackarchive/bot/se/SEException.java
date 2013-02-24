package org.thelq.stackarchive.bot.se;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Leon
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SEException extends RuntimeException {
	protected int errorId;
	protected String errorName;
	protected String errorMessage;

	public SEException(int errorId, String errorName, String errorMessage, Throwable cause) {
		super("Error #" + errorId + "(" + errorName + "): " + errorMessage, cause);
		this.errorId = errorId;
		this.errorName = errorName;
		this.errorMessage = errorMessage;
	}

	public SEException(int errorId, String errorName, String errorMessage) {
		this(errorId, errorName, errorMessage, null);
	}
}
