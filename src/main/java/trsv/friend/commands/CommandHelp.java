package trsv.friend.commands;

import trsv.friend.data.Message;

/**
 * Created by Администратор on 25.04.2017.
 */
public class CommandHelp {
    final static String HEADER_1 = Message.line + "\nСписок команд плагина Friend. Страница ";
    final static String HEADER_2 = " из 3";
    final static String FOOTER = "\n" + Message.line;
    final static String HELP_1 =
                    "\n/msg <игрок> - приватное сообщение другу" +
                    "\n/friend on - включить прием заявок в друзья" +
                    "\n/friend off - выключить прием заявок в друзья" +
                    "\n/friend add <игрок> - добавить игрока в друзья" +
                    "\n/friend accept - принять входящую заявку в друзья" +
                    "\n/friend ignore - игнорировать входящую заявку в друзья" +
                    "\n/friend deny - отклонить входящую заявку в друзья";

    final static String HELP_2 =
                    "\n/friend remove <игрок> - удалить игрока из друзей" +
                    "\n/friend list - список своих друзей" +
                    "\n/friend list <игрок> - список друзей игрока" +
                    "\n/friend hide <игрок> - скрыть игрока в списке друзей" +
                    "\n/friend show <игрок> - отобразить игрока в списке друзей" +
                    "\n/friend online - выключить режим невидимки" +
                    "\n/friend offline - включить режим невидимки";

    final static String HELP_3 =
                    "\n/friend teleport | tp <игрок> - телепорт к игроку в Lobby" +
                    "\n/friend ignore on - отключить приватные сообщения" +
                    "\n/friend ignore off - включить приватные сообщения" +
                    "\n/friend hide <игрок> - скрыть игрока в списке друзей" +
                    "\n/friend show <игрок> - отобразить игрока в списке друзей" +
                    "\n/friend reload - перезагрузить плагин";

    public static String getHelp(String args[]) {
        if (args.length == 1 || args[1].equals("1")) return getFromPart("1", HELP_1);
        else if (args[1].equals("2")) return  getFromPart("2", HELP_2);
        else if (args[1].equals("3")) return  getFromPart("3", HELP_3);
        else return getFromPart("1", HELP_1);
    }

    private static String getFromPart(String index, String part) {
        return HEADER_1 + index + HEADER_2 + part + FOOTER;
    }
}
