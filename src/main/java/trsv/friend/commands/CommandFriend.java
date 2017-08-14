package trsv.friend.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.joda.time.DateTime;
import trsv.friend.data.BPhook;
import trsv.friend.data.Friend;
import trsv.friend.data.Message;
import trsv.friend.data.Profile;
import trsv.friend.main.Main;

import java.io.*;
import java.util.HashMap;

/**
 * Created by Администратор on 11.04.2017.
 */
public class CommandFriend extends Command {
    public CommandFriend() {
        super("friend");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            pluginReload(sender);
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;

        if (args.length == 0) {
            p.sendMessage(new TextComponent("§bПлагин Friend. Автор Some. Специально для §cMemas§6Gold\n§bИспользуйте /friend help для справки"));
        } else {
            if (args[0].equalsIgnoreCase("help")) {
                p.sendMessage(new TextComponent(CommandHelp.getHelp(args)));
            }
            // list of friends
            else if (args[0].equalsIgnoreCase("list")) {
                if (args.length == 1) {
                    if (p.hasPermission("friend.list")) {
                        p.sendMessage(new TextComponent(Main.msg.getMyListFriend(getFriendList(p, p.getName(), true))));
                    } else {
                        p.sendMessage(new TextComponent(Message.NoPex));
                    }
                } else if (args.length > 1) {
                    if (p.hasPermission("friend.list.player")) {
                        p.sendMessage(new TextComponent(Main.msg.getPlayerListFriend(args[1], getFriendList(p, args[1], false))));
                    } else {
                        p.sendMessage(new TextComponent(Message.NoPex));
                    }
                }
                // add friend
            } else if (args[0].equalsIgnoreCase("add") && args.length > 1) {
                if (p.hasPermission("friend.add")) {
                    if (!args[1].equalsIgnoreCase(p.getName())) {
                        try {
                            Profile profile = Main.getProfile(args[1]);
                            if (profile.canInbox) {
                                if (!Main.listRequest.containsKey(args[1])) {
                                    sendRequest(p, args[1]);
                                } else {
                                    DateTime limit = Main.listLimit.get(args[1]);
                                    p.sendMessage(new TextComponent(Main.msg.getWait30s(limit)));
                                }
                            } else {
                                p.sendMessage(
                                        new TextComponent(Main.msg.getDenyInboxFriend()));
                            }
                        } catch (NullPointerException e) {
                            p.sendMessage(new TextComponent(Main.msg.getOffline()));
                        }
                    } else {
                        p.sendMessage(new TextComponent(ChatColor.RED + Message.line + "\n[Friend] Нельзя добавить самого себя в друзья\n" + Message.line));
                    }
                } else {
                    p.sendMessage(new TextComponent(Message.NoPex));
                }
                // accept request
            } else if (args[0].equalsIgnoreCase("accept")) {
                if (p.hasPermission("friend.accept")) {
                    if (Main.listRequest.containsKey(p.getName())) {
                        String newfriend = Main.listRequest.remove(p.getName());
                        Main.listLimit.remove(p.getName());
                        acceptRequest(p, newfriend);
                    } else {
                        p.sendMessage(new TextComponent(Main.msg.getEmptyInbox()));
                    }
                } else {
                    p.sendMessage(new TextComponent(Message.NoPex));
                }
                // ignore
            } else if (args[0].equalsIgnoreCase("ignore")) {

                if (args.length == 1) {
                    ignore(p);
                } else {
                    if (args[1].equalsIgnoreCase("off")) setPrivateMsg(p, true);
                    else if (args[1].equalsIgnoreCase("on")) setPrivateMsg(p, false);
                }
                // deny request
            } else if (args[0].equalsIgnoreCase("deny")) {
                if (p.hasPermission("friend.deny")) {
                    deny(p);
                } else {
                    p.sendMessage(new TextComponent(Message.NoPex));
                }
            }
            // remove friend
            else if (args[0].equalsIgnoreCase("remove") && args.length > 1) {
                if (p.hasPermission("friend.remove")) {
                    removeFriend(p, args[1]);
                } else {
                    p.sendMessage(new TextComponent(Message.NoPex));
                }
                // teleport
            } else if ((args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) && args.length > 1) {
                if (p.hasPermission("friend.teleport")) {
                    teleport(p, args[1]);
                } else {
                    p.sendMessage(new TextComponent(Message.NoPex));
                }
                // on
            } else if (args[0].equalsIgnoreCase("on")) {
                if (p.hasPermission("friend.on")) {
                    setCanInbox(p, true);
                } else {
                    p.sendMessage(new TextComponent(Message.NoPex));
                }
                // off
            } else if (args[0].equalsIgnoreCase("off")) {
                if (p.hasPermission("friend.off")) {
                    setCanInbox(p, false);
                } else {
                    p.sendMessage(new TextComponent(Message.NoPex));
                }
            }
            // offline
            else if (args[0].equalsIgnoreCase("offline")) {
                if (p.hasPermission("friend.offline")) {
                    setAnonimus(p, true);
                } else {
                    p.sendMessage(new TextComponent(Message.NoPex));
                }
                // online
            } else if (args[0].equalsIgnoreCase("online")) {
                if (p.hasPermission("friend.online")) {
                    setAnonimus(p, false);
                } else {
                    p.sendMessage(new TextComponent(Message.NoPex));
                }
                // hide
            } else if (args[0].equalsIgnoreCase("hide") && args.length > 1) {
                if (p.hasPermission("friend.hide")) {
                    setVisibleFriend(p, args[1], false);
                } else {
                    p.sendMessage(new TextComponent(Message.NoPex));
                }
                // show
            } else if (args[0].equalsIgnoreCase("show") && args.length > 1) {
                if (p.hasPermission("friend.show")) {
                    setVisibleFriend(p, args[1], true);
                } else {
                    p.sendMessage(new TextComponent(Message.NoPex));
                }
            } else {
                p.sendMessage(new TextComponent(Message.INVALID));
            }
        }

    }

