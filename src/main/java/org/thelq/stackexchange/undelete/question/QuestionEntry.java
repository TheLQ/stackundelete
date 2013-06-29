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
public class QuestionEntry implements Post {
	protected long questionId;
	protected long ownerId;
	protected int downVotes;
	protected int upVotes;
	protected String body;
	protected List<CommentEntry> comments;
}
