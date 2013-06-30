/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thelq.stackexchange.undelete;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import java.net.UnknownHostException;

/**
 *
 * @author Leon
 */
public class DatabaseManager {
	protected final MongoClient mongoClient;
	protected final DB database;

	public DatabaseManager() throws UnknownHostException {
		//Start database
		mongoClient = new MongoClient("localhost", 27017);
		mongoClient.setWriteConcern(WriteConcern.JOURNALED);
		database = mongoClient.getDB("se-undelete");
	}
}