    private void setPrivateMsg(ProxiedPlayer sender, boolean value) {
        if (!sender.hasPermission("friend.chat.turn")) {
            sender.sendMessage(new TextComponent(Message.NoPex));
            return;
        }

        Profile profile = Main.getProfile(sender.getName());
        profile.privatemsg = value;
        Main.saveProfile(sender.getName(), profile);

        if (value) sender.sendMessage(new TextComponent(Main.msg.getPrivateMessageOn()));
        else sender.sendMessage(new TextComponent(Main.msg.getPrivateMessageOff()));
    }

    static String getFriendList(ProxiedPlayer sender, String name, boolean owner) {
        File fl = new File(Main.CONFIG.get("source") + "/" + name + "/Friend/friendlist.json");

        String list = "";

        try {
            Main.reader = new FileReader(fl);
            HashMap<String, Boolean> friends = Main.gson.fromJson(Main.reader, Main.TYPE_FRIEND_LIST);
            Main.reader.close();

            if (!friends.isEmpty()) {
                for (HashMap.Entry<String, Boolean> friend : friends.entrySet()) {
                    String nickname = friend.getKey();
                    Profile profile = Main.getProfile(nickname);

                    if (profile == null) {
                        friends.remove(nickname);
                        continue;
                    }

                    if (owner) {
                        if (profile.anonimus && !sender.hasPermission("friend.list.showanonimus")) {
                            list += Friend.printToListGray(nickname);
                        } else {
                            list += Friend.printToList(nickname, profile.lastOnline);
                        }
                    } else {
                        if (!friend.getValue().booleanValue() && !sender.hasPermission("friend.list.showhiden")) continue;

                        if (profile.anonimus && !sender.hasPermission("friend.list.showanonimus")) {
                            list += Friend.printToListGray(nickname);
                        } else {
                            list += Friend.printToList(nickname, profile.lastOnline);
                        }
                    }
                }
            } else {
                list = "У Вас нет друзей";
            }
        } catch (java.io.IOException e) {
            e.getMessage();
        }
        return list;
    }


