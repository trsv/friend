package trsv.friend.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import trsv.friend.data.BPhook;
import trsv.friend.data.Message;
import trsv.friend.data.Profile;
import trsv.friend.main.Main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Администратор on 17.04.2017.
 */
public class CommandMsg extends Command {
    public CommandMsg() {
        super("msg", "friend.chat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length > 1) {
            ProxiedPlayer p = (ProxiedPlayer)sender;

            String message = "";
            for (int i = 1; i < args.length; i++) {
                message += args[i] + " ";
            }

            sendMessage(p, args[0], message);
        } else {
            sender.sendMessage(new TextComponent(Message.INVALID));
        }
    }

    static void sendMessage(ProxiedPlayer sender, String target, String text) {
        ProxiedPlayer pt = ProxyServer.getInstance().getPlayer(target);
        Profile profile_target = Main.getProfile(target);

        if (sender.hasPermission("friend.plus.chat")) {
            chatting(sender, pt, text);
            return;
        }

        if (!profile_target.privatemsg && !sender.hasPermission("friend.plus.chat")) {
            sender.sendMessage(new TextComponent(Main.msg.getNoPrivate()));
            return;
        }

        File ffl = new File(Main.CONFIG.get("source") + "/" + sender.getName() + "/Friend/friendlist.json");

        try {
            Main.reader = new FileReader(ffl);
            HashMap <String, Boolean> fl = Main.gson.fromJson(Main.reader, Main.TYPE_FRIEND_LIST);
            Main.reader.close();

            for (HashMap.Entry <String, Boolean> friend : fl.entrySet()) {
                if (friend.getKey().equals(target)) {
                    chatting(sender, pt, text);
                    return;
                }
            }
            sender.sendMessage(new TextComponent(Main.msg.getNoFriend()));
        } catch (IOException e) {
            e.getMessage();
        }
    }

    static void chatting(ProxiedPlayer sender, ProxiedPlayer target, String text) {
        if (target == null) {
            sender.sendMessage(new TextComponent(Main.msg.getOffline()));
            return;
        } else {
            String sprefix = BPhook.getPrefix(sender.getName()).replaceAll("&", "§") + sender.getName();
            String starget = BPhook.getPrefix(target.getName()).replaceAll("&", "§") + target.getName();

            sender.sendMessage(new TextComponent(Main.msg.getPrivateMessageToSender(starget, text)));
            target.sendMessage(new TextComponent(Main.msg.getPrivateMessageToTarget(sprefix, text)));
        }
    }
}