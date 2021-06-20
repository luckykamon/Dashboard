package dashthyme.dashboard;

import javax.servlet.http.HttpServlet;
import java.util.*;

public class Attributes extends HttpServlet {

    // USER
    private ArrayList<Integer> idUser = new ArrayList<>();
    private ArrayList<String> pseudoUser = new ArrayList<>();

    // WIDGET ID PREFERENCES
    private ArrayList<Integer> idPreference = new ArrayList<>();
    private ArrayList<String> typeWidget = new ArrayList<>();

    // DISCORD ---------------------------------- DISCORD --------------------------------------------- DISCORD
    private ArrayList<Boolean> discordActivated = new ArrayList<>();
    private ArrayList<String> discordIdServer = new ArrayList<>();
    private ArrayList<String> discordDarkMode = new ArrayList<>();


    // YOUTUBE ---------------------------------- YOUTUBE --------------------------------------------- YOUTUBE

    // Widget vues d'une chaine
    private ArrayList<Boolean> youtubeVueChannelActivated = new ArrayList<>();
    private ArrayList<String> youtubeVueChannelURL = new ArrayList<>();

    // Widget vues d'une video
    private ArrayList<Boolean> youtubeVueVideoActivated = new ArrayList<>();
    private ArrayList<String> youtubeVueVideoURL = new ArrayList<>(); // URL de la Video de laquelle on veut le nombre de vues

    private int youtubeVueVideoWidth = 350;
    private int getYoutubeVueVideoHeight = 500;
    // Widget playlist iframe
    private ArrayList<Boolean> youtubePlaylistActivated = new ArrayList<>();
    private ArrayList<String> youtubePlaylistURL = new ArrayList<>();

    //Widget commentaires youtube
    private ArrayList<Boolean> youtubeCommentActivated = new ArrayList<>();
    private ArrayList<String> youtubeCommentURL = new ArrayList<>(); // URL de la vidéo de laquelle on veut les commentaires


    // AMAZON ----------------------------------- AMAZON ---------------------------------------------- AMAZON
    private ArrayList<Boolean> amazonActivated = new ArrayList<>();
    private ArrayList<String> amazonTitreRecherche = new ArrayList<>();


    // WEATHER ---------------------------------- WEATHER --------------------------------------------- WEATHER

    // Widget temperatures
    private ArrayList<Boolean> weatherTempActivated = new ArrayList<>();
    private ArrayList<Integer> weatherTempDays = new ArrayList<>();
    private int weatherTempWidth = 300;
    private int weatherTempHeight = 300;
    private ArrayList<String> weatherTempVille = new ArrayList<>();


    // Widget pictogramme
    private ArrayList<Boolean> weatherPictoActivated = new ArrayList<>();
    private ArrayList<Integer> weatherPictoDays = new ArrayList<>();
    private int weatherPictoWidth = 300;
    private int weatherPictoHeight = 300;
    private ArrayList<String> weatherPictoVille = new ArrayList<>();


    // GOOGLE ---------------------------------- GOOGLE --------------------------------------------- GOOGLE
    private ArrayList<Boolean> googleCalendarActivated = new ArrayList<>();
    private ArrayList<Boolean> googleDriveActivated = new ArrayList<>();
    private ArrayList<String> googleCalendarFormat = new ArrayList<>();
    private ArrayList<String> googleDriveURL = new ArrayList<>();


    // TIMER ------------------------------------- TIMER ----------------------------------------------- TIMER
    private ArrayList<Integer> widgetTimer1 = new ArrayList<>();
    private ArrayList<Integer> widgetTimer2 = new ArrayList<>();
    private ArrayList<Integer> widgetTimer3 = new ArrayList<>();
    private ArrayList<Integer> widgetTimer4 = new ArrayList<>();

    // GETTERs  ----------------------------------GETTERS-----------------------------------------------GETTERS
    // USER

    public ArrayList<Integer> getIdUser() {
        return idUser;
    }

    public ArrayList<String> getPseudoUser() {
        return pseudoUser;
    }
// ID PREFERENCE

    public ArrayList<Integer> getIdPreference() {
        return idPreference;
    }
    public ArrayList<String> getTypeWidget() {
        return typeWidget;
    }

    // DISCORD
    public ArrayList<Boolean> isDiscordActivated() {
        return discordActivated;
    }

    public ArrayList<String> getDiscordIdServer() {
        return discordIdServer;
    }

    public ArrayList<String> getDiscordDarkMode() {
        return discordDarkMode;
    }

    // YOUTUBE
    public ArrayList<String> getYoutubeCommentURL() {
        return youtubeCommentURL;
    }

    public ArrayList<Boolean> isYoutubeVueChannelActivated() {
        return youtubeVueChannelActivated;
    }

    public ArrayList<String> getYoutubeVueChannelURL() {
        return youtubeVueChannelURL;
    }

    public ArrayList<Boolean> isYoutubeVueVideoActivated() {
        return youtubeVueVideoActivated;
    }

    public ArrayList<String> getYoutubeVueVideoURL() {
        return youtubeVueVideoURL;
    }

    public int getYoutubeVueVideoWidth() {
        return youtubeVueVideoWidth;
    }

    public int getGetYoutubeVueVideoHeight() {
        return getYoutubeVueVideoHeight;
    }

    public ArrayList<Boolean> isYoutubePlaylistActivated() {
        return youtubePlaylistActivated;
    }

    public ArrayList<String> getYoutubePlaylistURL() {
        return youtubePlaylistURL;
    }

