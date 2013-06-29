/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.stackexchange.undelete.question;

import lombok.Data;

/**
 *
 * @author Leon
 */
@Data
public class CommentEntry {
	protected long commentId;
	protected long postId;
	protected long ownerId;
	protected int upVotes;
	protected String body;
}
