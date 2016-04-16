package org.netherrack;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;

public class Chat extends SessionAdapter {
	@Override
	public void packetReceived(PacketReceivedEvent event) {
		if (event.getPacket() instanceof ClientChatPacket) {
			ClientChatPacket packet = event.getPacket();
			GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
			System.out.println("<" + profile.getName() + "> " + packet.getMessage());
			event.getSession().send(new ServerChatPacket("<" + profile.getName() + "> " + packet.getMessage()));
		}
	}
}
