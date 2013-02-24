package org.thelq.stackarchive.bot;

import org.thelq.stackarchive.bot.se.SEAPI;

/**
 * Hello world!
 *
 */
public class Main {
	public static void main(String[] args) throws Exception {
		SEAPI.get().getRecent();
	}
}
