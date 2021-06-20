package services.amazon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import services.toolboxServices;

public class widgetAmazon {

    public static void main(String[] args) throws IOException {
        // TEST
        System.out.println("Liste contenant une liste de titre et une liste de lien: " + listTitleAndLinkSearchAmazon("test"));
    }

    /**
     * @param title La recherche que l'on souhaite effectuer, sans prendre d'accents
     * @return Liste contenant une liste de titre et une liste de lien correspondants à la recherche
     * @throws IOException Mauvais envoie d'url, probablement lié à un titre avec des carractères spéciaux
     */
    public static List<List<String>> listTitleAndLinkSearchAmazon(String title) throws IOException {
        List<List<String>> result = new ArrayList<>();
        String requete = toolboxServices.requeteGet("https://api.rainforestapi.com/request?api_key=C557A7EC3A724B1B9156DAAF5EFE1331&type=search&output=json&language=fr_FR&amazon_domain=amazon.fr&search_term=" + title + "\n");
        result.add(toolboxServices.searchBeetween(requete, ",\"title\":\"", '\"'));
        result.add(toolboxServices.searchBeetween(requete, ",\"link\":\"", '\"'));
        return result;
    }

}
