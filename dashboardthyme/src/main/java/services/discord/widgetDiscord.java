package services.discord;

public class widgetDiscord {

    public static void main(String[] args) {
        // TEST
        System.out.println(widgetDiscord.iFrameUserConnect(350, 500, "755024047284158514", true));
    }

    public static String iFrameUserConnect(int width, int height, String idServeur, boolean darkMode) {
        String theme = "light";
        if (darkMode) {
            theme = "dark";
        }
        return "<iframe src=\"https://discord.com/widget?id=" + idServeur + "&theme=" + theme + "\" width=\"" + width + "\" height=\"" + height + "\" allowtransparency=\"true\" frameborder=\"0\" sandbox=\"allow-popups allow-popups-to-escape-sandbox allow-same-origin allow-scripts\"></iframe>";
    }

}
