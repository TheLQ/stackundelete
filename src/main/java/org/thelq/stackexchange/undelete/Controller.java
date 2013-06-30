/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.stackexchange.undelete;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.thelq.stackexchange.undelete.server.AddSevlet;

/**
 *
 * @author Leon
 */

public class Controller {
	protected final DatabaseManager databaseManager;
	protected final Server webServer;
	public Controller() throws Exception {
		databaseManager = new DatabaseManager();
		
		//Start webserver
		ServletHandler mainHandler = new ServletHandler();
		mainHandler.addServletWithMapping(new ServletHolder(new AddSevlet(this)), "/add");
		webServer = new Server(8080);
		webServer.setHandler(mainHandler);
		webServer.start();
	}

	public static void main(String[] args) throws Exception {
		new Controller();
	}
}
