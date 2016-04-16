package org.netherrack;

import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.mc.protocol.data.message.ChatColor;
import org.spacehq.mc.protocol.data.message.MessageStyle;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.event.server.ServerAdapter;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import org.spacehq.packetlib.event.server.SessionRemovedEvent;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.net.Proxy;

public class Netherrack {
	public static void main(String[] args) {
		System.out.println("Starting server on *:25565");

		Properties prop = new Properties();
		InputStream input = null;
		OutputStream output = null;

		try {
			input = new FileInputStream("server.properties");
			prop.load(input);

			Server server = new Server("localhost", Integer.parseInt(prop.getProperty("server-port")),
					MinecraftProtocol.class, new TcpSessionFactory(Proxy.NO_PROXY));
			server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD,
					Integer.parseInt(prop.getProperty("network-compression-threshold")));

			server.setGlobalFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
			server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY,
					Boolean.parseBoolean(prop.getProperty("online-mode")));

			server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, new Login());
			server.setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, new Ping());

			server.addListener(new ServerAdapter() {
				@Override
				public void sessionAdded(SessionAddedEvent event) {
					event.getSession().addListener(new Chat());
//					GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
//					System.out.println(profile.getName() + " has joined the game");
//					server.getSessions()
//					.forEach((session) -> session.send(
//							new ServerChatPacket(new TextMessage(profile.getName() + " has joined the game")
//									.setStyle(new MessageStyle().setColor(ChatColor.YELLOW)))));
				
				}

				@Override
				public void sessionRemoved(SessionRemovedEvent event) {
					MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
					if (protocol.getSubProtocol() == SubProtocol.GAME) {
						GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
						System.out.println(profile.getName() + " has left the game");
						server.getSessions()
								.forEach((session) -> session.send(
										new ServerChatPacket(new TextMessage(profile.getName() + " has left the game")
												.setStyle(new MessageStyle().setColor(ChatColor.YELLOW)))));
					}
				}
			});

			server.bind();
			System.out.println("Server started");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			} else {
				try {
					output = new FileOutputStream("server.properties");

					prop.setProperty("server-port", "25565");
					prop.setProperty("network-compression-threshold", "256");
					prop.setProperty("gamemode", "0");
					prop.setProperty("online-mode", "true");
					prop.setProperty("max-players", "20");
					prop.setProperty("motd", "A Minecraft Server");

					prop.store(output, null);

					prop = new Properties();

					input = new FileInputStream("server.properties");
					prop.load(input);

					input = new FileInputStream("server.properties");
					prop.load(input);

					Server server = new Server("localhost", Integer.parseInt(prop.getProperty("server-port")),
							MinecraftProtocol.class, new TcpSessionFactory(Proxy.NO_PROXY));
					server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD,
							Integer.parseInt(prop.getProperty("network-compression-threshold")));

					server.setGlobalFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
					server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY,
							Boolean.parseBoolean(prop.getProperty("online-mode")));

					server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, new Login());
					server.setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, new Ping());

					server.bind();
					System.out.println("Server started");

					server.addListener(new ServerAdapter() {
						@Override
						public void sessionAdded(SessionAddedEvent event) {
							event.getSession().addListener(new Chat());
//							GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
//							System.out.println(profile.getName() + " has joined the game");
//							server.getSessions()
//							.forEach((session) -> session.send(
//									new ServerChatPacket(new TextMessage(profile.getName() + " has joined the game")
//											.setStyle(new MessageStyle().setColor(ChatColor.YELLOW)))));
						
						}

						@Override
						public void sessionRemoved(SessionRemovedEvent event) {
							MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
							if (protocol.getSubProtocol() == SubProtocol.GAME) {
								GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
								System.out.println(profile.getName() + " has left the game");
								server.getSessions()
										.forEach((session) -> session.send(
												new ServerChatPacket(new TextMessage(profile.getName() + " has left the game")
														.setStyle(new MessageStyle().setColor(ChatColor.YELLOW)))));
							}
						}
					});
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

		while (true) {
			// keep it running
		}
	}
}