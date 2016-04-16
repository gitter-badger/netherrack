package org.netherrack;

import org.spacehq.mc.protocol.ServerLoginHandler;
import org.spacehq.mc.protocol.data.game.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.setting.Difficulty;
import org.spacehq.mc.protocol.data.game.world.WorldType;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.packetlib.Session;

public class Login implements ServerLoginHandler {

	@Override
	public void loggedIn(Session session) {
		session.send(new ServerJoinGamePacket(0, false, GameMode.SURVIVAL, 0, Difficulty.PEACEFUL, 10,
				WorldType.DEFAULT, false));
		
		session.send(new ServerPlayerPositionRotationPacket(0,1.62,0,0,0,0));
	}
}
