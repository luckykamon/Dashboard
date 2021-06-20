package services.google;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class widgetCalendar {

    public static void main(String[] args) {
        System.out.println("iFrame d'un calendrier parametré: " + widgetCalendar.iframeCalendar(null, "WEEK"));
    }

    /**
     * @param user OAuth2User qui permet de savoir si on est connecté au compte google
     * @param mode String égal à WEEK ou MONTH ou AGENDA pour choisir le mode d'affichage, si le string ne correspond pas alors il est égal à WEEK
     * @return iframe contenant l'agenda de l'utilisateur avec le mode choisi
     */
    public static String iframeCalendar(@AuthenticationPrincipal OAuth2User user, String mode) {
        if (!(mode.equals("WEEK") || mode.equals("MONTH") || mode.equals("AGENDA"))) {
            mode = "WEEK";
        }
        try {
            String email = user.getAttribute("email");
            if (email == null) {
                return "<iframe src=\"https://calendar.google.com/calendar/embed\" frameborder=\"0\" width=\"100%\"\n" +
                        "                            height=\"100%\"></iframe>";
            } else {
                String user_email = email.substring(0, email.indexOf('@'));
                return "<iframe width=\"100%\" height=\"100%\" src=\"https://calendar.google.com/calendar/embed?src=" + user_email + "%40gmail.com&amp;showPrint=0&amp;showTabs=0&amp;showTz=0&amp;mode=" + mode + "\" frameborder=\"0\"></iframe>";
            }
        } catch (Exception e) {
            return "<iframe src=\"https://calendar.google.com/calendar/embed\" frameborder=\"0\" width=\"100%\"\n" +
                    "                            height=\"100%\"></iframe>";
        }
    }

}
