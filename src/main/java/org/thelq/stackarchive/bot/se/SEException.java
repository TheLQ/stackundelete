package org.thelq.stackarchive.bot.se;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Leon
 */
//Manually specify since we don't want @ToString (want message) or @AllArgsConstructor 
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SEException extends Exception {
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
