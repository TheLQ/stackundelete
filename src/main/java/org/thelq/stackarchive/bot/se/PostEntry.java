package org.thelq.stackarchive.bot.se;

import java.util.List;
import lombok.Data;

/**
 *
 * @author Leon
 */
@Data
public class PostEntry {
	protected int postId;
	protected Type postType;
	@MaybeAbsent
	protected ShallowUserEntry owner;
	protected String body;
	@MaybeAbsent
	protected List<CommentEntry> comments;
	protected int downVoteCount;
	protected int upVoteCount;
	protected long creationDate;
	protected long lastActivityDate;
	@MaybeAbsent
	protected long lastEditDate;
	protected String link;
	protected int score;

	public static enum Type {
		QUESTION,
		ANSWER
	}
}
