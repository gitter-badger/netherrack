package org.netherrack;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.mc.protocol.data.message.ChatColor;
import org.spacehq.mc.protocol.data.message.ChatFormat;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.data.message.MessageStyle;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.event.server.ServerAdapter;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.net.Proxy;

public class Netherrack {
	public static void main(String[] args) {
		Server server = new Server("localhost", 25565, MinecraftProtocol.class, new TcpSessionFactory(Proxy.NO_PROXY));
		
		server.setGlobalFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
		server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, true);
		server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, 256);
		
		new RegisterListeners(server);

		server.addListener(new ServerAdapter() {
			@Override
			public void sessionAdded(SessionAddedEvent event) {
				event.getSession().addListener(new SessionAdapter() {
					@Override
					public void packetReceived(PacketReceivedEvent event) {
						if (event.getPacket() instanceof ClientChatPacket) {
							ClientChatPacket packet = event.getPacket();
							GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
							System.out.println(profile.getName() + ": " + packet.getMessage());
							Message msg = new TextMessage("Hello, ")
									.setStyle(new MessageStyle().setColor(ChatColor.GREEN));
							Message name = new TextMessage(profile.getName()).setStyle(
									new MessageStyle().setColor(ChatColor.AQUA).addFormat(ChatFormat.UNDERLINED));
							Message end = new TextMessage("!");
							msg.addExtra(name);
							msg.addExtra(end);
							event.getSession().send(new ServerChatPacket(msg));
						}
					}
				});
			}
		});

		server.bind();
		while (true) {
			// keep it running
		}
	}
}