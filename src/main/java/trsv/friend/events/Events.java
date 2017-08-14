package trsv.friend.events;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import trsv.friend.data.BPhook;
import trsv.friend.data.Friend;
import trsv.friend.data.Profile;
import trsv.friend.main.Main;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Events implements Listener {

    SimpleDateFormat DATEPRINT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        ProxiedPlayer pConnect = event.getPlayer();
        String connect = event.getTarget().getName();

        if (!Main.listOnce.contains(pConnect.getName())) {
            Main.listOnce.add(pConnect.getName());
            for (ProxiedPlayer pServer : ProxyServer.getInstance().getPlayers()) {
                if (pServer != pConnect && Friend.isFriend(pServer.getName(), pConnect.getName())) {
                    switch (connectMessage(connect, pServer.getServer().getInfo().getName())) {
                        case 1:
                            pServer.sendMessage(new TextComponent(
                                    Main.msg.getConnectToEqualsLobby(
                                            BPhook.getPrefix(pConnect.getName()) + pConnect.getName()
                                    )));
                            break;
                        case 2:
                            pServer.sendMessage(new TextComponent(
                                    Main.msg.getConnectToServer(
                                            BPhook.getPrefix(pConnect.getName()) + pConnect.getName(), connect
                                    )));
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
	public void onPlayerDisconnect(ServerDisconnectEvent event) {
        ProxiedPlayer p = event.getPlayer();

        Profile profile = Main.getProfile(p.getName());

        if (profile != null) {
            Date date = new Date();
            profile.lastOnline = DATEPRINT.format(date);
            Main.saveProfile(p.getName(), profile);
        }

        String disconnect = p.getServer().getInfo().getName();

        if (ProxyServer.getInstance().getPlayer(p.getName()) == null) {
            Main.listOnce.remove(p.getName());
            for (ProxiedPlayer pServer : ProxyServer.getInstance().getPlayers()) {
                if (pServer != p && Friend.isFriend(pServer.getName(), p.getName())) {
                    switch (connectMessage(disconnect, pServer.getServer().getInfo().getName())) {
                        case 1:
                            pServer.sendMessage(new TextComponent(
                                    Main.msg.getConnectToEqualsLobby(
                                            BPhook.getPrefix(p.getName()) + p.getName()
                                    )));
                            break;
                        case 2:
                            pServer.sendMessage(new TextComponent(
                                    Main.msg.getDisconnectFromServer(
                                            BPhook.getPrefix(p.getName()) + p.getName(), disconnect
                                    )));
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public static int connectMessage(String connect, String target) {
        if (connect.toLowerCase().indexOf("lobby") != -1 && connect.equalsIgnoreCase(target)){
            return 1;
        } else {
            return 2;
        }
    }
}