/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thelq.stackexchange.undelete.question;

import java.util.List;
import lombok.Data;

/**
 *
 * @author Leon
 */
@Data
public class AnswerEntry implements Post {
	protected long answerId;
	protected long ownerId;
	protected int downVotes;
	protected int upVotes;
	protected String body;
	protected List<CommentEntry> comments;
}
