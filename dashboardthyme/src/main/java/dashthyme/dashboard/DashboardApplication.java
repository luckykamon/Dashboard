package dashthyme.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import services.amazon.widgetAmazon;
import services.google.widgetCalendar;
import services.google.widgetDrive;
import services.google.widgetYoutube;
import services.weather.widgetWeather;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
@RestController
public class DashboardApplication extends Attributes {

    public static void main(String[] args) {
        SpringApplication.run(DashboardApplication.class, args);
    }

    /**
     * @param cookies      Liste des cookies
     * @param cookieName   Nom du cookie dont on souhaite obtenir la valeur
     * @param defaultValue valeur par défault renvoyé si l'on ne trouve pas le cookie avec le nom cookieName
     * @return la valeur du cookie ou defaultValue si le cookie n'as pas été trouvé
     */
    public static String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName()))
                    return (cookie.getValue());
            }
        }
        return defaultValue;
    }

    /**
     * @param i Un numéro pour identifier un widget
     * @return Le code html permetttant de supprimer un cookie
     */
    protected String deleteThisWidget(int i) {
        return "" +
                "<form action=\"/deleteWidget\" class=\"text-right col-xs-4 col-sm-4 col-md-4 col-lg-4\">\n" +
                "   <input type=\"hidden\" name=\"deleteWidget\" value=\"" + getIdPreference().get(i) + "\">\n" +
                "    <button type=\"submit\" class=\"btn btn-danger active btn-sm\">\n" +
                "       <span class=\"glyphicon glyphicon-remove\"></span>\n" +
                "    </button>\n" +
                "</form>";
    }

    protected String modifyThisWidget(int i) {
       String result = "<div class=' text-center col-xs-4 col-sm-4col-md-4 col-lg-4'>";
       String youtube ="""
                <div class="modal fade" id="editYoutube" role="dialog">
                <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Paramètres des widgets Youtube
                </h4>
                </div>
                <div class="modal-body">
                    <form action="/editWidgetYoutube">
                        <input type="hidden" name="editYoutube" value=""" + getIdPreference().get(i) +
               """ 
                                       >
                                       <div class="checkbox">
                                           <label><input type="checkbox" name="youtubePlaylistActivated" value="1">Playlist de vidéos de la chaîne choisie.</label>
                                       </div>
                                       <label>Url de la chaîne (playlist) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="preferenceYoutubePlaylistChannelURL"></label>
                                       <br>
                                       <label> Refresh du widget (en minute) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="timer1"></label>
                                       <br>
                                       <div class="checkbox">
                                           <label><input type="checkbox" name="youtubeVueChannelActivated" value="1">Nombre de vues de la chaîne.</label>
                                       </div>
                                       <label>Url de la chaîne (nombre de vues)  : <input type="text" name="preferenceYoutubeVueChannelURL"></label>
                                       <br>
                                       <label> Refresh du widget (en minute) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="timer2"></label>
                                       <br>
                                       <div class="checkbox">
                                           <label><input type="checkbox" name="youtubeVueVideoActivated" value="1">Nombre de vues de la vidéo choisie.</label>
                                       </div>
                                       <label>Url de la vidéo (nombre de vues) : &nbsp;&nbsp;<input type="text" name="preferenceYoutubeVueVideoURL"></label>
                                       <br>
                                       <label> Refresh du widget (en minute) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="timer3"></label>
                                       <br>
                                       <div class="checkbox">
                                           <label><input type="checkbox" name="youtubeCommentActivated" value="1">Liste des 20 premiers commentaires de la vidéo.</label>
                                       </div>
                                       <label>Url de la vidéo (commentaires) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="preferenceYoutubeCommentVideoURL"></label>
                                       <br>
                                       <label> Refresh du widget (en minute) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="timer4"></label>
                                       <br>
                                       <br>
                                       <div class="text-center">
                                           <input type="submit" value="Modifier" class="btn btn-primary">
                                       </div>
                                   </form>
                               </div>
                           </div>
                       </div>
                       </div>""";
       String calendar = """
                       <div class="modal fade" id="editCalendar" role="dialog">
                       <div class="modal-dialog">
                           <!-- Modal content-->
                           <div class="modal-content">
                               <div class="modal-header">
                                   <button type="button" class="close" data-dismiss="modal">&times;</button>
                                   <h4 class="modal-title">Paramètres du widget Calendrier
                        </h4>
                        </div>
                        <div class="modal-body">
                            <form action="/editWidgetGoogleCalendar">
                            <input type="hidden" name="editCalendar" value=""" + getIdPreference().get(i) + """
                                >
                                <div class="checkbox">
                                    <label><input type="checkbox" name="googleCalendarActivated" value="1">Calendrier</label>
                                    <br>
                                </div>
                                <label> Refresh du widget (en minute) : <input type="text" name="timer1"></label>
                                <br>
                                <div class="form-group">
                                    <label for="sel1">Choix du format :</label>
                                    <select name="googleCalendarFormat" class="form-control" id="sel1">
                                        <option value="WEEK">Semaine</option>
                                        <option value="MONTH">Mois</option>
                                        <option value="AGENDA">Agenda</option>
                                    </select>
                                </div>
                                <br>
                                <div class="text-center">
                                    <input type="submit" value="Modifier" class="btn btn-primary">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                </div>""";
       String drive = """
                <div class="modal fade" id="editDrive" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Paramètres du widget Drive
                        </h4>
                        </div>
                        <div class="modal-body">
                            <form action="/editWidgetGoogleDrive">
                            <input type="hidden" name="editDrive" value=""" + getIdPreference().get(i) + """
                                >
                                <div class="checkbox">
                                    <label><input type="checkbox" name="googleDriveActivated" value="1">Drive</label>
                                </div>
                                <label>Url de votre drive : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="googleDriveURL"></label>
                                <br>
                                <label> Refresh du widget (en minute) : <input type="text" name="timer1"></label>
                                <br>
                                <br>
                                <div class="text-center">
                                    <input type="submit" value="Modifier" class="btn btn-primary">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                </div>""";
               String amazon = """
                <div class="modal fade" id="editAmazon" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Paramètres du widget Amazon</h4>
                        </div>
                        <div class="modal-body">
                            <form action="/editWidgetAmazon">
                            <input type="hidden" name="editAmazon" value=""" + getIdPreference().get(i) + """
                                >
                                <div class="checkbox">
                                    <label><input type="checkbox" name="amazonActivated" value="1">Résultats de
                                        recherche.</label>
                                </div>
                                <label>Objet recherché : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="amazonTitreRecherche"></label>
                                <br>
                                <label> Refresh du widget (en minute) : <input type="text" name="timer1"></label>
                                <br>
                                <br>
                                <div class="text-center">
                                    <input type="submit" value="Modifier" class="btn btn-primary">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                </div>""";
               String discord = """
                <div class="modal fade" id="editDiscord" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Paramètres du widget Discord</h4>
                        </div>
                        <div class="modal-body">
                            <form action="/editWidgetDiscord">
                            <input type="hidden" name="editDiscord" value=""" + getIdPreference().get(i) + """
                                >
                                <div class="checkbox">
                                    <label><input type="checkbox" name="discordDarkMode" value="dark">Mode dark</label>
                                </div>
                                <div class="checkbox">
                                    <label><input type="checkbox" name="discordActivated" value="1">Affichage des membres connectés du serveur
                                        choisis.</label>
                                </div>
                                <label>Id du serveur : <input type="text" name="discordIdServer"></label>
                                <br>
                                <label> Refresh du widget (en minute) : <input type="text" name="timer1"></label>
                                <br>                            <br>
                                <div class="text-center">
                                    <input type="submit" value="Modifier" class="btn btn-primary">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                </div>""";
               String weather = """
                <div class="modal fade" id="editWeather" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Paramètres du widget Weather
                        </h4>
                        </div>
                        <div class="modal-body">
                            <form action="/editWidgetWeather">
                            <input type="hidden" name="editWeather" value=""" + getIdPreference().get(i) + """
                                >
                                <div class="checkbox">
                                    <label><input type="checkbox" name="weatherTempActivated" value="1">Température.</label>
                                </div>
                                <div class="checkbox">
                                    <label><input type="checkbox" name="weatherPictoActivated" value="1">Pictogramme.</label>
                                </div>

                                <label>Nombre de jours (1-7) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="weatherDays"></label>
                                <br/>
                                <label>Nom de la ville : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text"  name="weatherCity"></label>
                                <br>
                                <label> Refresh du widget (en minute) : <input type="text" name="timer1"></label>
                                <br>
                                <br>

                                <div class="text-center">
                                    <input type="submit" value="Modifier" class="btn btn-primary">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                </div>
                """;
        result += "<button type=\"button\" class=\"btn btn-warning active btn-sm\" data-toggle=\"modal\"";
        switch (getTypeWidget().get(i)) {
            case "youtube" -> {
                result += "data-target=\"#editYoutube\">";
                result += "<span class=\"glyphicon glyphicon-pencil\"></span>\n" +
                        "                </button>";
                result += youtube;
            }
            case "discord" -> {
                result += "data-target=\"#editDiscord\">";
                result += "<span class=\"glyphicon glyphicon-pencil\"></span>\n" +
                        "                </button>";
                result += discord;
            }
            case "calendar" -> {
                result += "data-target=\"#editCalendar\">";
                result += "<span class=\"glyphicon glyphicon-pencil\"></span>\n" +
                        "                </button>";
                result += calendar;
            }
            case "drive" -> {
                result += "data-target=\"#editDrive\">";
                result += "<span class=\"glyphicon glyphicon-pencil\"></span>\n" +
                        "                </button>";
                result += drive;
            }
            case "amazon" -> {
                result += "data-target=\"#editAmazon\">";
                result += "<span class=\"glyphicon glyphicon-pencil\"></span>\n" +
                        "                </button>";
                result += amazon;
            }
            case "weather" -> {
                result += "data-target=\"#editWeather\">";
                result += "<span class=\"glyphicon glyphicon-pencil\"></span>\n" +
                        "                </button>";
                result += weather;
            }
            default -> result += "";
        }
        result += "</div>";
        return result;
//                "<form action=\"/modifyWidget\" class=\"text-left col-xs-4 col-sm-4 col-md-4 col-lg-4\">\n" +
//                "   <input type=\"hidden\" name=\"modifyWidget\" value=\"" + getIdPreference().get(i) + "\">\n" +
//                "   <button type=\"submit\" class=\"btn btn-warning active btn-sm\">\n" +
//                "       <span class=\"glyphicon glyphicon-pencil\"></span>\n" +
//                "   </button>\n" +
//                "</form>";
    }


    /**
     * @return Le code html affichant tout les widgets Youtube à partir de la base donnée
     */
    protected String displayYoutube() {
        StringBuilder result = new StringBuilder();
        int largeurTotale;
        int largeurVideo;
        int largeurAbout;
        int hauteurComment;
        for (int i = 0; i < getYoutubeCommentURL().size(); i++) {
            if (isYoutubePlaylistActivated().get(i) || isYoutubeVueChannelActivated().get(i) || isYoutubeVueVideoActivated().get(i) || isYoutubeCommentActivated().get(i)) {
                if (isYoutubePlaylistActivated().get(i)) {
                    if (isYoutubeVueChannelActivated().get(i) || isYoutubeVueVideoActivated().get(i) || isYoutubeCommentActivated().get(i)) {
                        largeurTotale = 12;
                        largeurVideo = 6;
                        largeurAbout = 6;
                    } else {
                        largeurTotale = 8;
                        largeurVideo = 12;
                        largeurAbout = 0;
                    }
                } else {
                    largeurTotale = 4;
                    largeurVideo = 0;
                    largeurAbout = 12;
                }
                result.append(templateHTML.beginDragAndDrop(i));
                result.append("<div id=\"dragYoutube").append(i)
                        .append("\" class=\"container children col-xs-")
                        .append(12).append(" col-sm-")
                        .append(largeurTotale)
                        .append(" col-md-")
                        .append(largeurTotale)
                        .append(" col-lg-")
                        .append(largeurTotale)
                        .append("\" draggable=\"true\"\n")
                        .append("ondragstart=\"drag(event)\">\n")
                        .append("<div class=\"well\" style=\"height: 500px\">\n")
                        .append("<div>\n")
                        .append(modifyThisWidget(i))
                        .append("<p style=\"font-size: 30px; \" class=\"text-center col-xs-4 col-sm-4col-md-4 col-lg-4\">Youtube</p>\n")
                        .append(deleteThisWidget(i))
                        .append("            </div>\n");
                if (isYoutubePlaylistActivated().get(i)) {
                    result.append("<div style=\"margin-bottom: 20px\" class=\"text-center container col-xs-" + 12 + " col-sm-")
                            .append(largeurVideo).append(" col-md-")
                            .append(largeurVideo).append(" col-lg-")
                            .append(largeurVideo).append("\">\n")
                            .append(widgetYoutube.iFrameLastVideoOfChannel(getYoutubePlaylistURL().get(i)))
                            .append("            </div>\n");
                }
                if (isYoutubeVueChannelActivated().get(i) || isYoutubeVueVideoActivated().get(i)) {
                    hauteurComment = 59;
                    result.append("<div class=\"informations well container comment col-xs-" + 12 + " col-sm-")
                            .append(largeurAbout).append(" col-md-")
                            .append(largeurAbout).append(" col-lg-")
                            .append(largeurAbout).append("\" style=\"background-color: white; height: 22%\"\n")
                            .append("                 id=\"dragViews5\" draggable=\"true\"\n")
                            .append("                 ondragstart=\"drag(event)\">\n");
                    if (isYoutubeVueChannelActivated().get(i)) {
                        result.append("<p id=\"chainViews\"> Nombre de vues de la chaîne ")
                                .append(widgetYoutube.nameChannel(getYoutubeVueChannelURL().get(i)))
                                .append(" : ")
                                .append(widgetYoutube.countNumberViewOfChannel(getYoutubeVueChannelURL()
                                        .get(i))).append("</p>\n");
                    }
                    if (isYoutubeVueVideoActivated().get(i)) {
                        result.append("<p id=\"videoViews\"> Nombre de vues de la vidéo ")
                                .append(widgetYoutube.nameVideo(getYoutubeVueVideoURL().get(i)))
                                .append(" : ")
                                .append(widgetYoutube.countNumberViewOfVideo(getYoutubeVueVideoURL().get(i))).append("</p>\n");
                    }
                    result.append("</div>");
                } else {
                    hauteurComment = 85;
                }
                if (isYoutubeCommentActivated().get(i)) {
                    StringBuilder comments = new StringBuilder();
                    List<List<String>> liste = new ArrayList<>(Objects.requireNonNull(widgetYoutube.listCommentsOfVideo(getYoutubeCommentURL().get(i))));
                    for (List<String> strings : liste) {
                        comments.append("<p> <img alt=\"\" src=\"").append(strings.get(0)).append("\"> a écrit : </p>");
                        for (int k = 1; k < strings.size(); k++) {
                            comments.append("<p>").append(strings.get(k)).append("</p>\n");
                        }
                    }
                    result.append("<div class=\"commentaires well container comment col-xs-" + 12 + " col-sm-")
                            .append(largeurAbout).append(" col-md-").append(largeurAbout).append(" col-lg-")
                            .append(largeurAbout).append("\"").append("style=\"background-color: white; height: ")
                            .append(hauteurComment).append("%\"").append("id=\"dragComment5\">").append(comments)
                            .append("</div>");
                }
                result.append("</div> </div> </div>");
            }
        }
        if (result != null) {
            result.append("""
                            </div>
                        </div>
                    <script>
                        $(document).ready(function () {
                            let youtubeClass = $(".youtube");
                            youtubeClass.css("width", youtubeClass.parent().width());
                            youtubeClass.css("height", youtubeClass.parent().width() * 0.5625);
                            if (youtubeClass.css("height") > "400px") {
                                youtubeClass.css("width", "711");
                                youtubeClass.css("height", "400");
                            }
                            if (window.innerWidth < 768) {
                                youtubeClass.parent().parent().css("height", "1000");
                                $(".informations").css("height", "120");
                                $(".commentaires").css("height", "420")
                            }
                        });
                    </script>""");
        }
        return result.toString();
    }

    /**
     * @return Le code html affichant tout les widgets Amazon à partir de la base donnée
     * @throws IOException Erreur dans le widget amazon
     */
    protected String displayAmazon() throws IOException {
        String corps = "";
        for (int i = 0; i < isAmazonActivated().size(); i++) {
            if (isAmazonActivated().get(i)) {
                corps += """
                        <div id="ondragover""";
                corps += i;
                corps += """
                        " ondrop="drop(event)" ondragover="allowDrop(event)"><div id="dragAmazon""";
                corps += i;
                corps += """
                        " class="children container col-xs-12 col-sm-6 col-md-4 col-lg-4" draggable="true" ondragstart="drag(event)">
                                    <div class="well" style="height: 500px; padding: 5%">
                                        <div>
                                            <form action="/modifyWidget" class="text-left col-xs-4 col-sm-4 col-md-4 col-lg-4">
                                                <input type="hidden" name="modifyWidget" value="
                                                """;
                corps += getIdPreference().get(i);
                corps += """
                            ">
                            <button type="submit" class="btn btn-warning active btn-sm">
                                <span class="glyphicon glyphicon-pencil"></span>
                            </button> 
                        </form>
                        <p style="font-size: 30px" class="text-center  col-xs-4 col-sm-4 col-md-4 col-lg-4">Amazon</p>
                        <form action="/deleteWidget" class="text-right col-xs-4 col-sm-4 col-md-4 col-lg-4">
                            <input type="hidden" name="deleteWidget" value="
                            """;
                corps += getIdPreference().get(i);
                corps += """
                                                ">
                                                <button type="submit" class="btn btn-danger active btn-sm">
                                                    <span class="glyphicon glyphicon-remove"></span>
                                                </button>
                                            </form>
                                        </div>
                                        <div>
                        <form action="test()">
                            <label><input class='form-control' type="text" placeholder="Saisissez votre recherche" name="amazonTitreRecherche""";
                corps += i;
                corps += """
                        "></label>
                            <button class="btn btn-primary" type="button" onclick="sendData""";
                corps += i;
                corps += """
                        ()">Rechercher</button>
                        <script>
                            function sendData""";
                corps += i;
                corps += """
                        () {
                        var valeur = $('input[name=amazonTitreRecherche""";
                corps += i;
                corps += """
                        ]').val();
                        $.ajax({
                            url: "/requestAmazonSearch?data="+valeur,
                            contentType: "application/json", // Pour préciser le type de données qui transite
                            type: "GET",
                            success: function (result) {
                                $("#amazon""";
                corps += i;
                corps += """
                                                ").append("<div class='resulatsdeRecherche'>" + result + "</div>");
                                                },
                                                error: function (err) {
                                                    console.log(err);
                                                },
                                                done: function (e) {
                                                    console.log("DONE");
                                                }
                                            });
                                        }
                                </script>
                            </form>
                        </div>
                                            <div style="width: 100%; height: 78%" class="text-center comment" id="amazon""";
                corps += i;
                corps += """
                        "></div>
                        <script>$(document).ready(function(){
                            setInterval(function(){
                                $("#amazon0").load(" #amazon0");
                                console.log("amazon !");
                            },60 * 60000);
                        });</script></div></div></div>""";
            }
        }
        return corps;
    }

    @RequestMapping(value = "/requestAmazonSearch", method = RequestMethod.GET)
    public @ResponseBody
    String getSearchResultViaAjax1(@RequestParam("data") String itemid) throws IOException {
        List<List<String>> liste = widgetAmazon.listTitleAndLinkSearchAmazon(itemid);
        StringBuilder listeRecherche = new StringBuilder();
        for (int i = 0; i < Math.min(liste.get(0).size(), liste.get(1).size()); i++) {
            listeRecherche.append("<p style=\"background-color:white\">");
            listeRecherche.append(liste.get(0).get(i));
            listeRecherche.append("<br> <a href=\"");
            listeRecherche.append(liste.get(1).get(i));
            listeRecherche.append("\">");
            listeRecherche.append(liste.get(1).get(i));
            listeRecherche.append("</a> </p>");
        }
        return listeRecherche.toString();
    }

    /**
     * @return Le code html affichant tout les widgets Discord à partir de la base donnée
     */
    protected String displayDiscord() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < getDiscordIdServer().size(); i++) {
            if (isDiscordActivated().get(i)) {
                result.append(templateHTML.beginDragAndDrop(i));
                result.append("<div id=\"dragDiscord")
                        .append(i).append("\"class=\"children container col-xs-12 col-sm-6 col-md-4 col-lg-4\" draggable=\"true\"\n")
                        .append("ondragstart=\"drag(event)\">\n")
                        .append("<div class=\"well\" style=\"height: 500px; padding: 5%\">\n")
                        .append("<div>\n")
                        .append(modifyThisWidget(i))
                        .append("<p style=\"font-size: 30px\" class=\"text-center  col-xs-4 col-sm-4 col-md-4 col-lg-4\">Discord</p>\n")
                        .append(deleteThisWidget(i))
                        .append("            </div>\n")
                        .append("            <div style=\"width: 100%; height: 86%\" class=\"text-center\" id=\"discord")
                        .append(i).append("\">\n")
                        .append("            <iframe id=\"discord")
                        .append(i).append("\"src=\"https://discord.com/widget?id=")
                        .append(getDiscordIdServer().get(i)).append("&theme=")
                        .append(getDiscordDarkMode().get(i))
                        .append("\" width=\"100%\" height=\"100%\" allowtransparency=\"true\" frameborder=\"0\" sandbox=\"allow-popups allow-popups-to-escape-sandbox allow-same-origin allow-scripts\"></iframe> ")
                        .append("</div>\n").append("</div>\n").append("<script>").append("$(document).ready(function(){\n")
                        .append("    setInterval(function(){\n").append("        $(\"#discord")
                        .append(i).append("\").load(\" #discord").append(i).append("\");\n")
                        .append("        console.log(\"discord !\");\n").append("    },")
                        .append(getWidgetTimer1().get(i)).append(" * 60000);\n").append("});").append("</script>").append("    </div>");
                result.append("</div>");
            }
        }
        return result.toString();
    }

    /**
     * @return Le code html affichant tout les widgets Weather à partir de la base donnée
     */
    protected String displayWeather() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < getWeatherTempDays().size(); i++) {
            if (isWeatherTempActivated().get(i) || isWeatherPictoActivated().get(i)) {
                result.append(templateHTML.beginDragAndDrop(i));
                result.append("<div id=\"dragWeather")
                        .append(i)
                        .append("\"class=\"children container col-xs-12 col-sm-6 col-md-4 col-lg-4\" draggable=\"true\"\n")
                        .append("ondragstart=\"drag(event)\">\n")
                        .append("<div class=\"well\" style=\"height: 500px; padding: 5%\">\n")
                        .append("<div>\n").append(modifyThisWidget(i))
                        .append("<p style=\"font-size: 30px\" class=\"text-center  col-xs-4 col-sm-4 col-md-4 col-lg-4\">Météo</p>\n")
                        .append(deleteThisWidget(i))
                        .append("</div>\n")
                        .append("<div style=\"width: 100%; height: 86%\" class=\"text-center\" id=\"weather")
                        .append(i).append("\">\n").append(widgetWeather.iFrameWidgetWeather(isWeatherTempActivated()
                        .get(i), isWeatherPictoActivated().get(i), getWeatherTempDays().get(i), getWeatherTempVille().get(i)))
                        .append("</div>\n")
                        .append("</div>\n").append("<script>")
                        .append("$(document).ready(function(){\n")
                        .append("setInterval(function(){\n")
                        .append("$(\"#weather").append(i).append("\").load(\" #weather").append(i).append("\");\n")
                        .append("console.log(\"weather!\");\n").append("    },")
                        .append(getWidgetTimer1().get(i)).append(" * 60000);\n").append("});").append("</script>").append("</div>");
                result.append("</div>");
            }
        }
        return result.toString();
    }

    /**
     * @param user les informations sur l'oauth2
     * @return Le code html affichant tout les widgets Calendrier à partir de la base donnée et de l'oauth2 avec Google
     */
    protected String displayCalendar(@AuthenticationPrincipal OAuth2User user) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < getWeatherTempDays().size(); i++) {
            if (isGoogleCalendarActivated().get(i)) {
                result.append(templateHTML.beginDragAndDrop(i));
                result.append("<div id=\"dragCalendar")
                        .append(i).append("\" class=\"children container col-xs-12 col-sm-6 col-md-4 col-lg-4\" draggable=\"true\"\n")
                        .append("         ondragstart=\"drag(event)\">\n")
                        .append("        <div class=\"well\" style=\"height: 500px; padding: 5%\">\n")
                        .append("            <div>\n")
                        .append(modifyThisWidget(i))
                        .append("                <p style=\"font-size: 30px\" class=\"text-center  col-xs-4 col-sm-4 col-md-4 col-lg-4\">Calendrier</p>\n")
                        .append(deleteThisWidget(i))
                        .append("            </div>\n")
                        .append("\n")
                        .append("            <div id=\"calendar")
                        .append(i)
                        .append("\" style=\"width: 100%; height: 86%\" class=\"text-center\">\n")
                        .append(widgetCalendar.iframeCalendar(user, getGoogleCalendarFormat().get(i)))
                        .append("            </div>\n")
                        .append("        </div>\n")
                        .append("<script>")
                        .append("$(document).ready(function(){\n")
                        .append("    setInterval(function(){\n")
                        .append("        $(\"#dragCalendar")
                        .append(i).append("\").load(\" #dragCalendar")
                        .append(i).append("\");\n").append("    },")
                        .append(getWidgetTimer1().get(i)).append(" * 60000);\n").append("});").append("</script>").append("    </div>");
                result.append("</div>");
            }
        }
        return result.toString();
    }

    /**
     * @return Le code html affichant tout les widgets Drive à partir de la base donnée
     */
    protected String displayDrive() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < getWeatherTempDays().size(); i++) {
            if (isGoogleDriveActivated().get(i)) {
                result.append(templateHTML.beginDragAndDrop(i));
                result.append("<div id=\"dragDrive")
                        .append(i)
                        .append("\" class=\"children container col-xs-12 col-sm-6 col-md-4 col-lg-4\" draggable=\"true\"\n")
                        .append("         ondragstart=\"drag(event)\">\n")
                        .append("        <div class=\"well\" style=\"height: 500px; padding: 5%\">\n")
                        .append("            <div>\n")
                        .append(modifyThisWidget(i))
                        .append("                <p style=\"font-size: 30px\" class=\"text-center  col-xs-4 col-sm-4 col-md-4 col-lg-4\">Drive</p>\n")
                        .append(deleteThisWidget(i))
                        .append("            </div>\n")
                        .append("            <div id=\"drive")
                        .append(i).append("\" style=\"width: 100%; height: 86%\" class=\"text-center\">\n")
                        .append(widgetDrive.iframeDriveFolder("https://drive.google.com/drive/folders/1JlSlbCsKtwS5OtTZMUSyH7PBfB4ke0pA"))
                        .append("            </div>\n")
                        .append("        </div>\n")
                        .append("<script>")
                        .append("$(document).ready(function(){\n")
                        .append("    setInterval(function(){\n")
                        .append("        $(\"#drive")
                        .append(i).append("\").load(\" #drive")
                        .append(i).append("\");\n")
                        .append("        console.log(\"drive!\");\n")
                        .append("    },")
                        .append(getWidgetTimer1().get(i)).append(" * 60000);\n").append("});").append("</script>").append("    </div>");
                result.append("</div>");
            }
        }
        return result.toString();
    }

    /**
     * @param i un identifiant pour un utilisateur
     * @return le code html permettant pas la suite de supprimer un utilisateur
     */
    protected String deleteThisUser(int i) {
        return "<form action=\"/deleteUser\">\n" +
                "                    <input type=\"hidden\" name=\"idUser\" value=\"" + getIdUser().get(i) + "\">\n" +
                "                    <button type=\"submit\" class=\"btn btn-danger active btn-sm\">\n" +
                "                        <span class=\"glyphicon glyphicon-remove\"></span>\n" +
                "                    </button>\n" +
                "                </form>";
    }

    /**
     * @return le code html permettant d'afficher les utilisateurs enregistrés sur le site
     */
    protected String displayUsersCRUD() {
        StringBuilder result = new StringBuilder();
        result.append("""
                <div class="panel panel-default">
                <h2 class="text-center"> Panneau admin - Liste des utilisateurs </h2>
                  <table class="table">
                 <tr>
                    <th>User ID</th>
                    <th>Pseudo</th>
                    <th>Delete User</th>
                  </tr>""");

        for (int i = 0; i < getPseudoUser().size(); i++) {
            result.append(" <tr>" + "    <td>").append(getIdUser().get(i)).append("</td>").append("    <td>").append(getPseudoUser().get(i)).append("</td>").append("    <td>").append(deleteThisUser(i)).append("</td>").append("  </tr>");
        }
        result.append("  </table>\n" + "</div>");
        return result.toString();
    }

    /**
     * Méthode qui échange l'idPréférence de deux widget pour changer l'affichage pour un utilisateur
     *
     * @param id1 ID du widget 1 dans la BDD de l'utilisateur
     * @param id2 ID du widget 2 dans la BDD de l'utilisateur
     */
    @RequestMapping(value = "/requestOrdreIdPreference", method = RequestMethod.GET)
    public @ResponseBody
    String updateOrdreWidget(@RequestParam("data1") int id1, @RequestParam("data2") int id2, HttpServletRequest request) {
        //TODO Erreur ici car on n'accède pas aux préférences de l'utilisateur, demander à Corentin pour savoir comment faire
        MysqlCon bdd = new MysqlCon();
        bdd.getBDD(getCookieValue(request.getCookies(), "idLogin", ""));
        Integer idPreference1 = bdd.getIdPreference().get(id1);
        Integer idPreference2 = bdd.getIdPreference().get(id2);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query1;
            String query2;
            String query3;
            query1 = String.format("UPDATE `widgets` SET `idPreference`=-1 WHERE `idPreference`=%s;", idPreference1);
            query2 = String.format("UPDATE `widgets` SET `idPreference`=%s WHERE `idPreference`=%s;", idPreference1, idPreference2);
            query3 = String.format("UPDATE `widgets` SET `idPreference`=%s WHERE `idPreference`=-1;", idPreference2);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query1);
            stmt.executeUpdate(query2);
            stmt.executeUpdate(query3);
            con.close();
            return "true";
        } catch (Exception e) {
            System.out.println(e);
            return "false";
        }
    }

    /**
     * Page principale sur site
     *
     * @return le code html permettant d'accéder à la page / du site
     */
    @GetMapping("/")
    protected String Accueil(@AuthenticationPrincipal OAuth2User user, HttpServletResponse response, HttpServletRequest request) {
        try { //connecté avec oauth2
            response.setHeader("Set-Cookie", "idLogin=" + user.getName().substring(0, 8) + "; HttpOnly; SameSite=strict");
            MysqlCon bdd = new MysqlCon();
            if (!bdd.isIdExist(user.getName().substring(0, 8))) {
                bdd.createOAuthAccountBDD(user.getName().substring(0, 8)); // CREER COMPTE
            }
        } catch (Exception e) { //Pas connecté avec oauth2
        }

        String corps = """
                <div class="container text-center">
                    <h3>Nos widgets</h3><br>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 col-md-4 col-lg-4">
                            <h3>Discord</h3>
                            <iframe src="https://discord.com/widget?id=755024047284158514&theme=dark" width="300" height="300"
                                    allowtransparency="true" style='border: 0'
                                    sandbox="allow-popups allow-popups-to-escape-sandbox allow-same-origin allow-scripts"></iframe>

                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4 col-lg-4">
                            <h3>Youtube</h3>
                            <iframe width="300" height="160px"
                                    src="https://www.youtube.com/embed/videoseries?list=UUYGjxo5ifuhnmvhPvCc3DJQ" style='border: 0'
                                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                    allowfullscreen></iframe>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-4 col-lg-4">
                            <h3>Météo</h3>
                            <iframe src="https://www.meteoblue.com/fr/meteo/widget/daily/rennes_france_2983990?geoloc=fixed&days=7&tempunit=CELSIUS&windunit=KILOMETER_PER_HOUR&precipunit=MILLIMETER&coloured=coloured&pictoicon=1&maxtemperature=1&mintemperature=1&windspeed=0&windgust=0&winddirection=0&uv=0&humidity=0&precipitation=0&precipitationprobability=0&spot=0&pressure=0&layout=light"
                                    allowtransparency="true"
                                    sandbox="allow-same-origin allow-scripts allow-popups allow-popups-to-escape-sandbox"
                                    style="width: 300px; height: 300px; border: 0"></iframe>
                        </div>
                    </div>
                </div>
                <br>""";
        return templateHTML.head("") + templateHTML.nav("/", request) + templateHTML.carousel() + corps + templateHTML.footer();
    }

    /**
     * @return le code html permettant d'accéder à la page /connexion du site
     */
    @GetMapping("/connexion")
    protected String Connexion(HttpServletRequest request) {

        String corps = """
                <br>
                <br>
                <br>
                <div id="page" class="container">
                    <div class="row">
                        <h2 class="text-primary text-center">Se connecter</h2>
                        <br>
                        <div class="col-md-6 col-md-offset-3">
                            <form class="form-group" action="/checkLogin">
                                <div class="form-group">
                                    <label for="login">Login<sup class="requiredField">(*)</sup> : </label>
                                    <input id="login" class="form-control" type="text" name="loginGiven">
                                </div>
                                <div class="form-group">
                                    <label for="mdp">Mot de passe<sup class="requiredField">(*)</sup> : </label>
                                    <input id="mdp" class="form-control" type="password" name="mdpGiven">
                                </div>
                                <div class="form-group text-center">
                                    <input type="submit" value="Se connecter" id="connexion" class="btn btn-primary">
                                </div>
                            </form>
                            <br> <br>
                            <div class="container text-center" style="width: 100%">
                                <a href="/oauth2/authorization/facebook">
                                    <img src="https://facebookbrand.com/wp-content/uploads/2019/04/f_logo_RGB-Hex-Blue_512.png?w=512&h=512"
                                         style="width: 10%; height: 10%">
                                </a>
                                <a href="/oauth2/authorization/google">
                                    <img src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg"
                                         style="width: 10%; height: 10%; margin-right: 5%; margin-left: 5%">
                                </a>
                                <a href="/oauth2/authorization/github">
                                    <img src="https://image.flaticon.com/icons/png/512/25/25231.png" style="width: 10%; height: 10%">
                                </a>
                            </div>
                            <div class="text-center">
                                <br>
                                <a href="/inscription">Pas encore inscrit?</a>
                            </div>
                        </div>
                    </div>
                </div>
                <br><br>""";

        return templateHTML.head("") + templateHTML.nav("/connexion", request) + corps + templateHTML.footer();
    }

    /**
     * @return le code html permettant d'accéder à la page /perso du site
     * @throws IOException Une erreur liée au widget Amazon
     */
    @GetMapping("/perso")
    protected String Perso(@AuthenticationPrincipal OAuth2User user, HttpServletRequest request) throws IOException { // Les Request param permettent de prendre ce qui a été entré dans le formulaire

        MysqlCon bdd = new MysqlCon();
        bdd.getBDD(getCookieValue(request.getCookies(), "idLogin", ""));
        String divDebut = "<br>" +
                "<div id=\"element\">";
        String divFin = "</div>";
        String trieFonction = """
                <script>
                    $(document).ready(function () {
                        for (let i = 0; i < $("#element").children().length - 1; i++) {
                            $('#ondragover' + i).after($('#ondragover' + (i + 1)));
                        }
                    })
                </script>""";
        String userDisconnected = "";
        if (getCookieValue(request.getCookies(), "idLogin", "").equals("")) {
            userDisconnected += templateHTML.userDisconnected();
        }
        return templateHTML.head("") + templateHTML.nav("/perso", request) + divDebut + userDisconnected + bdd.displayCalendar(user) + bdd.displayDrive() + bdd.displayAmazon() + bdd.displayWeather() + bdd.displayDiscord() + bdd.displayYoutube() + divFin + trieFonction + templateHTML.footer();
    }


    /**
     * @return le code html permettant d'accéder à la page /preferences du site
     */
    @GetMapping("/preferences")
    protected String Preferences(HttpServletRequest request) {
        MysqlCon connexion = new MysqlCon();
        connexion.getBDD(getCookieValue(request.getCookies(), "idLogin", ""));

        String corps = """
                <br>
                <h2 class="text-center">Vos widgets</h2>
                <br>
                <!--                    SERVICE GOOGLE               -->
                <div class="container well" style="background-color: #CA3535;">
                    <div class="contaier-fluid">
                        <h3 class="text-center" style="color: white;">Service Google</h3>
                        <br>
                        <!--                                        YOUTUBE                                  -->
                        <div class="col-sm-4">
                            <div class="text-center checkbox">
                                <h3 style="color: white;">Youtube
                                    <button type="button" class="btn btn-default btn-sm" data-toggle="modal"
                                            data-target="#youtubeModal">
                                        <span class="glyphicon glyphicon-plus"></span> Ajouter Widget
                                    </button>
                                </h3>
                            </div>
                        </div>
                        <div class="modal fade" id="youtubeModal" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Paramètres des widgets Youtube</h4>
                                    </div>
                                    <div class="modal-body">
                                        <form action="/ajoutWidgetYoutube">
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="youtubePlaylistActivated" value="1">Playlist de vidéos de la chaîne choisie.</label>
                                            </div>
                                            <label>Url de la chaîne (playlist) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="preferenceYoutubePlaylistChannelURL"></label>
                                            <br>
                                            <label> Refresh du widget (en minute) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="timer1"></label>
                                            <br>
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="youtubeVueChannelActivated" value="1">Nombre de vues de la chaîne.</label>
                                            </div>
                                            <label>Url de la chaîne (nombre de vues)  : <input type="text" name="preferenceYoutubeVueChannelURL"></label>
                                            <br>
                                            <label> Refresh du widget (en minute) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="timer2"></label>
                                            <br>
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="youtubeVueVideoActivated" value="1">Nombre de vues de la vidéo choisie.</label>
                                            </div>
                                            <label>Url de la vidéo (nombre de vues) : &nbsp;&nbsp;<input type="text" name="preferenceYoutubeVueVideoURL"></label>
                                            <br>
                                            <label> Refresh du widget (en minute) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="timer3"></label>
                                            <br>
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="youtubeCommentActivated" value="1">Liste des 20 premiers commentaires de la vidéo.</label>
                                            </div>
                                            <label>Url de la vidéo (commentaires) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="preferenceYoutubeCommentVideoURL"></label>
                                            <br>
                                            <label> Refresh du widget (en minute) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="timer4"></label>
                                            <br>
                                            <br>
                                            <div class="text-center">
                                                <input type="submit" value="Ajouter" class="btn btn-primary">
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!--                                           CALENDRIER                            -->
                        <div class="col-sm-4">
                            <div class="text-center checkbox">
                                <h3 style="color: white;">Calendrier
                                    <button type="button" class="btn btn-default btn-sm" data-toggle="modal" data-target="#calendarModal">
                                        <span class="glyphicon glyphicon-plus"></span> Ajouter Widget
                                    </button>
                                </h3>
                            </div>
                        </div>
                        <div class="modal fade" id="calendarModal" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Paramètres du widget Calendrier</h4>
                                    </div>
                                    <div class="modal-body">
                                        <form action="/ajoutWidgetGoogleCalendar">
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="googleCalendarActivated" value="1">Calendrier</label>
                                                <br>
                                            </div>
                                            <label> Refresh du widget (en minute) : <input type="text" name="timer1"></label>
                                            <br>
                                            <div class="form-group">
                                                <label for="sel1">Choix du format :</label>
                                                <select name="googleCalendarFormat" class="form-control" id="sel1">
                                                    <option value="WEEK">Semaine</option>
                                                    <option value="MONTH">Mois</option>
                                                    <option value="AGENDA">Agenda</option>
                                                </select>
                                            </div>
                                            <br>
                                            <div class="text-center">
                                                <input type="submit" value="Ajouter" class="btn btn-primary">
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!--                                           DRIVE                                 -->
                        <div class="col-sm-4">
                            <div class="text-center checkbox">
                                <h3 style="color: white;">Drive
                                    <button type="button" class="btn btn-default btn-sm" data-toggle="modal" data-target="#driveModal">
                                        <span class="glyphicon glyphicon-plus"></span>Ajouter Widget
                                    </button>
                                </h3>
                            </div>
                        </div>
                        <div class="modal fade" id="driveModal" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Paramètres du widget Drive</h4>
                                    </div>
                                    <div class="modal-body">
                                        <form action="/ajoutWidgetGoogleDrive">
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="googleDriveActivated" value="1">Drive</label>
                                            </div>
                                            <label>Url de votre drive : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="googleDriveURL"></label>
                                            <br>
                                            <label> Refresh du widget (en minute) : <input type="text" name="timer1"></label>
                                            <br>
                                            <br>
                                            <div class="text-center">
                                                <input type="submit" value="Ajouter" class="btn btn-primary">
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <!--                    AUTRES SERVICES               -->
                    <div class="container-fluid" >
                        <!--                    SERVICE AMAZON               -->
                        <div class="col-sm-2">
                        </div>
                        <div class="well col-sm-3 text-center" style="background-color: #EA6F2D;">
                            <h3 class="text-center" style="color: white;">Service Amazon</h3>
                            <br>
                            <div class="text-center checkbox">
                                <h3 style="color: white;">Amazon
                                    <button type="button" class="btn btn-default btn-sm" data-toggle="modal" data-target="#amazonModal">
                                        <span class="glyphicon glyphicon-plus"></span> Ajouter Widget
                                    </button>
                                </h3>
                            </div>
                        </div>
                        <div class="modal fade" id="amazonModal" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Paramètres du widget Amazon</h4>
                                    </div>
                                    <div class="modal-body">
                                        <form action="/ajoutWidgetAmazon">
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="amazonActivated" value="1">Résultats de
                                                    recherche.</label>
                                            </div>
                                            <label>Objet recherché : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="amazonTitreRecherche"></label>
                                            <br>
                                            <label> Refresh du widget (en minute) : <input type="text" name="timer1"></label>
                                            <br>
                                            <br>
                                            <div class="text-center">
                                                <input type="submit" value="Ajouter" class="btn btn-primary">
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-2">
                        </div>
                <!--                    SERVICE DISCORD               -->
                        <div class="well col-sm-3 text-center" style="background-color: #A4ADAF;">
                            <h3 class="text-center">Service Discord</h3>
                            <br>
                            <div class="text-center checkbox">
                                <h3>Discord
                                    <button type="button" class="btn btn-default btn-sm" data-toggle="modal"
                                            data-target="#discordModal">
                                        <span class="glyphicon glyphicon-plus"></span> Ajouter Widget
                                    </button>
                                </h3>
                            </div>
                        </div>
                        <div class="modal fade" id="discordModal" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Paramètres du widget Discord</h4>
                                    </div>
                                    <div class="modal-body">
                                        <form action="/ajoutWidgetDiscord">
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="discordDarkMode" value="dark">Mode dark</label>
                                            </div>
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="discordActivated" value="1">Affichage des membres connectés du serveur
                                                    choisis.</label>
                                            </div>
                                            <label>Id du serveur : <input type="text" name="discordIdServer"></label>
                                            <br>
                                            <label> Refresh du widget (en minute) : <input type="text" name="timer1"></label>
                                            <br>                            <br>
                                            <div class="text-center">
                                                <input type="submit" value="Ajouter" class="btn btn-primary">
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                <!--                    SERVICE MÉTÉO               -->
                    <div class="container-fluid">
                        <div class="col-sm-2">
                        </div>
                        <div class="well col-sm-3 text-center" style="background-color: #41838F;">
                            <h3 style="color: white;">Service Météo</h3>
                            <br>
                            <div class="text-center checkbox">
                                <h3 style="color: white;">Météo
                                    <button type="button" class="btn btn-default btn-sm" data-toggle="modal"
                                            data-target="#weatherModal">
                                        <span class="glyphicon glyphicon-plus"></span> Ajouter Widget
                                    </button>
                                </h3>
                            </div>
                        </div>
                        <div class="modal fade" id="weatherModal" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Paramètres du widget Weather</h4>
                                    </div>
                                    <div class="modal-body">
                                        <form action="/ajoutWidgetWeather">
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="weatherTempActivated" value="1">Température.</label>
                                            </div>
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="weatherPictoActivated" value="1">Pictogramme.</label>
                                            </div>

                                            <label>Nombre de jours (1-7) : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="weatherDays"></label>
                                            <br/>
                                            <label>Nom de la ville : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text"  name="weatherCity"></label>
                                            <br>
                                            <label> Refresh du widget (en minute) : <input type="text" name="timer1"></label>
                                            <br>
                                            <br>

                                            <div class="text-center">
                                                <input type="submit" value="Ajouter" class="btn btn-primary">
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>""";
        return templateHTML.head("") + templateHTML.nav("/preferences", request) + corps + templateHTML.footer();
    }

    /**
     * @return le code html permettant d'accéder à la page /inscription du site
     */
    @GetMapping("/inscription")
    protected String Inscription(HttpServletRequest request) {
        String corps = """
                <br>
                <br>
                <br>
                <div id="page" class="container">
                    <div class="row">
                        <h2 class="text-primary text-center">S'inscrire</h2>
                        <br>
                        <div class="col-md-6 col-md-offset-3">
                            <form class="form-group" action="/createAccountStandard">
                                <div class="form-group">
                                    <label for="login">Login<sup class="requiredField">(*)</sup> : </label>
                                    <input id="login" class="form-control " type="text" name="pseudo">
                                </div>
                                <div class="form-group">
                                    <label for="mdp">Mot de passe<sup class="requiredField">(*)</sup> : </label>
                                    <input id="mdp" class="form-control" type="password" name="mdp">
                                </div>
                                <div class="form-group text-center">
                                    <input type="submit" value="Créer compte" id="connexion" class="btn btn-primary">
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
                <br><br>""";

        return templateHTML.head("") + templateHTML.nav("/connexion", request) + corps + templateHTML.footer();
    }

    /**
     * @return le code html permettant d'accéder à la page /CRUD du site
     */
    @GetMapping("/CRUD")
    protected String CRUD(HttpServletRequest request) {
        String result = "";
        String listeUser = "";
        // RAJOUTER UN IF COOKIE VIDE AVANT DE LE PARSE, SINON CA PARSE DU VIDE ET CA PLANTE
        if (!getCookieValue(request.getCookies(), "idLogin", "").equals("")) {
            int cookieLogin = Integer.parseInt(getCookieValue(request.getCookies(), "idLogin", ""));
            if (cookieLogin <= 3 && cookieLogin >= 1) {
                MysqlCon bdd = new MysqlCon();
                bdd.getUserListBDD();
                listeUser += bdd.displayUsersCRUD();
            } else {
                listeUser += "<h3 class=\"text-center\"> Vous n'êtes pas autorisé à consulter cette page </h3>";
            }

        } else {
            listeUser += "<h3 class=\"text-center\"> Vous n'êtes pas autorisé à consulter cette page </h3>";
        }


        result += templateHTML.head("") + templateHTML.nav("/CRUD", request) + listeUser + templateHTML.footer();

        return result;
    }

    @GetMapping("/about.json")
    protected @ResponseBody
    String about() {
        return templateHTML.about();
    }

    //GETMAPPING SERVICES OAUTH --------------------------------------------GETMAPPING SERVICES OAUTH------------------------------------------------------

    /**
     * @return le code html permettant d'accéder à la page /success-login du site, qui redirige vers la page /
     */
    @GetMapping("/success-login")
    protected String login(HttpServletRequest request) {
        String corps = "Connexion réussi, vous allez être redirigé vers la page d'accueil";
        return templateHTML.head("<meta http-equiv=\"Refresh\" content=\"0; url=/\" />") + templateHTML.nav("/connexion", request) + corps + templateHTML.footer();
    }

    /**
     * @return le code html permettant d'accéder à la page /success-logout du site qui redirige vers la page de connexion
     */
    @GetMapping("/success-logout")
    protected String logout(HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Set-Cookie", "idLogin=0; Max-Age=0; HttpOnly; SameSite=strict");
        String corps = "Deconnection réussi, vous allez être redirigé vers la page d'accueil";
        return templateHTML.head("<meta http-equiv=\"Refresh\" content=\"0; url=/\" />") + templateHTML.nav("/connexion", request) + corps + templateHTML.footer();
    }

    // GETMAPPING DE REDIRECTION AJOUTS DE WIDGETS --------------------------------------------------------------------------------------------------------

    /**
     * @param preferenceYoutubeVueChannelURL      l'url d'une chaine youtube pour afficher son nombre de vue
     * @param preferenceYoutubePlaylistChannelURL l'url d'une vidéo youtube pour afficher une playlist de vidéo
     * @param youtubePlaylistActivated            indique si l'on doit activer le widget affichant une playlist de vidéo ou non
     * @param youtubeVueChannelActivated          indique si l'on doit activer le widget indiquant le nombre de vue d'une chaine ou non
     * @param youtubeVueVideoActivated            indique si l'on doit activer le widget indiquant le nombre de vue d'une vidéo ou non
     * @param youtubeCommentActivated             indique si l'on doit activer le widget affichant les commentaires d'une vidéo ou non
     * @param preferenceYoutubeVueVideoURL        l'url d'une vidéo youtube pour afficher son nombre de vue
     * @param preferenceYoutubeCommentVideoURL    l'url d'une vidéo youtube pour afficher ses commentaires
     * @param timer1                              temps de refresh en minute du widget de la playlist youtube
     * @param timer2                              temps de refresh en minute du widget du nombre de vue d'une chaîne youtube
     * @param timer3                              temps de refresh en minute du widget du nombre de vue d'une vidéo youtube
     * @param timer4                              temps de refresh en minute du widget des commentaires d'une vidéo youtube
     * @throws IOException Erreur dans un widget
     */
    @GetMapping("/ajoutWidgetYoutube")
    protected void addWidgetYoutube(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "preferenceYoutubeVueChannelURL", defaultValue = "") String preferenceYoutubeVueChannelURL, @RequestParam(value = "preferenceYoutubePlaylistChannelURL", defaultValue = "") String preferenceYoutubePlaylistChannelURL, @RequestParam(value = "youtubePlaylistActivated", defaultValue = "0") int youtubePlaylistActivated, @RequestParam(value = "youtubeVueChannelActivated", defaultValue = "0") int youtubeVueChannelActivated, @RequestParam(value = "youtubeVueVideoActivated", defaultValue = "0") int youtubeVueVideoActivated, @RequestParam(value = "youtubeCommentActivated", defaultValue = "0") int youtubeCommentActivated, @RequestParam(value = "preferenceYoutubeVueVideoURL", defaultValue = "") String preferenceYoutubeVueVideoURL, @RequestParam(value = "preferenceYoutubeCommentVideoURL", defaultValue = "") String preferenceYoutubeCommentVideoURL, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "timer2", defaultValue = "10") int timer2, @RequestParam(value = "timer3", defaultValue = "10") int timer3, @RequestParam(value = "timer4", defaultValue = "10") int timer4) throws IOException {
        int id = Integer.parseInt(getCookieValue(request.getCookies(), "idLogin", ""));
        MysqlCon bdd = new MysqlCon();
        bdd.addWidgetYoutubeBDD(id, youtubeVueChannelActivated, youtubeVueVideoActivated, youtubePlaylistActivated, youtubeCommentActivated, preferenceYoutubeVueChannelURL, preferenceYoutubeVueVideoURL, preferenceYoutubePlaylistChannelURL, preferenceYoutubeCommentVideoURL, timer1, timer2, timer3, timer4);
        response.sendRedirect("/preferences");
    }

    /**
     * @param timer1               temps de refresh en minute du widget amazon
     * @param amazonActivated      indique si l'on doit activer ou non le widget amazon
     * @param amazonTitreRecherche indique le nom de la recherche à effectuer
     * @throws IOException une erreur sur le widget d'amazon
     */
    @GetMapping("/ajoutWidgetAmazon")
    protected void addWidgetAmazon(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "amazonActivated", defaultValue = "0") int amazonActivated, @RequestParam(value = "amazonTitreRecherche", defaultValue = "chocolat") String amazonTitreRecherche) throws IOException {
        int id = Integer.parseInt(getCookieValue(request.getCookies(), "idLogin", ""));
        MysqlCon bdd = new MysqlCon();
        bdd.addWidgetAmazonBDD(id, amazonActivated, amazonTitreRecherche, timer1);
        response.sendRedirect("/preferences");
    }

    /**
     * @param timer1                temps de refresh en minute du widget weather
     * @param weatherTempActivated  indique si l'on doit afficher la température sur le widget
     * @param weatherPictoActivated indique si l'on doit activer le pictogramme sur le widget
     * @param weatherDays           indique le nombre de jour a afficher sur le widget
     * @param weatherCity           indique la ville sur laquelle afficher les informations
     * @throws IOException une erreur sur le widget weather
     */
    @GetMapping("/ajoutWidgetWeather")
    protected void addWidgetWeather(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "weatherTempActivated", defaultValue = "0") int weatherTempActivated, @RequestParam(value = "weatherPictoActivated", defaultValue = "0") int weatherPictoActivated, @RequestParam(value = "weatherDays", defaultValue = "5") int weatherDays, @RequestParam(value = "weatherCity", defaultValue = "") String weatherCity) throws IOException {
        int id = Integer.parseInt(getCookieValue(request.getCookies(), "idLogin", ""));
        MysqlCon bdd = new MysqlCon();
        bdd.addWidgetWeatherBDD(id, weatherTempActivated, weatherPictoActivated, weatherCity, weatherDays, weatherCity, weatherDays, timer1);
        response.sendRedirect("/preferences");
    }

    /**
     * @param timer1           temps de refresh en minute du widget discord
     * @param discordActivated indique si l'on doit activer ce widget
     * @param discordDarkMode  indique si l'on doit activer ou non le dark mode
     * @param discordIdServer  indique le serveur discord sur lequelle on affichera les utilisateurs de connectés
     * @throws IOException une erreur sue le widget discord
     */
    @GetMapping("/ajoutWidgetDiscord")
    protected void addWidgetDiscord(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "discordActivated", defaultValue = "0") int discordActivated, @RequestParam(value = "discordDarkMode", defaultValue = "light") String discordDarkMode, @RequestParam(value = "discordIdServer", defaultValue = "5") String discordIdServer) throws IOException {
        int id = Integer.parseInt(getCookieValue(request.getCookies(), "idLogin", ""));
        MysqlCon bdd = new MysqlCon();
        bdd.addWidgetDiscordBDD(id, discordActivated, discordIdServer, discordDarkMode, timer1);
        response.sendRedirect("/preferences");
    }

    /**
     * @param timer1                  temps de refresh en minute du widget calendar
     * @param googleCalendarActivated indique si l'on doit activer le widget calendar
     * @param googleCalendarFormat    indique le format d'affichage du widget calendar
     * @throws IOException une erreur sur le widget calendar
     */
    @GetMapping("/ajoutWidgetGoogleCalendar")
    protected void addWidgetCalendar(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "googleCalendarActivated", defaultValue = "0") int googleCalendarActivated, @RequestParam(value = "googleCalendarFormat", defaultValue = "Semaine") String googleCalendarFormat) throws IOException {
        int id = Integer.parseInt(getCookieValue(request.getCookies(), "idLogin", ""));
        MysqlCon bdd = new MysqlCon();
        bdd.addWidgetGoogleCalendarBDD(id, googleCalendarActivated, googleCalendarFormat, timer1);
        response.sendRedirect("/preferences");
    }

    /**
     * @param timer1               temps de refresh en minute du widget drive
     * @param googleDriveActivated indique si l'on doit activer le widget drive
     * @param googleDriveURL       l'url du dossier sur lequel on souhaite afficher les fichiers et dossiers contenus à l'intérieur
     * @throws IOException une erreur sur le widget drive
     */
    @GetMapping("/ajoutWidgetGoogleDrive")
    protected void addWidgetDrive(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "googleDriveActivated", defaultValue = "0") int googleDriveActivated, @RequestParam(value = "googleDriveURL", defaultValue = "") String googleDriveURL) throws IOException {
        int id = Integer.parseInt(getCookieValue(request.getCookies(), "idLogin", ""));
        MysqlCon bdd = new MysqlCon();
        bdd.addWidgetGoogleDriveBDD(id, googleDriveActivated, googleDriveURL, timer1);
        response.sendRedirect("/preferences");
    }



    // EDIT WIDGETS -----------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/editWidgetYoutube")
    protected void editWidgetYoutube(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "preferenceYoutubeVueChannelURL", defaultValue = "") String preferenceYoutubeVueChannelURL, @RequestParam(value = "preferenceYoutubePlaylistChannelURL", defaultValue = "") String preferenceYoutubePlaylistChannelURL, @RequestParam(value = "youtubePlaylistActivated", defaultValue = "0") int youtubePlaylistActivated, @RequestParam(value = "youtubeVueChannelActivated", defaultValue = "0") int youtubeVueChannelActivated, @RequestParam(value = "youtubeVueVideoActivated", defaultValue = "0") int youtubeVueVideoActivated, @RequestParam(value = "youtubeCommentActivated", defaultValue = "0") int youtubeCommentActivated, @RequestParam(value = "preferenceYoutubeVueVideoURL", defaultValue = "") String preferenceYoutubeVueVideoURL, @RequestParam(value = "preferenceYoutubeCommentVideoURL", defaultValue = "") String preferenceYoutubeCommentVideoURL, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "timer2", defaultValue = "10") int timer2, @RequestParam(value = "timer3", defaultValue = "10") int timer3, @RequestParam(value = "timer4", defaultValue = "10") int timer4, @RequestParam(value = "editYoutube", defaultValue = "1") int idPreference) throws IOException {


        MysqlCon bdd = new MysqlCon();
        bdd.editWidgetYoutubeBDD(idPreference, youtubeVueChannelActivated, youtubeVueVideoActivated, youtubePlaylistActivated, youtubeCommentActivated, preferenceYoutubeVueChannelURL, preferenceYoutubeVueVideoURL, preferenceYoutubePlaylistChannelURL, preferenceYoutubeCommentVideoURL, timer1, timer2, timer3, timer4);
        response.sendRedirect("/perso");
    }

    /**
     * @param timer1               temps de refresh en minute du widget amazon
     * @param amazonActivated      indique si l'on doit activer ou non le widget amazon
     * @param amazonTitreRecherche indique le nom de la recherche à effectuer
     * @throws IOException une erreur sur le widget d'amazon
     */
    @GetMapping("/editWidgetAmazon")
    protected void editWidgetAmazon(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "amazonActivated", defaultValue = "0") int amazonActivated, @RequestParam(value = "amazonTitreRecherche", defaultValue = "chocolat") String amazonTitreRecherche, @RequestParam(value = "editAmazon", defaultValue = "1") int idPreference) throws IOException {
        MysqlCon bdd = new MysqlCon();
        bdd.editWidgetAmazonBDD(idPreference, amazonActivated, amazonTitreRecherche, timer1);
        response.sendRedirect("/perso");
    }

    /**
     * @param timer1                temps de refresh en minute du widget weather
     * @param weatherTempActivated  indique si l'on doit afficher la température sur le widget
     * @param weatherPictoActivated indique si l'on doit activer le pictogramme sur le widget
     * @param weatherDays           indique le nombre de jour a afficher sur le widget
     * @param weatherCity           indique la ville sur laquelle afficher les informations
     * @throws IOException une erreur sur le widget weather
     */
    @GetMapping("/editWidgetWeather")
    protected void editWidgetWeather(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "weatherTempActivated", defaultValue = "0") int weatherTempActivated, @RequestParam(value = "weatherPictoActivated", defaultValue = "0") int weatherPictoActivated, @RequestParam(value = "weatherDays", defaultValue = "5") int weatherDays, @RequestParam(value = "weatherCity", defaultValue = "") String weatherCity, @RequestParam(value = "editWeather", defaultValue = "1") int idPreference) throws IOException {
        MysqlCon bdd = new MysqlCon();
        bdd.editWidgetWeatherBDD(idPreference, weatherTempActivated, weatherPictoActivated, weatherCity, weatherDays, weatherCity, weatherDays, timer1);
        response.sendRedirect("/perso");
    }

    /**
     * @param timer1           temps de refresh en minute du widget discord
     * @param discordActivated indique si l'on doit activer ce widget
     * @param discordDarkMode  indique si l'on doit activer ou non le dark mode
     * @param discordIdServer  indique le serveur discord sur lequelle on affichera les utilisateurs de connectés
     * @throws IOException une erreur sue le widget discord
     */
    @GetMapping("/editWidgetDiscord")
    protected void editWidgetDiscord(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "discordActivated", defaultValue = "0") int discordActivated, @RequestParam(value = "discordDarkMode", defaultValue = "light") String discordDarkMode, @RequestParam(value = "discordIdServer", defaultValue = "5") String discordIdServer, @RequestParam(value = "editDiscord", defaultValue = "1") int idPreference) throws IOException {
        MysqlCon bdd = new MysqlCon();
        bdd.editWidgetDiscordBDD(idPreference, discordActivated, discordIdServer, discordDarkMode, timer1);
        response.sendRedirect("/perso");
    }

    /**
     * @param timer1                  temps de refresh en minute du widget calendar
     * @param googleCalendarActivated indique si l'on doit activer le widget calendar
     * @param googleCalendarFormat    indique le format d'affichage du widget calendar
     * @throws IOException une erreur sur le widget calendar
     */
    @GetMapping("/editWidgetGoogleCalendar")
    protected void editWidgetCalendar(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "googleCalendarActivated", defaultValue = "0") int googleCalendarActivated, @RequestParam(value = "googleCalendarFormat", defaultValue = "Semaine") String googleCalendarFormat, @RequestParam(value = "editCalendar", defaultValue = "1") int idPreference) throws IOException {
        MysqlCon bdd = new MysqlCon();
        bdd.editWidgetGoogleCalendarBDD(idPreference, googleCalendarActivated, googleCalendarFormat, timer1);
        response.sendRedirect("/perso");
    }

    /**
     * @param timer1               temps de refresh en minute du widget drive
     * @param googleDriveActivated indique si l'on doit activer le widget drive
     * @param googleDriveURL       l'url du dossier sur lequel on souhaite afficher les fichiers et dossiers contenus à l'intérieur
     * @throws IOException une erreur sur le widget drive
     */
    @GetMapping("/editWidgetGoogleDrive")
    protected void editWidgetDrive(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "timer1", defaultValue = "10") int timer1, @RequestParam(value = "googleDriveActivated", defaultValue = "0") int googleDriveActivated, @RequestParam(value = "googleDriveURL", defaultValue = "") String googleDriveURL, @RequestParam(value = "editDrive", defaultValue = "1") int idPreference) throws IOException {
        MysqlCon bdd = new MysqlCon();
        bdd.editWidgetGoogleDriveBDD(idPreference, googleDriveActivated, googleDriveURL, timer1);
        response.sendRedirect("/perso");
    }




    // GETMAPPING Autres

    /**
     * vérifie si un utilisateur est connecté
     *
     * @param loginGiven l'identifiant d'un utilisateur
     * @param mdpGiven   le mot de passe d'un utilisqteur
     * @throws IOException une erreur
     */
    @GetMapping("/checkLogin")
    protected void redirection(@RequestParam(value = "loginGiven", defaultValue = "") String loginGiven, @RequestParam(value = "mdpGiven", defaultValue = "") String mdpGiven, HttpServletResponse response) throws IOException {
        MysqlCon bdd = new MysqlCon();
        bdd.loginBDD(loginGiven, mdpGiven);
        if (bdd.getIdLogin().equals("")) {
            response.sendRedirect("/connexion");
        } else {
            response.setHeader("Set-Cookie", "idLogin=" + bdd.getIdLogin() + "; HttpOnly; SameSite=strict");
            response.sendRedirect("/perso");
        }
    }

    /**
     * crée un utilisateur
     *
     * @param pseudo le pseudo a créer pour un nouvel utilisateur
     * @param mdp    le mot de passe associé au pseudo du nouvel utilisateur
     * @throws IOException une erreur
     */
    @GetMapping("/createAccountStandard")
    protected void createAccountStandard(HttpServletResponse response, @RequestParam(value = "pseudo", defaultValue = "") String pseudo, @RequestParam(value = "mdp", defaultValue = "") String mdp) throws IOException {
        // creer le compte avec pseudo et mdp. puis redirect to accueil ou /perso peu importe
        MysqlCon bdd = new MysqlCon();
        bdd.createAccountStandardBDD(pseudo, mdp);
        response.sendRedirect("/perso");
    }

    /**
     * Supprime un widget de la base donnée
     *
     * @param idPreference l'id d'un widget défini dans la base de donnée
     * @throws IOException une erreur
     */
    @GetMapping("/deleteWidget")
    protected void deleteWidget(HttpServletResponse response, @RequestParam(value = "deleteWidget", defaultValue = "") int idPreference) throws IOException {

        MysqlCon bdd = new MysqlCon();
        bdd.deleteWidget(idPreference);
        response.sendRedirect("/perso");
    }

    /**
     * Modifie un widget de la base donnée
     *
     * @param idPreference l'id d'un widget défini dans la base de donnée
     * @throws IOException une erreur
     */
    @GetMapping("/modifyWidget")
    protected void modifyWidget(HttpServletResponse response, @RequestParam(value = "modifyWidget", defaultValue = "") int idPreference) throws IOException {

        MysqlCon bdd = new MysqlCon();
        bdd.modifyWidget(idPreference);
        response.sendRedirect("/perso");
    }

    /**
     * Supprime un utilisateur
     *
     * @param idUser l'id d'un utilisateur
     * @throws IOException une erreur
     */
    @GetMapping("/deleteUser")
    protected void deleteUser(HttpServletResponse response, @RequestParam(value = "idUser", defaultValue = "") int idUser) throws IOException {
        MysqlCon bdd = new MysqlCon();
        bdd.deleteUser(idUser);
        response.sendRedirect("/CRUD");
    }


}


