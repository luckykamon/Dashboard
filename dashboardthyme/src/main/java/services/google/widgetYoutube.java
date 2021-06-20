package services.google;

import services.toolboxServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class widgetYoutube {

    public static void main(String[] args) throws IOException {
        System.out.println("Nom d'une vidéo youtube en String" + widgetYoutube.nameVideo("https://www.youtube.com/watch?v=tN6-scHjA3Y&list=PL0F308567431BC033"));
        System.out.println("Nom d'une chaine youtube en String: " + widgetYoutube.nameChannel("https://www.youtube.com/user/absolvideos"));
        System.out.println("Nombre de vue de la chaine en String: " + widgetYoutube.countNumberViewOfChannel("https://www.youtube.com/c/absolvideos/featured"));
        System.out.println("Widget playslist avec balise iframe: " + widgetYoutube.iFrameLastVideoOfChannel("https://www.youtube.com/user/MiniprodFR"));
        System.out.println("Nombre de vue de la vidéo en String: " + widgetYoutube.countNumberViewOfVideo("https://www.youtube.com/watch?v=or1V6EbDGbc&t=548s"));
        System.out.println("Liste de String de commentaires de la vidéo maximum 20: " + widgetYoutube.listCommentsOfVideo("https://www.youtube.com/watch?v=65hkYtU_WPc"));
        affichage();
    }

    private static void affichage() {
        List<List<String>> a = widgetYoutube.listCommentsOfVideo("https://www.youtube.com/watch?v=or1V6EbDGbc");
        StringBuilder comment = new StringBuilder();
        assert a != null;
        for (List<String> strings : a) {
            comment.append("<img src=\"").append(strings.get(0)).append("\">").append(" a écrit:").append("\n");
            for (int j = 1; j < strings.size(); j++) {
                comment.append(strings.get(j)).append("\n");
            }
        }
        System.out.println(comment);
    }


    /**
     * @param urlVideo l'url d'une vidéo youtube
     * @return Le nom de la vidéo youtube
     */
    public static String nameVideo(String urlVideo) {
        try {
            final String apiKey = "AIzaSyDhyWhrqSCZMRJE1xuLO5ExgSJMK-gfuyY";
            String urlPlaylist;
            String idVideo;
            if (urlVideo.contains("www.youtube.com/watch?v=")) {
                idVideo = toolboxServices.getStringBeforeStop(urlVideo.substring(urlVideo.indexOf("www.youtube.com/watch?v=") + "www.youtube.com/watch?v=".length()), '&');
            } else if (urlVideo.contains("youtu.be/")) {
                idVideo = toolboxServices.getStringBeforeStop(urlVideo.substring(urlVideo.indexOf("youtu.be/") + "youtu.be/".length()), '/');
            } else {
                return "BadUrlnameVideo1";
            }
            urlPlaylist = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet&id=" + idVideo + "&key=" + apiKey;
            String s = toolboxServices.requeteGet(urlPlaylist);
            return toolboxServices.searchBeetween(s, "\"title\": \"", '\"').get(0);
        } catch (Exception e) {
            return "BadUrlnameVideo2";
        }
    }

    /**
     * @param urlVideo L'url d'une vidéo
     * @return Une liste contenant les 20 derniers commentaires de la vidéo, null dans le cas d'une erreur
     */
    public static List<List<String>> listCommentsOfVideo(String urlVideo) {
        try {
            final String apiKey = "AIzaSyDhyWhrqSCZMRJE1xuLO5ExgSJMK-gfuyY";
            String urlPlaylist;
            String idVideo;
            if (urlVideo.contains("www.youtube.com/watch?v=")) {
                idVideo = toolboxServices.getStringBeforeStop(urlVideo.substring(urlVideo.indexOf("www.youtube.com/watch?v=") + "www.youtube.com/watch?v=".length()), '&');
            } else if (urlVideo.contains("youtu.be/")) {
                idVideo = toolboxServices.getStringBeforeStop(urlVideo.substring(urlVideo.indexOf("youtu.be/") + "youtu.be/".length()), '/');
            } else {
                return null;
            }
            urlPlaylist = "https://youtube.googleapis.com/youtube/v3/commentThreads?part=snippet&videoId=" + idVideo + "&key=" + apiKey;
            String s = toolboxServices.requeteGet(urlPlaylist);
            List<String> comments = toolboxServices.searchBeetween(s, "textOriginal\": \"", '\"');
            List<List<String>> result = new ArrayList<>();
            for (String urlAuthor : toolboxServices.searchBeetween(s, "\"authorProfileImageUrl\": \"", '\"')) {
                List<String> a = new ArrayList<>();
                a.add(urlAuthor);
                result.add(a);
            }
            for (int k = 0; k < min(comments.size(), result.size()); k++) {
                for (String split : comments.get(k).split("\\\\n")) {
                    result.get(k).add(split);
                }
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param a un int
     * @param b un int
     * @return le minimum entre a et b
     */
    private static int min(int a, int b) {
        return Math.min(a, b);
    }

    /**
     * @param urlVideo L'url d'une vidéo
     * @return un string contenant le nombre de vue de l'url de la vidéo
     */
    public static String countNumberViewOfVideo(String urlVideo) {
        try {
            final String apiKey = "AIzaSyDhyWhrqSCZMRJE1xuLO5ExgSJMK-gfuyY";
            String urlPlaylist;
            String idVideo;
            if (urlVideo.contains("www.youtube.com/watch?v=")) {
                idVideo = toolboxServices.getStringBeforeStop(urlVideo.substring(urlVideo.indexOf("www.youtube.com/watch?v=") + "www.youtube.com/watch?v=".length()), '&');
            } else if (urlVideo.contains("youtu.be/")) {
                idVideo = toolboxServices.getStringBeforeStop(urlVideo.substring(urlVideo.indexOf("youtu.be/") + "youtu.be/".length()), '/');
            } else {
                return "BadUrlcountNumberViewOfVideo";
            }
            urlPlaylist = "https://youtube.googleapis.com/youtube/v3/videos?part=statistics&id=" + idVideo + "&key=" + apiKey;
            String s = toolboxServices.requeteGet(urlPlaylist);
            return toolboxServices.getStringBeforeStop(s.substring(s.indexOf("viewCount") + 13), '\"');
        } catch (Exception e) {
            return "BadUrlcountNumberViewOfVideo";
        }
    }

    /**
     * @param urlChaine l'url d'une chaine youtube
     * @return Le nom d'un chaine youtube à partir de l'url entrée
     */
    public static String nameChannel(String urlChaine) {
        try {
            final String apiKey = "AIzaSyDhyWhrqSCZMRJE1xuLO5ExgSJMK-gfuyY";
            String urlPlaylist = "BadUrlnameChannel1";
            if (urlChaine.contains("youtube.com/c/")) {
                String UsernameWithEndToDel = urlChaine.substring(urlChaine.indexOf("youtube.com/c/") + "youtube.com/c/".length());
                urlPlaylist = "https://youtube.googleapis.com/youtube/v3/channels?part=snippet&forUsername=" + toolboxServices.getStringBeforeStop(UsernameWithEndToDel, '/') + "&key=" + apiKey;
            } else if (urlChaine.contains("youtube.com/user/")) {
                String UsernameWithEndToDel = urlChaine.substring(urlChaine.indexOf("youtube.com/user/") + "youtube.com/user/".length());
                urlPlaylist = "https://youtube.googleapis.com/youtube/v3/channels?part=snippet&forUsername=" + toolboxServices.getStringBeforeStop(UsernameWithEndToDel, '/') + "&key=" + apiKey;
            } else if (urlChaine.contains("youtube.com/channel/")) {
                String UsernameWithEndToDel = urlChaine.substring(urlChaine.indexOf("youtube.com/channel/") + "youtube.com/channel/".length());
                urlPlaylist = "https://youtube.googleapis.com/youtube/v3/channels?part=snippet&id=" + toolboxServices.getStringBeforeStop(UsernameWithEndToDel, '/') + "&key=" + apiKey;
            } else {
                return urlPlaylist;
            }
            String s = toolboxServices.requeteGet(urlPlaylist);
            return toolboxServices.searchBeetween(s, "\"title\": \"", '\"').get(0);
        } catch (Exception e) {
            return "<p>BadUrlnameChannel2</p>";
        }
    }

    /**
     * @param urlChaine l'url d'une chaine youtube
     * @return Renvoie sous forme de String le nombre de vue de la chaine rentré avec urlChaine
     */
    public static String countNumberViewOfChannel(String urlChaine) {
        try {
            final String apiKey = "AIzaSyDhyWhrqSCZMRJE1xuLO5ExgSJMK-gfuyY";
            String urlPlaylist = "BadUrlcountNumberViewOfChannel1";
            if (urlChaine.contains("youtube.com/c/")) {
                String UsernameWithEndToDel = urlChaine.substring(urlChaine.indexOf("youtube.com/c/") + "youtube.com/c/".length());
                urlPlaylist = "https://youtube.googleapis.com/youtube/v3/channels?part=statistics&forUsername=" + toolboxServices.getStringBeforeStop(UsernameWithEndToDel, '/') + "&key=" + apiKey;
            } else if (urlChaine.contains("youtube.com/user/")) {
                String UsernameWithEndToDel = urlChaine.substring(urlChaine.indexOf("youtube.com/user/") + "youtube.com/user/".length());
                urlPlaylist = "https://youtube.googleapis.com/youtube/v3/channels?part=statistics&forUsername=" + toolboxServices.getStringBeforeStop(UsernameWithEndToDel, '/') + "&key=" + apiKey;
            } else if (urlChaine.contains("youtube.com/channel/")) {
                String UsernameWithEndToDel = urlChaine.substring(urlChaine.indexOf("youtube.com/channel/") + "youtube.com/channel/".length());
                urlPlaylist = "https://youtube.googleapis.com/youtube/v3/channels?part=statistics&id=" + toolboxServices.getStringBeforeStop(UsernameWithEndToDel, '/') + "&key=" + apiKey;
            } else {
                return urlPlaylist;
            }
            String s = toolboxServices.requeteGet(urlPlaylist);
            return toolboxServices.getStringBeforeStop(s.substring(s.indexOf("viewCount") + 13), '\"');
        } catch (Exception e) {
            return "<p>BadUrlcountNumberViewOfChannel2</p>";
        }
    }

    /**
     * @param urlChaine l'url d'une chaine youtube
     * @return un iFrame contenant la playslist des dernières vidéos mises en ligne de la chaine donné en paramètre avec urlChaine
     */
    public static String iFrameLastVideoOfChannel(String urlChaine) {
        try {
            final String apiKey = "AIzaSyDhyWhrqSCZMRJE1xuLO5ExgSJMK-gfuyY";
            String urlPlaylist = "BadUrliFrameLastVideoOfChannel1";
            String idPlaylist;
            if (urlChaine.contains("youtube.com/c/")) {
                String UsernameWithEndToDel = urlChaine.substring(urlChaine.indexOf("youtube.com/c/") + "youtube.com/c/".length());
                urlPlaylist = "https://youtube.googleapis.com/youtube/v3/channels?part=" + "statistics" + "&forUsername=" + toolboxServices.getStringBeforeStop(UsernameWithEndToDel, '/') + "&key=" + apiKey;
            } else if (urlChaine.contains("youtube.com/user/")) {
                String UsernameWithEndToDel = urlChaine.substring(urlChaine.indexOf("youtube.com/user/") + "youtube.com/user/".length());
                urlPlaylist = "https://youtube.googleapis.com/youtube/v3/channels?part=" + "statistics" + "&forUsername=" + toolboxServices.getStringBeforeStop(UsernameWithEndToDel, '/') + "&key=" + apiKey;
            } else if (urlChaine.contains("youtube.com/channel/")) {
                String UsernameWithEndToDel = urlChaine.substring(urlChaine.indexOf("youtube.com/channel/") + "youtube.com/channel/".length());
                for (int k = 0; k < UsernameWithEndToDel.length(); k++) {
                    if (UsernameWithEndToDel.charAt(k) == '/') {
                        idPlaylist = "UU" + UsernameWithEndToDel.substring(2, k);
                        return "<iframe class=\"youtube\" src=\"https://www.youtube.com/embed/videoseries?list=" + idPlaylist + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
                    }
                }
                idPlaylist = "UU" + UsernameWithEndToDel.substring(2);
                return "<iframe class=\"youtube\" src=\"https://www.youtube.com/embed/videoseries?list=" + idPlaylist + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
            } else {
                return urlPlaylist;
            }

            String s = toolboxServices.requeteGet(urlPlaylist);

            idPlaylist = "UU" + s.substring(276, 298);
            return "<iframe class=\"youtube\" src=\"https://www.youtube.com/embed/videoseries?list=" + idPlaylist + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
        } catch (Exception e) {
            return "<p>BadUrliFrameLastVideoOfChannel2</p>";
        }

    }
}
