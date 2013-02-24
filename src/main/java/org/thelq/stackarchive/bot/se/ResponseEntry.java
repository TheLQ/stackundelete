
package org.thelq.stackarchive.bot.se;

import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author Leon
 */
@Data
public class ResponseEntry {
	@MaybeAbsent
	protected int backoff;
	protected boolean hasMore;
	protected JSONArray items;
	protected int page;
	protected int pageSize;
	protected int quotaMax;
	protected int quotaRemaining;
	protected int total;
	//TODO: Comprehensive list of types?
	protected String type;
}
