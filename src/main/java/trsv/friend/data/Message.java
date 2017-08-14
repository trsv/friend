package trsv.friend.data;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.md_5.bungee.api.ChatColor;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import trsv.friend.main.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class Message {

    static Map<String, String> config;
    static String message;
    static PeriodFormatter FORMAT = new PeriodFormatterBuilder()
            .printZeroAlways()
            .appendMinutes()
            .appendSuffix("m ")
            .appendSeconds()
            .appendSuffix("s")
            .toFormatter();

    public static final String line = ChatColor.WHITE + "------------------------------------------------------------------------";
    public static final String NoPex = line +
            ChatColor.RED + "\n[Friend] У Вас нет на это прав!\n" + line;
    public static final String NO_TP = line + ChatColor.RED + "\n[Friend] Нельзя телепортироваться к себе\n" + line;
    public static final String INVALID = line + ChatColor.RED + "\n[Friend] Неверная команда. Используйте: /friend help для справки\n" + line;

    public Message() {
        try {
            config = (Map<String, String>) Main.yaml.load(new FileReader(new File("plugins/Friend/messageconfig.yml")));
            Main.logger.info("Messages loaded");
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.getMessage();
        }
    }

    public String getInboxRequest(String sender) {
        message = line + "\n" + config.get("inbox").toString() + "\n" + line;

        message = message.replace("%player%", sender);

        return message;
    }

    public String getMyListFriend(String list) {
        message = line + "\n" + config.get("my-friendlist") + "\n" + line;

        message = message.replace("%list%", list);

        return message;
    }

    public String getPlayerListFriend(String player, String list) {
        message = line + "\n" + config.get("player-friendlist") + "\n" + line;

        message = message.replace("%player%", player);
        message = message.replace("%list%", list);

        return message;
    }

    public String getWait30s(DateTime limit) {
        message = line + "\n" + config.get("wait") + "\n" + line;

        Duration duration = new Duration(new DateTime(), limit);

        message = message.replace("%time%", FORMAT.print(duration.toPeriod()));

        return message;
    }

    public String getAcceptRequest(String player) {
        message = line + "\n" + config.get("accept") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getRemove(String player) {
        message = line + "\n" + config.get("remove") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getRemoveByFriend(String player) {
        message = line + "\n" + config.get("remove-by-friend") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getSendRequest(String player) {
        message = line + "\n" + config.get("send") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getDenyInboxFriend() {
        return line + "\n" + config.get("deny-inbox-friend") + "\n" + line;
    }

    public String getEmptyInbox() {
        return line + "\n" + config.get("empty-inbox") + "\n" + line;
    }

    public String getOffline() {
        return line + "\n" + config.get("offline") + "\n" + line;
    }

    public String getNoFriend() {
        return line + "\n" + config.get("no-friend") + "\n" + line;
    }

    public String getNoFriendInLobby() {
        return line + "\n" + config.get("no-friend-in-lobby") + "\n" + line;
    }

    public String getAnonimusOn() {
        return line + "\n" + config.get("anonimus-on") + "\n" + line;
    }

    public String getAnonimusOff() {
        return line + "\n" + config.get("anonimus-off") + "\n" + line;
    }

    public String getPrivateMessageToTarget(String player, String text) {
        message = config.get("private-target");

        message = message.replace("%text%", text);
        message = message.replace("%player%", player);

        return message;
    }

    public String getPrivateMessageToSender(String player, String text) {
        message = config.get("private-sender");

        message = message.replace("%text%", text);
        message = message.replace("%player%", player);

        return message;
    }

    public String getHideFriend(String player) {
        message = line + "\n" + config.get("hide-friend") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getShowFriend(String player) {
        message = line + "\n" + config.get("show-friend") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getTeleport(String player) {
        message = line + "\n" + config.get("teleport") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getTeleportToFriend(String player) {
        message = line + "\n" + config.get("teleport-to-friend") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getInboxOn() {
        return config.get("inbox-on");
    }

    public String getInboxOff() {
        return config.get("inbox-off");
    }

    public String getIgnore(String player) {
        message = line + "\n" + config.get("ignore") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getDeny(String player) {
        message = line + "\n" + config.get("deny") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getNoPrivate() {
        return line + "\n" + config.get("no-private") + "\n" + line;
    }

    public String getConnectToEqualsLobby(String player) {
        message = line + "\n" + config.get("conn-to-equals-lobby") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getDisconnectFromEqualsLobby(String player) {
        message = line + "\n" + config.get("disconn-from-equals-lobby") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getConnectToServer(String player, String server) {
        message = line + "\n" + config.get("conn-to-server") + "\n" + line;

        message = message.replace("%player%", player);
        message = message.replace("%server%", server);

        return message;
    }

    public String getDisconnectFromServer(String player, String server) {
        message = line + "\n" + config.get("disconn-from-server") + "\n" + line;

        message = message.replace("%player%", player);
        message = message.replace("%server%", server);

        return message;
    }

    public String getIgnoreSender(String player) {
        message = line + "\n" + config.get("ignore-sender") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getDenySender(String player) {
        message = line + "\n" + config.get("deny-sender") + "\n" + line;

        message = message.replace("%player%", player);

        return message;
    }

    public String getPrivateMessageOn() {
        return line + "\n" + config.get("private-on") + "\n" + line;
    }

    public String getPrivateMessageOff() {
        return line + "\n" + config.get("private-off") + "\n" + line;
    }
}