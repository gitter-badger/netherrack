package org.netherrack;

import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.mc.protocol.ServerLoginHandler;
import org.spacehq.mc.protocol.data.game.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.setting.Difficulty;
import org.spacehq.mc.protocol.data.game.world.WorldType;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

public class Login {
	public Login(Server server) {
		server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
			@Override
			public void loggedIn(Session session) {
				session.send(new ServerJoinGamePacket(0, false, GameMode.SURVIVAL, 0, Difficulty.PEACEFUL, 10,
						WorldType.DEFAULT, false));
			}
		});
	}
}
