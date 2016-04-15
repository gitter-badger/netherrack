package org.netherrack;

import org.spacehq.packetlib.Server;

public class RegisterListeners {
	public RegisterListeners(Server server) {
		// get server to accept pings
		new Ping(server);
		
		// get server to complete logins
		new Login(server);
	}
}
