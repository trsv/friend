package trsv.friend.main;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.joda.time.DateTime;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import trsv.friend.commands.CommandMsg;
import trsv.friend.commands.CommandFriend;
import trsv.friend.data.Message;
import trsv.friend.data.Profile;
import trsv.friend.events.Events;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Main extends Plugin {

    static File FILE_MESSAGES;
    static File FILE_CONFIG;

    public static HashMap<String, String> CONFIG;
    public static HashMap<String, String> listRequest;
    public static HashMap<String, DateTime> listLimit = new HashMap<>(0);
    public static ArrayList<String> listOnce = new ArrayList<String>(0);
    public static Message msg;

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Yaml yaml;
    public static Reader reader;
    public static Writer writer;
    public static Logger logger;

    public static final Type TYPE_PROFILE = new TypeToken<Profile>() {}.getType();
    public static final Type TYPE_FRIEND_LIST = new TypeToken<HashMap<String, Boolean>>() {}.getType();

    @Override
    public void onEnable() {

        logger = getLogger();

        /**
		 * создание файлов
		 */

        final DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        yaml = new Yaml(options);

        try {
            FILE_MESSAGES = new File("plugins/Friend/messageconfig.yml");
            FILE_MESSAGES.getParentFile().mkdirs();
            if (FILE_MESSAGES.createNewFile()) {
                try {
                    writer = new FileWriter(FILE_MESSAGES);
                    yaml.dump(setMessageConfig(), writer);
                    writer.close();
                } catch (FileNotFoundException e) {
                    System.out.println("[Friend] Messages file not found");
                }
            }

            FILE_CONFIG = new File("plugins/Friend/config.yml");
            if (FILE_CONFIG.createNewFile()) {
                try {
                    writer = new FileWriter(FILE_CONFIG);
                    yaml.dump(setConfig(), writer);
                    writer.close();
                } catch (FileNotFoundException e) {
                    System.out.println("[Friend] Config file not found");
                }
            }

        } catch (IOException e) {
            e.getMessage();
        }

        msg = new Message();

		/*
		 * регистрация команд, событий
		 */

        getProxy().getPluginManager().registerCommand(this, new CommandFriend());
        getProxy().getPluginManager().registerCommand(this, new CommandMsg());
        getProxy().getPluginManager().registerListener(this, new Events());

        ProxyServer.getInstance().registerChannel("CPtptop");

        listRequest = new HashMap<>(0);

        loadConfig();
    }

    static Map<String, String> setMessageConfig() {
        Map<String, String> config = new HashMap<>(0);

        config.put("inbox",
                "[Friend] Новая заявка в друзья от %player% \nИспользуйте ");

        config.put("my-friendlist", "[Friend] Ваш список друзей: %list%");

        config.put("player-friendlist", "[Friend] Список друзей игрока %player% %list%");

        config.put("wait", "[Friend] Подождите еще %time%, чтобы отправить новую заявку");

        config.put("send", "[Friend] Заявка игроку %player% отправлена!");

        config.put("accept", "[Friend] %player% стал вашим другом!");

        config.put("remove", "[Friend] %player% убран из друзей!");

        config.put("remove-by-friend", "[Friend] %player% убрал Вас из друзей!");

        config.put("deny-inbox-friend", "[Friend] Прием заявок у этого игрока отключен");

        config.put("empty-inbox", "[Friend] Нет входящих заявок");

        config.put("offline", "[Friend] Этот игрок не в сети");

        config.put("no-friend", "[Friend] У Вас нет этого игрока в друзьях");

        config.put("no-friend-in-lobby", "[Friend] Этот игрок не в Lobby");

        config.put("anonimus-on", "[Friend] Вы перешли в режим невидимки");

        config.put("anonimus-off", "[Friend] Вы отключили режим невидимки");

        config.put("inbox-on", "[Friend] Вы включили прием заявок");

        config.put("inbox-off", "[Friend] Вы отключили прием заявок");

        config.put("private-sender", "[Private] > <%player%> %text%");

        config.put("private-target", "[Private] < <%player%> %text%");

        config.put("hide-friend", "[Friend] Друг %player% скрыт");

        config.put("show-friend", "[Friend] Друг %player% отображен");

        config.put("teleport-to-friend", "[Friend] Вы телепортировались к %player%");

        config.put("teleport", "[Friend] К Вам телепортировался %player%");

        config.put("ignore", "[Friend] Игрок %player% проигнорировал Вашу заявку");

        config.put("deny", "[Friend] Игрок %player% отклонил Вашу заявку");

        config.put("no-private", "[Friend] У этого игрока отключены приватные сообщения");

        config.put("private-on", "[Friend] Вы включили приватные сообщения");

        config.put("private-off", "[Friend] Вы отключили приватные сообщения");

        config.put("conn-to-equals-lobby", "[Friend] Игрок %player% зашел в Ваше Lobby");

        config.put("conn-to-server", "[Friend] Игрок %player% зашел на сервер %server%");

        config.put("disconn-from-server", "[Friend] Игрок %player% вышел с сервера %server%");

        config.put("disconn-from-equals-lobby", "[Friend] Игрок %player% вышел из Вашего Lobby");

        config.put("ignore-sender", "[Friend] Вы проигнорировали заявку от игрока %player%");

        config.put("deny-sender", "[Friend] Вы отклонили заявку от игрока %player%");

        return config;
    }

    static HashMap<String, String> setConfig() {
        HashMap<String, String> config = new HashMap<>(0);

        config.put("source", "../Players");

        return config;
    }

    public static void loadConfig() {
        File fileConfig = new File("plugins/Friend/config.yml");
        try {
            reader = new FileReader(fileConfig);
            CONFIG = (HashMap<String, String>) yaml.load(reader);
            reader.close();
            logger.info("Config loaded");
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static Profile getProfile(String name) {
        File fileprofile = new File(CONFIG.get("source") + "/" + name + "/Friend/profile.json");

        try {
            Reader treader = new FileReader(fileprofile);
            Profile profile = new Gson().fromJson(treader, TYPE_PROFILE);
            treader.close();

            Writer twriter = new FileWriter(fileprofile);
            gson.toJson(profile, twriter);
            twriter.close();

            return profile;
        } catch (IOException e) {
            return null;
        }
    }

    public static void saveProfile(String name, Profile profile) {
        File fileprofile = new File(CONFIG.get("source") + "/" + name + "/Friend/profile.json");

        try {
            Writer twriter = new FileWriter(fileprofile);
            new GsonBuilder().setPrettyPrinting().create().toJson(profile, twriter);
            twriter.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}