    void sendRequest(final ProxiedPlayer sender, final String target) {
        ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
        if (t == null) {
            sender.sendMessage(new TextComponent(Main.msg.getOffline()));
            return;
        }

        File fl = new File(Main.CONFIG.get("source") + "/" + sender.getName() + "/Friend/friendlist.json");

        try {
            Main.reader = new FileReader(fl);
            HashMap<String, Boolean> friends = Main.gson.fromJson(Main.reader, Main.TYPE_FRIEND_LIST);
            Main.reader.close();

            if (friends.containsKey(target)){
                sender.sendMessage(new TextComponent("У вас уже есть этот друг"));
                return;
            }
        } catch (IOException e) {
            e.getMessage();
        }

        final String prefixTarget = BPhook.getPrefix(target) + t.getName();
        String prefixSender = BPhook.getPrefix(sender.getName()) + sender.getName();

        // помечаем, что получатель ждет принятие заявки
        Main.listRequest.put(target, sender.getName());
        Main.listLimit.put(target, new DateTime().plusMinutes(5));

        // уведомляем отправителя и получателя
        TextComponent accept = new TextComponent("[Принять]");
        accept.setColor(ChatColor.GREEN);
        accept.setBold(true);
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Принять заявку в друзья").create()));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept"));

        TextComponent ignore = new TextComponent("[Игнорировать]");
        ignore.setColor(ChatColor.GRAY);
        ignore.setBold(true);
        ignore.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Игнорировать заявку в друзья").create()));
        ignore.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend ignore"));

        TextComponent deny = new TextComponent("[Отклонить]");
        deny.setColor(ChatColor.RED);
        deny.setBold(true);
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Отклонить заявку в друзья").create()));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny"));

        t.sendMessage(new TextComponent(Main.msg.getInboxRequest(prefixSender)),
                accept,
                new TextComponent(" - "),
                ignore,
                new TextComponent(" - "),
                deny);
        sender.sendMessage(new TextComponent(Main.msg.getSendRequest(prefixTarget)));

        // через 5 минут можно снова отправить заявку
        Thread threadSendRequest = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(180000);
                    if (Main.listRequest.containsKey(target)) {
                        Main.listRequest.remove(target);
                        Main.listLimit.remove(target);
                        sender.sendMessage(new TextComponent(Main.msg.getIgnore(prefixTarget)));
                    }
                } catch (InterruptedException e) {
                    System.out.println("Error in 5sm wait");
                }
            }
        });
        threadSendRequest.start();
    }

    static void acceptRequest(ProxiedPlayer sender, String target) {
        File senderFL = new File(Main.CONFIG.get("source") + "/" + sender.getName() + "/Friend/friendlist.json");
        File targetFL = new File(Main.CONFIG.get("source") + "/" + target + "/Friend/friendlist.json");

        try {
            Main.reader = new FileReader(senderFL);
            HashMap<String, Boolean> sFL = Main.gson.fromJson(Main.reader, Main.TYPE_FRIEND_LIST);
            if (sFL == null) sFL = new HashMap<>(0);
            Main.reader.close();

            Main.reader = new FileReader(targetFL);
            HashMap<String, Boolean> tFL = Main.gson.fromJson(Main.reader, Main.TYPE_FRIEND_LIST);
            if (tFL == null) tFL = new HashMap<>(0);
            Main.reader.close();

            ProxiedPlayer pTarget = ProxyServer.getInstance().getPlayer(target);

            String prefixTarget = BPhook.getPrefix(pTarget.getName()) + pTarget.getName();
            String prefixSender = BPhook.getPrefix(sender.getName()) + sender.getName();

            sFL.put(pTarget.getName(), true);
            tFL.put(sender.getName(), true);

            Main.writer = new FileWriter(senderFL);
            Main.gson.toJson(sFL, Main.writer);
            Main.writer.close();

            Main.writer = new FileWriter(targetFL);
            Main.gson.toJson(tFL, Main.writer);
            Main.writer.close();

            sender.sendMessage(new TextComponent(Main.msg.getAcceptRequest(prefixTarget)));
            pTarget.sendMessage(new TextComponent(Main.msg.getAcceptRequest(prefixSender)));

        } catch (IOException e) {
            e.getMessage();
        } catch (NullPointerException e) {
            sender.sendMessage(new TextComponent(Main.msg.getOffline()));
        }
    }

    static void removeFriend(ProxiedPlayer sender, String target) {
        File senderFL = new File(Main.CONFIG.get("source") + "/" + sender.getName() + "/Friend/friendlist.json");
        File targetFL = new File(Main.CONFIG.get("source") + "/" + target + "/Friend/friendlist.json");

        try {
            Main.reader = new FileReader(senderFL);
            HashMap<String, Boolean> sFL = Main.gson.fromJson(Main.reader, Main.TYPE_FRIEND_LIST);
            if (sFL == null) {
                sender.sendMessage(new TextComponent(Main.msg.getNoFriend()));
                return;
            }
            Main.reader.close();

            Main.reader = new FileReader(targetFL);
            HashMap<String, Boolean> tFL = Main.gson.fromJson(Main.reader, Main.TYPE_FRIEND_LIST);
            if (tFL == null) tFL = new HashMap<>(0);
            Main.reader.close();

            boolean find = false;

            for (HashMap.Entry<String, Boolean> friend : sFL.entrySet()) {
                if (friend.getKey().equalsIgnoreCase(target)) {
                    find = true;
                    sFL.remove(friend.getKey());
                    break;
                }
            }

            for (HashMap.Entry<String, Boolean> friend : tFL.entrySet()) {
                if (friend.getKey().equalsIgnoreCase(sender.getName())) {
                    find = true;
                    tFL.remove(friend.getKey());
                    break;
                }
            }

            if (find) {
                Main.writer = new FileWriter(senderFL);
                Main.gson.toJson(sFL, Main.writer);
                Main.writer.close();

                Main.writer = new FileWriter(targetFL);
                Main.gson.toJson(tFL, Main.writer);
                Main.writer.close();

                sender.sendMessage(new TextComponent(Main.msg.getRemove(BPhook.getPrefix(target) + target)));

                try {
                    ProxyServer.getInstance().getPlayer(target).sendMessage(new TextComponent(
                            Main.msg.getRemoveByFriend(BPhook.getPrefix(sender.getName()) + sender.getName())));
                } catch (NullPointerException e) {}
            } else {
                sender.sendMessage(new TextComponent(Main.msg.getNoFriend()));
            }

        } catch (IOException e) {
            e.getMessage();
        }
    }

    static void teleport(ProxiedPlayer sender, String target) {
        try {
            if (sender.getName().equalsIgnoreCase(target)) {
                sender.sendMessage(new TextComponent(Message.NO_TP));
                return;
            }

            ProxiedPlayer pt = ProxyServer.getInstance().getPlayer(target);
            ServerInfo server = pt.getServer().getInfo();
            if (server.getName().toLowerCase().indexOf("lobby") != -1) {

                String prefixSender = BPhook.getPrefix(sender.getName()) + sender.getName();
                String prefixTarget = BPhook.getPrefix(pt.getName()) + pt.getName();

                sender.connect(server);
                sendToBukkit("CPtptop", sender.getName() + "/" + pt.getName(), server);

                sender.sendMessage(new TextComponent(Main.msg.getTeleportToFriend(prefixTarget)));
                pt.sendMessage(new TextComponent(Main.msg.getTeleport(prefixSender)));
            } else {
                sender.sendMessage(new TextComponent(Main.msg.getNoFriendInLobby()));
            }
        } catch (NullPointerException e) {
            sender.sendMessage(new TextComponent(Main.msg.getOffline()));
        }
    }

    static void setCanInbox(ProxiedPlayer sender, boolean value) {
        File fileProfile = new File(Main.CONFIG.get("source") + "/" + sender.getName() + "/Friend/profile.json");

        Profile profile = Main.getProfile(sender.getName());

        profile.canInbox = value;

        try {
            Main.writer = new FileWriter(fileProfile);
            Main.gson.toJson(profile, Main.writer);
            Main.writer.close();

            if (value) sender.sendMessage(new TextComponent(Main.msg.getInboxOn()));
            else sender.sendMessage(new TextComponent(Main.msg.getInboxOff()));
        } catch (IOException e) {
            e.getMessage();
        }
    }

    static void setAnonimus(ProxiedPlayer sender, boolean value) {
        File fileProfile = new File(Main.CONFIG.get("source") + "/" + sender.getName() + "/Friend/profile.json");

        Profile profile = Main.getProfile(sender.getName());

        profile.anonimus = value;

        try {
            Main.writer = new FileWriter(fileProfile);
            Main.gson.toJson(profile, Main.writer);
            Main.writer.close();

            if (value) sender.sendMessage(new TextComponent(Main.msg.getAnonimusOn()));
            else sender.sendMessage(new TextComponent(Main.msg.getAnonimusOff()));
        } catch (IOException e) {
            e.getMessage();
        }
    }

    static void setVisibleFriend(ProxiedPlayer sender, String target, boolean value) {
        File senderFL = new File(Main.CONFIG.get("source") + "/" + sender.getName() + "/Friend/friendlist.json");

        try {
            Main.reader = new FileReader(senderFL);
            HashMap<String, Boolean> sFL = Main.gson.fromJson(Main.reader, Main.TYPE_FRIEND_LIST);
            if (sFL == null) sFL = new HashMap<>(0);
            Main.reader.close();

            boolean find = false;
            for (HashMap.Entry<String, Boolean> friend : sFL.entrySet()) {
                if (friend.getKey().equalsIgnoreCase(target)) {
                    sFL.put(friend.getKey(), value);
                    find = true;

                    Main.writer = new FileWriter(senderFL);
                    Main.gson.toJson(sFL, Main.writer);
                    Main.writer.close();

                    if (value) sender.sendMessage(new TextComponent(Main.msg.getShowFriend(target)));
                    else sender.sendMessage(new TextComponent(Main.msg.getHideFriend(target)));
                }
            }

            if (!find) {
                sender.sendMessage(new TextComponent(Main.msg.getNoFriend()));
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    static void sendToBukkit(String channel, String message, ServerInfo server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(channel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.getMessage();
        }
        server.sendData("CPtptop", stream.toByteArray());
    }

    static void deny(ProxiedPlayer sender) {
        if (Main.listRequest.containsKey(sender.getName())) {
            String target = Main.listRequest.get(sender.getName());
            Main.listRequest.remove(sender.getName());
            Main.listLimit.remove(sender.getName());
            ProxiedPlayer ptarget = ProxyServer.getInstance().getPlayer(target);
            ptarget.sendMessage(new TextComponent(Main.msg.getDeny(BPhook.getPrefix(sender.getName()) + sender.getName())));
            sender.sendMessage(new TextComponent(Main.msg.getDenySender(BPhook.getPrefix(ptarget.getName()) + ptarget.getName())));
        } else {
            sender.sendMessage(new TextComponent(Main.msg.getEmptyInbox()));
        }
    }

    static void ignore(ProxiedPlayer sender) {

        if (!sender.hasPermission("friend.ignore")) {
            sender.sendMessage(new TextComponent(Message.NoPex));
            return;
        }

        if (Main.listRequest.containsKey(sender.getName())) {
            String target = Main.listRequest.get(sender.getName());
            ProxiedPlayer ptarget = ProxyServer.getInstance().getPlayer(target);
            ptarget.sendMessage(new TextComponent(Main.msg.getIgnore(BPhook.getPrefix(sender.getName()) + sender.getName())));
            sender.sendMessage(new TextComponent(Main.msg.getIgnoreSender(BPhook.getPrefix(ptarget.getName()) + ptarget.getName())));
        } else {
            sender.sendMessage(new TextComponent(Main.msg.getEmptyInbox()));
        }

    }

    static void pluginReload(CommandSender sender) {
        Main.loadConfig();
        Main.msg = new Message();
        sender.sendMessage(new TextComponent(ChatColor.GREEN + "[Friend] Plugin reloaded"));
    }
}