package org.netherrack;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.mc.protocol.data.message.ChatColor;
import org.spacehq.mc.protocol.data.message.MessageStyle;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.data.status.PlayerInfo;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.VersionInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoBuilder;
import org.spacehq.packetlib.Session;

public class Ping implements ServerInfoBuilder {
	@Override
    public ServerStatusInfo buildInfo(Session session) {
        return new ServerStatusInfo(new VersionInfo(MinecraftConstants.GAME_VERSION, MinecraftConstants.PROTOCOL_VERSION), new PlayerInfo(20, 0, new GameProfile[0]), new TextMessage("A Minecraft Server").setStyle(new MessageStyle().setColor(ChatColor.RESET)), null);
    }
}
