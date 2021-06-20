package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class toolboxServices {

    /**
     * @param url d'une méthode GET
     * @return Un string contenant la réponse d'une requête GET
     * @throws IOException Problème de requête GET
     */
    public static String requeteGet(String url) throws IOException {
        final String USER__AGENT = "Mozilla/5.0";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER__AGENT);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        List<String> response = new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            response.add(inputLine);
        }
        in.close();

        return response.toString();
    }

    /**
     * @param s Un String où l'on souhaite rechercher quelque chose
     * @param before Ce qu'il y a avant un String
     * @param after Ce qu'il y a après un un String
     * @return Renvoie sous forme de liste, tout les String compris entre before et after
     */
    public static List<String> searchBeetween(String s, String before, char after) {
        List<String> allResult = new ArrayList<>();
        String usernameWithEndToDel = s;
        int indexBefore;
        String beetween;
        do {
            indexBefore = s.indexOf(before);
            if (indexBefore == -1 || s.indexOf(before) + before.length() >= usernameWithEndToDel.length()) {
                break;
            }
            usernameWithEndToDel = s.substring(s.indexOf(before) + before.length());
            beetween = getStringBeforeStop(usernameWithEndToDel, after);
            allResult.add(beetween);
            s = s.substring(s.indexOf(before) + beetween.length() + before.length());
        } while (true);
        return allResult;
    }

    /**
     * @param stop                 char qui délimite
     * @param usernameWithEndToDel un String
     * @return la String usernameWithEndToDel du début jusqu'a l'apparition d'un stop exclus ou usernameWithEndToDel en entier si aucun stop n'est trouvé
     */
    public static String getStringBeforeStop(String usernameWithEndToDel, char stop) {
        for (int k = 0; k < usernameWithEndToDel.length(); k++) {
            if (usernameWithEndToDel.charAt(k) == stop) {
                return usernameWithEndToDel.substring(0, k);
            }
        }
        return usernameWithEndToDel;
    }

}
