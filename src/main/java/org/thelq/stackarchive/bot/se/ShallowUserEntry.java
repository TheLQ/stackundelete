
package org.thelq.stackarchive.bot.se;

import lombok.Data;

/**
 *
 * @author Leon
 */
@Data
public class ShallowUserEntry {
	@MaybeAbsent
	protected int userId;
	protected Type userType;
	@MaybeAbsent
	protected String displayName;
	@MaybeAbsent
	protected int acceptRate;
	@MaybeAbsent
	protected String link;
	@MaybeAbsent
	protected String profileImage;
	@MaybeAbsent
	protected int reputation;
	
	
	public static enum Type {
		UNREGISTERED,
		REGISTERED,
		MODERATOR,
		DOESNOTEXIST
	}
}
