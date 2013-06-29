/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.stackexchange.undelete.question;

import java.util.List;

/**
 *
 * @author Leon
 */
public interface Post {
	public long getOwnerId();

	public int getUpVotes();

	public int getDownVotes();

	public String getBody();

	public List<CommentEntry> getComments();
}
