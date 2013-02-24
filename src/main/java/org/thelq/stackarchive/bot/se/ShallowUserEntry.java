
package org.thelq.stackarchive.bot.se;

import lombok.Data;

/**
 *
 * @author Leon
 */
@Data
public class ShallowUserEntry {
	protected int userId;
	protected Type userType;
	protected String displayName;
	protected int acceptRate;
	protected String link;
	protected String profileImage;
	protected int reputation;
	
	
	public static enum Type {
		UNREGISTERED,
		REGISTERED,
		MODERATOR,
		DOESNOTEXIST
	}
}
