package trsv.friend.data;

import net.alpenblock.bungeeperms.BungeePerms;
import net.alpenblock.bungeeperms.PermissionsManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Created by GFY on 01.05.2017.
 */
public class BPhook {

    public static BungeePerms bp_hook;

    public static String getPrefix(String name) {

        bp_hook = BungeePerms.getInstance();

        if (bp_hook == null) {
            ProxyServer.getInstance().getLogger().warning(ChatColor.RED + "ERROR > Error while trying to gain data from BungeePerms... is it installed?");
            return "";
        }
        PermissionsManager pm = bp_hook.getPermissionsManager();
        net.alpenblock.bungeeperms.User user = pm.getUser(name);
        if (user == null) return "§e";
        else return user.buildPrefix().replaceAll("&", "§");
    }

}
