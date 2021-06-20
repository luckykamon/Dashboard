package services.google;

public class widgetDrive {

    public static void main(String[] args) {
        System.out.println("iFrame d'un folder: " + widgetDrive.iframeDriveFolder("https://drive.google.com/drive/folders/1JlSlbCsKtwS5OtTZMUSyH7PBfB4ke0pA"));
    }

    /**
     * @param urlFolder String qui contient l'url d'un dossier
     * @return iFrame contenant une liste de fichiers/dossiers contenu dans l'url du dossier drive
     */
    public static String iframeDriveFolder(String urlFolder) {
        if (urlFolder.contains("folders/")) {
            String idFolder = urlFolder.substring(urlFolder.indexOf("folders/") + "folders/".length());
            return "<iframe src=\"https://drive.google.com/embeddedfolderview?id=" + idFolder + " \" width=\"100%\" height=\"100%\" frameborder=\"0\"" +
                    "    style=\"background-color:white\"></iframe>\n";
        }
        return "badUrliframeDriveFolder";
    }

}
