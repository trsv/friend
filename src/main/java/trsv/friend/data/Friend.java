package trsv.friend.data;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import trsv.friend.main.Main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by GFY on 01.05.2017.
 */
public class Friend {

    public static String printToListGray (String target) {
        String tprefix = BPhook.getPrefix(target) + target;
        return "\n" + ChatColor.GRAY + tprefix;
    }

    public static String printToList(String target, String time) {

        try {
            return "\n" + BPhook.getPrefix(target) + ChatColor.BLUE + target + " online на сервере "
                    + ProxyServer.getInstance().getPlayer(target).getServer().getInfo().getName();
        } catch (NullPointerException e) {
            return "\n" + BPhook.getPrefix(target) + ChatColor.RED + target + " offline, был в сети " + time;
        }
    }

    public static boolean isFriend(String owner, String name) {
        File ffl = new File(Main.CONFIG.get("source") + "/" + owner +  "/Friend/friendlist.json");

        try {
            Main.reader = new FileReader(ffl);
            HashMap <String, Boolean> fl = Main.gson.fromJson(Main.reader, Main.TYPE_FRIEND_LIST);
            Main.reader.close();

            return fl.containsKey(name);
        } catch (IOException e) {
            e.getMessage();
            return false;
        }
    }
}
