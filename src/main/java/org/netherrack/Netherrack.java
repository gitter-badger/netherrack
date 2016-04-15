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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.net.Proxy;

public class Netherrack {
	public static void main(String[] args) {
		Properties prop = new Properties();
		InputStream input = null;
		OutputStream output = null;
		
		try {
			input = new FileInputStream("server.properties");
			prop.load(input);
			
			Server server = new Server("localhost", Integer.parseInt(prop.getProperty("server-port")), MinecraftProtocol.class, new TcpSessionFactory(Proxy.NO_PROXY));
			server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, Integer.parseInt(prop.getProperty("network-compression-threshold")));
			
			server.setGlobalFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
			server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, Boolean.parseBoolean(prop.getProperty("online-mode")));
			
			new RegisterListeners(server);
			server.bind();

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
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					//e.printStackTrace();
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
					
					Server server = new Server("localhost", Integer.parseInt(prop.getProperty("server-port")), MinecraftProtocol.class, new TcpSessionFactory(Proxy.NO_PROXY));
					server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, Integer.parseInt(prop.getProperty("network-compression-threshold")));
					
					server.setGlobalFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
					server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, Boolean.parseBoolean(prop.getProperty("online-mode")));
					
					new RegisterListeners(server);
					server.bind();

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
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		}
		
		while (true) {
			// keep it running
		}
	}
}