    public ArrayList<Boolean> isYoutubeCommentActivated() {
        return youtubeCommentActivated;
    }

    // AMAZON
    public ArrayList<Boolean> isAmazonActivated() {
        return amazonActivated;
    }

    public ArrayList<String> getAmazonTitreRecherche() {
        return amazonTitreRecherche;
    }

    // WEATHER
    public ArrayList<Boolean> isWeatherTempActivated() {
        return weatherTempActivated;
    }

    public ArrayList<Integer> getWeatherTempDays() {
        return weatherTempDays;
    }

    public int getWeatherTempWidth() {
        return weatherTempWidth;
    }

    public int getWeatherTempHeight() {
        return weatherTempHeight;
    }

    public ArrayList<String> getWeatherTempVille() {
        return weatherTempVille;
    }

    public ArrayList<Boolean> isWeatherPictoActivated() {
        return weatherPictoActivated;
    }

    public ArrayList<Integer> getWeatherPictoDays() {
        return weatherPictoDays;
    }

    public int getWeatherPictoWidth() {
        return weatherPictoWidth;
    }

    public int getWeatherPictoHeight() {
        return weatherPictoHeight;
    }

    public ArrayList<String> getWeatherPictoVille() {
        return weatherPictoVille;
    }

    public ArrayList<Integer> getWidgetTimer1() {
        return widgetTimer1;
    }

    public ArrayList<Integer> getWidgetTimer2() {
        return widgetTimer2;
    }

    public ArrayList<Integer> getWidgetTimer3() {
        return widgetTimer3;
    }

    public ArrayList<Integer> getWidgetTimer4() {
        return widgetTimer4;
    }

    // GOOGLE

    public ArrayList<Boolean> isGoogleCalendarActivated() {
        return googleCalendarActivated;
    }

    public ArrayList<Boolean> isGoogleDriveActivated() {
        return googleDriveActivated;
    }

    public ArrayList<String> getGoogleCalendarFormat() {
        return googleCalendarFormat;
    }

    public ArrayList<String> getGoogleDriveURL() {
        return googleDriveURL;
    }


    // ADDs ----------------------------- ADD to arraylist ------------------------------------------------ADDs
    public void addIdUser(Integer element) {
        this.idUser.add(element);
    }

    public void addPseudoUser(String element) {
        this.pseudoUser.add(element);
    }

    public void addIdPreference(Integer element) {
        this.idPreference.add(element);
    } //id du Widget en question

    public void addTypeWidget(String element) {this.typeWidget.add(element);} // type de widget (youtube, discord, drive, calendar, amazon, weather)

    public void addDiscordActivated(Boolean element) {
        this.discordActivated.add(element);
    }

    public void addDiscordIdServer(String element) {
        this.discordIdServer.add(element);
    }

    public void addDiscordDarkMode(String element) {
        this.discordDarkMode.add(element);
    }

    public void addYoutubeCommentActivated(Boolean element) {
        this.youtubeCommentActivated.add(element);
    }

    public void addYoutubeCommentURL(String element) {
        this.youtubeCommentURL.add(element);
    }

    public void addYoutubeVueChannelActivated(Boolean element) {
        this.youtubeVueChannelActivated.add(element);
    }

    public void addYoutubeVueChannelURL(String element) {
        this.youtubeVueChannelURL.add(element);
    }

    public void addYoutubeVueVideoActivated(Boolean element) {
        this.youtubeVueVideoActivated.add(element);
    }

    public void addYoutubeVueVideoURL(String element) {
        this.youtubeVueVideoURL.add(element);
    }

    public void addYoutubePlaylistActivated(Boolean element) {
        this.youtubePlaylistActivated.add(element);
    }

    public void addYoutubePlaylistURL(String element) {
        this.youtubePlaylistURL.add(element);
    }


    public void addAmazonActivated(Boolean element) {
        this.amazonActivated.add(element);
    }

    public void addAmazonTitreRecherche(String element) {
        this.amazonTitreRecherche.add(element);
    }

    public void addWeatherTempActivated(Boolean element) {
        this.weatherTempActivated.add(element);
    }

    public void addWeatherTempDays(Integer element) {
        this.weatherTempDays.add(element);
    }

    public void addWeatherTempVille(String element) {
        this.weatherTempVille.add(element);
    }

    public void addWeatherPictoActivated(Boolean element) {
        this.weatherPictoActivated.add(element);
    }

    public void addWeatherPictoDays(Integer element) {
        this.weatherPictoDays.add(element);
    }

    public void addWeatherPictoVille(String element) {
        this.weatherPictoVille.add(element);
    }

    public void addWidgetTimer1(Integer element) {
        this.widgetTimer1.add(element);
    }

    public void addWidgetTimer2(Integer element) {
        this.widgetTimer2.add(element);
    }

    public void addWidgetTimer3(Integer element) {
        this.widgetTimer3.add(element);
    }

    public void addWidgetTimer4(Integer element) {
        this.widgetTimer4.add(element);
    }

    public void addGoogleCalendarActivated(Boolean element) {
        this.googleCalendarActivated.add(element);
    }

    public void addGoogleDriveActivated(Boolean element) {
        this.googleDriveActivated.add(element);
    }

    public void addGoogleCalendarFormat(String element) {
        this.googleCalendarFormat.add(element);
    }

    public void addGoogleDriveURL(String element) {
        this.googleDriveURL.add(element);
    }


}
