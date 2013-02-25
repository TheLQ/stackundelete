
package org.thelq.stackarchive.bot.se;

import java.util.Collection;
import lombok.Data;

/**
 *
 * @author Leon
 */
@Data
public class ResponseEntry<E> {
	@MaybeAbsent
	protected int backoff;
	protected boolean hasMore;
	protected Collection<E> items;
	protected int page;
	protected int pageSize;
	protected int quotaMax;
	protected int quotaRemaining;
	protected int total;
	//TODO: Comprehensive list of types?
	protected String type;
}
