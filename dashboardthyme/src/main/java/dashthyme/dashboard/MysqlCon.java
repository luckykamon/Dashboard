package dashthyme.dashboard;

import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


class MysqlCon extends DashboardApplication {
    private String idLogin = "";

    public String getIdLogin() {
        return idLogin;
    }


    // Methodes de base

    /**
     * method qui va lire les préférences de l'utilisateur dans la BDD et les ajouter aux variables (arraylist)
     *
     * @param idLogin l'ID de l'utilisateur (id dans la table login)
     */
    public void getBDD(String idLogin) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
//here dashboard is database name, root is username and password
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from widgets where id =" + idLogin);
            while (rs.next()) {
                addIdPreference(rs.getInt(1));
                addDiscordActivated(rs.getBoolean(3));
                addYoutubeVueChannelActivated(rs.getBoolean(4));
                addYoutubeVueVideoActivated(rs.getBoolean(5));
                addYoutubePlaylistActivated(rs.getBoolean(6));
                addYoutubeCommentActivated(rs.getBoolean(7));
                addAmazonActivated(rs.getBoolean(8));
                addWeatherTempActivated(rs.getBoolean(9));
                addWeatherPictoActivated(rs.getBoolean(10));
                addDiscordIdServer(rs.getString(11));
                addYoutubeVueChannelURL(rs.getString(12));
                addYoutubeVueVideoURL(rs.getString(13));
                addYoutubePlaylistURL(rs.getString(14));
                addYoutubeCommentURL(rs.getString(15));
                addWeatherTempVille(rs.getString(16));
                addWeatherTempDays(rs.getInt(17));
                addWeatherPictoVille(rs.getString(18));
                addWeatherPictoDays(rs.getInt(19));
                addWidgetTimer1(rs.getInt(20));
                addWidgetTimer2(rs.getInt(21));
                addWidgetTimer3(rs.getInt(22));
                addWidgetTimer4(rs.getInt(23));
                addAmazonTitreRecherche(rs.getString(24));
                addDiscordDarkMode(rs.getString(25));
                addGoogleCalendarActivated(rs.getBoolean(26));
                addGoogleDriveActivated(rs.getBoolean(27));
                addGoogleCalendarFormat(rs.getString(28));
                addGoogleDriveURL(rs.getString(29));


                if (rs.getInt(3) == 1) {
                    addTypeWidget("discord");
                } else if (rs.getInt(8) == 1) {
                    addTypeWidget("amazon");
                }
                else if (rs.getInt(9)==1 || rs.getInt(10)==1){
                    addTypeWidget("weather");
                }
                else if (rs.getInt(26)==1){
                    addTypeWidget("calendar");
                }
                else if (rs.getInt(27)==1){
                addTypeWidget("drive");
                }

                else{
                    addTypeWidget("youtube");
                }
            }

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }


    /**
     * Connexion standard sans OAUTH : Method qui check le login et le mdp donné par l'utilisateur avant d'autoriser la connexion et de créer le cookie
     *
     * @param loginGiven pseudo (pris via formulaire)
     * @param mdpGiven   mot de passe (pris via formulaire)
     */
    public void loginBDD(String loginGiven, String mdpGiven) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            Statement stmt = con.createStatement();
            String query;
            query = String.format("select id,mdp from login where pseudo = '%s';", loginGiven);
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if (mdpGiven.equals(rs.getString(2))) {
                this.idLogin = "" + rs.getInt(1);
            }
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * en connexion OAUTH : Check si l'ID existe déjà dans la BDD (s'utilise avec CreateOAuthAccountBDD). Si l'ID n'existe pas, on créé un compte.
     *
     * @param subAuth paramètre "Sub" récupéré de l'OAUTH : un indentifiant unique d'un utilisteur Google, Facebook, Git, etc.
     * @return True if ID existe, false otherwise
     */
    public boolean isIdExist(String subAuth) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            Statement stmt = con.createStatement();
            String query;
            query = String.format("select pseudo from login where id = '%s';", subAuth);
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                con.close();
                return true;
            } else {
                con.close();
                return false;
            }


        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    /**
     * Créé un compte dans la BDD, et le lie au sub de l'OAUTH
     *
     * @param subAuth paramètre "Sub" récupéré de l'OAUTH : un identifiant unique d'un utilisteur Google, Facebook, Git, etc.
     */
    public void createOAuthAccountBDD(String subAuth) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("INSERT INTO login (id, pseudo, mdp) VALUES ('%s','%s','%s');", subAuth, subAuth, subAuth);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * créé un compte standard quand l'utilisateur clique sur s'inscrire.
     *
     * @param pseudo pseudo (via formulaire)
     * @param mdp    mot de passe (via formulaire)
     */
    public void createAccountStandardBDD(String pseudo, String mdp) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("INSERT INTO login (pseudo, mdp) VALUES ('%s','%s');", pseudo, mdp);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    // WIDGET PUT BDD ----------------------------------

    /**
     * Ajoute un widget Youtube
     *
     * @param id                         id de l'utilisateur (pris via cookie)
     * @param youtubeVueChannelActivated Widget1 activé (boolean)
     * @param youtubeVueVideoActivated   Widget2 activé (boolean)
     * @param youtubePlaylistActivated   Widget3 activé (boolean)
     * @param youtubeCommentActivated    Widget4 activé (boolean)
     * @param youtubeVueChannelURL       URL de la chaine
     * @param youtubeVueVideoURL         URL de la video
     * @param youtubePlaylistChannelURL  URL de la chaine
     * @param youtubeCommentVideoURL     URL de la video
     * @param timer1                     timer du widget 1
     * @param timer2                     timer du widget 2
     * @param timer3                     timer du widget 3
     * @param timer4                     timer du widget 4
     */
    public void addWidgetYoutubeBDD(int id, int youtubeVueChannelActivated, int youtubeVueVideoActivated, int youtubePlaylistActivated, int youtubeCommentActivated, String youtubeVueChannelURL, String youtubeVueVideoURL, String youtubePlaylistChannelURL, String youtubeCommentVideoURL, int timer1, int timer2, int timer3, int timer4) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("INSERT INTO widgets (id, youtubeVueChannelActivated, youtubeVueVideoActivated, youtubePlaylistActivated, youtubeCommentActivated, youtubeVueChannelURL, youtubeVueVideoURL, youtubePlaylistURL, youtubeCommentURL, timer1, timer2, timer3, timer4) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s');", id, youtubeVueChannelActivated, youtubeVueVideoActivated, youtubePlaylistActivated, youtubeCommentActivated, youtubeVueChannelURL, youtubeVueVideoURL, youtubePlaylistChannelURL, youtubeCommentVideoURL, timer1, timer2, timer3, timer4);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * ajouter un widget amazon
     *
     * @param id                   id utilisateur (pris via cookie)
     * @param amazonActivated      boolean widget amazon activé
     * @param amazonTitreRecherche String de l'objet cherché
     * @param timer1               timer de refresh du widget
     */
    public void addWidgetAmazonBDD(int id, int amazonActivated, String amazonTitreRecherche, int timer1) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("INSERT INTO widgets (id, amazonActivated, timer1, titreRecherche) VALUES ('%s','%s','%s','%s');", id, amazonActivated, timer1, amazonTitreRecherche);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * Ajout d'un widget Weather
     *
     * @param id                    id de l'utilisateur (pris via cookie)
     * @param weatherTempActivated  Affichage des temperatures (config boolean)
     * @param weatherPictoActivated Affichage des pictogrammes pluie/soleil etc (config boolean)
     * @param weatherTempVille      Ville choisie par l'utilisateur
     * @param weatherTempDays       Nombre de jours de météo à afficher (1-7)
     * @param weatherPictoVille     same as TempVille
     * @param weatherPictoDays      same as TempDays
     * @param timer1                timer de refresh du widget
     */
    public void addWidgetWeatherBDD(int id, int weatherTempActivated, int weatherPictoActivated, String weatherTempVille, int weatherTempDays, String weatherPictoVille, int weatherPictoDays, int timer1) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("INSERT INTO widgets (id, weatherTempActivated, weatherPictoActivated, weatherTempVille, weatherTempDays, weatherPictoVille, weatherPictoDays, timer1) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s');", id, weatherTempActivated, weatherPictoActivated, weatherTempVille, weatherTempDays, weatherPictoVille, weatherPictoDays, timer1);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * @param id               id de l'utilisateur (pris via cookie)
     * @param discordActivated Affichage du widget (boolean)
     * @param discordIdServer  ID du serveur discord à afficher
     * @param discordDarkMode  Dark Mode (si oui, dark mode, si non, light mode)
     * @param timer1           timer de refresh du widget
     */
    public void addWidgetDiscordBDD(int id, int discordActivated, String discordIdServer, String discordDarkMode, int timer1) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("INSERT INTO widgets (id, discordActivated, discordIdServer, timer1 , discordDarkMode) VALUES ('%s','%s','%s','%s','%s');", id, discordActivated, discordIdServer, timer1, discordDarkMode);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * @param id                      id de l'utilisateur (pris via cookie)
     * @param googleCalendarActivated affichage du calendar (boolean)
     * @param googleCalendarFormat    format du calendrier : semaine, mois ou agenda (affiché sous forme de liste déroulante)
     * @param timer1                  timer de refresh du widget
     */
    public void addWidgetGoogleCalendarBDD(int id, int googleCalendarActivated, String googleCalendarFormat, int timer1) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("INSERT INTO widgets (id, timer1, googleCalendarActivated, googleCalendarFormat) VALUES ('%s','%s','%s','%s');", id, timer1, googleCalendarActivated, googleCalendarFormat);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * @param id                   id de l'utilisateur (pris via cookie)
     * @param googleDriveActivated affichage du drive (boolean)
     * @param googleDriveURL       URL du dossier Drive à afficher
     * @param timer1               timer de refresh du widget
     */
    public void addWidgetGoogleDriveBDD(int id, int googleDriveActivated, String googleDriveURL, int timer1) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("INSERT INTO widgets (id, timer1, googleDriveActivated, googleDriveURL) VALUES ('%s','%s','%s','%s');", id, timer1, googleDriveActivated, googleDriveURL);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Method qui delete un widget donné
     *
     * @param idPreference ID du widget dans la BDD
     */
    public void deleteWidget(int idPreference) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("DELETE FROM widgets where idPreference = %s;", idPreference);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Method qui modifie un widget donné
     *
     * @param idPreference ID du widget dans la BDD
     */
    public void modifyWidget(int idPreference) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");

//            String query;
//            query = String.format("DELETE FROM widgets where idPreference = %s;", idPreference);
//            Statement stmt = con.createStatement();
//            stmt.executeUpdate(query);
            con.close();


        } catch (Exception e) {
            System.out.println(e);
        }
    }


    // WIDGET UPDATE BDD --------------------------------



    /**
     * Modifie un widget Youtube
     *
     * @param idPreference                         id widget
     * @param youtubeVueChannelActivated Widget1 activé (boolean)
     * @param youtubeVueVideoActivated   Widget2 activé (boolean)
     * @param youtubePlaylistActivated   Widget3 activé (boolean)
     * @param youtubeCommentActivated    Widget4 activé (boolean)
     * @param youtubeVueChannelURL       URL de la chaine
     * @param youtubeVueVideoURL         URL de la video
     * @param youtubePlaylistChannelURL  URL de la chaine
     * @param youtubeCommentVideoURL     URL de la video
     * @param timer1                     timer du widget 1
     * @param timer2                     timer du widget 2
     * @param timer3                     timer du widget 3
     * @param timer4                     timer du widget 4
     */
    public void editWidgetYoutubeBDD(int idPreference, int youtubeVueChannelActivated, int youtubeVueVideoActivated, int youtubePlaylistActivated, int youtubeCommentActivated, String youtubeVueChannelURL, String youtubeVueVideoURL, String youtubePlaylistChannelURL, String youtubeCommentVideoURL, int timer1, int timer2, int timer3, int timer4) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("UPDATE widgets SET youtubeVueChannelActivated = '%s', youtubeVueVideoActivated = '%s', youtubePlaylistActivated = '%s', youtubeCommentActivated = '%s', youtubeVueChannelURL = '%s', youtubeVueVideoURL = '%s', youtubePlaylistURL = '%s', youtubeCommentURL = '%s', timer1 = '%s', timer2 = '%s', timer3 = '%s', timer4 = '%s' WHERE idPreference = '%s';", youtubeVueChannelActivated, youtubeVueVideoActivated, youtubePlaylistActivated, youtubeCommentActivated, youtubeVueChannelURL, youtubeVueVideoURL, youtubePlaylistChannelURL, youtubeCommentVideoURL, timer1, timer2, timer3, timer4, idPreference);
            System.out.println(query);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * ajouter un widget amazon
     *
     * @param idPreference                   id widget
     * @param amazonActivated      boolean widget amazon activé
     * @param amazonTitreRecherche String de l'objet cherché
     * @param timer1               timer de refresh du widget
     */
    public void editWidgetAmazonBDD(int idPreference, int amazonActivated, String amazonTitreRecherche, int timer1) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("UPDATE widgets SET amazonActivated = '%s', timer1 = '%s', titreRecherche = '%s' WHERE idPreference = '%s';", amazonActivated, timer1, amazonTitreRecherche, idPreference);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * Ajout d'un widget Weather
     *
     * @param idPreference                id d'un widget
     * @param weatherTempActivated  Affichage des temperatures (config boolean)
     * @param weatherPictoActivated Affichage des pictogrammes pluie/soleil etc (config boolean)
     * @param weatherTempVille      Ville choisie par l'utilisateur
     * @param weatherTempDays       Nombre de jours de météo à afficher (1-7)
     * @param weatherPictoVille     same as TempVille
     * @param weatherPictoDays      same as TempDays
     * @param timer1                timer de refresh du widget
     */
    public void editWidgetWeatherBDD(int idPreference, int weatherTempActivated, int weatherPictoActivated, String weatherTempVille, int weatherTempDays, String weatherPictoVille, int weatherPictoDays, int timer1) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("UPDATE widgets SET weatherTempActivated = '%s', weatherPictoActivated = '%s', weatherTempVille = '%s', weatherTempDays = '%s', weatherPictoVille = '%s', weatherPictoDays = '%s', timer1 = '%s' WHERE idPreference = '%s';", weatherTempActivated, weatherPictoActivated, weatherTempVille, weatherTempDays, weatherPictoVille, weatherPictoDays, timer1, idPreference);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * @param idPreference               id du widget
     * @param discordActivated Affichage du widget (boolean)
     * @param discordIdServer  ID du serveur discord à afficher
     * @param discordDarkMode  Dark Mode (si oui, dark mode, si non, light mode)
     * @param timer1           timer de refresh du widget
     */
    public void editWidgetDiscordBDD(int idPreference, int discordActivated, String discordIdServer, String discordDarkMode, int timer1) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("UPDATE widgets SET discordActivated = '%s', discordIdServer = '%s', timer1  = '%s', discordDarkMode = '%s' WHERE idPreference = '%s';", discordActivated, discordIdServer, timer1, discordDarkMode, idPreference);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * @param idPreference                      id du widget
     * @param googleCalendarActivated affichage du calendar (boolean)
     * @param googleCalendarFormat    format du calendrier : semaine, mois ou agenda (affiché sous forme de liste déroulante)
     * @param timer1                  timer de refresh du widget
     */
    public void editWidgetGoogleCalendarBDD(int idPreference, int googleCalendarActivated, String googleCalendarFormat, int timer1) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("UPDATE widgets SET timer1 = '%s', googleCalendarActivated = '%s', googleCalendarFormat = '%s' WHERE idPreference = '%s';", timer1, googleCalendarActivated, googleCalendarFormat, idPreference);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * @param idPreference                  id du widget
     * @param googleDriveActivated affichage du drive (boolean)
     * @param googleDriveURL       URL du dossier Drive à afficher
     * @param timer1               timer de refresh du widget
     */
    public void editWidgetGoogleDriveBDD(int idPreference, int googleDriveActivated, String googleDriveURL, int timer1) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query;
            query = String.format("UPDATE widgets SET timer1 = '%s', googleDriveActivated = '%s', googleDriveURL = '%s' WHERE idPreference = '%s';", timer1, googleDriveActivated, googleDriveURL, idPreference);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }








//    /**
//     * modifie un widget amazon
//     *
//     * @param idPreference                   id utilisateur (pris via cookie)
//     * @param amazonActivated      boolean widget amazon activé
//     * @param amazonTitreRecherche String de l'objet cherché
//     * @param timer1               timer de refresh du widget
//     */
//    public void editWidgetAmazonBDD(int idPreference, int amazonActivated, String amazonTitreRecherche, int timer1) {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
//            String query;
//            query = String.format("INSERT INTO widgets (id, amazonActivated, timer1, titreRecherche) VALUES ('%s','%s','%s','%s');", id, amazonActivated, timer1, amazonTitreRecherche);
//            Statement stmt = con.createStatement();
//            stmt.executeUpdate(query);
//            con.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//
//    }
//
//    /**
//     * modifie d'un widget Weather
//     *
//     * @param idPreference                    id de l'utilisateur (pris via cookie)
//     * @param weatherTempActivated  Affichage des temperatures (config boolean)
//     * @param weatherPictoActivated Affichage des pictogrammes pluie/soleil etc (config boolean)
//     * @param weatherTempVille      Ville choisie par l'utilisateur
//     * @param weatherTempDays       Nombre de jours de météo à afficher (1-7)
//     * @param weatherPictoVille     same as TempVille
//     * @param weatherPictoDays      same as TempDays
//     * @param timer1                timer de refresh du widget
//     */
//    public void editWidgetWeatherBDD(int idPreference, int weatherTempActivated, int weatherPictoActivated, String weatherTempVille, int weatherTempDays, String weatherPictoVille, int weatherPictoDays, int timer1) {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
//            String query;
//            query = String.format("INSERT INTO widgets (id, weatherTempActivated, weatherPictoActivated, weatherTempVille, weatherTempDays, weatherPictoVille, weatherPictoDays, timer1) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s');", id, weatherTempActivated, weatherPictoActivated, weatherTempVille, weatherTempDays, weatherPictoVille, weatherPictoDays, timer1);
//            Statement stmt = con.createStatement();
//            stmt.executeUpdate(query);
//            con.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    /** modifie un widget discord
//     * @param idPreference               id de l'utilisateur (pris via cookie)
//     * @param discordActivated Affichage du widget (boolean)
//     * @param discordIdServer  ID du serveur discord à afficher
//     * @param discordDarkMode  Dark Mode (si oui, dark mode, si non, light mode)
//     * @param timer1           timer de refresh du widget
//     */
//    public void editWidgetDiscordBDD(int idPreference, int discordActivated, String discordIdServer, String discordDarkMode, int timer1) {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
//            String query;
//            query = String.format("INSERT INTO widgets (id, discordActivated, discordIdServer, timer1 , discordDarkMode) VALUES ('%s','%s','%s','%s','%s');", id, discordActivated, discordIdServer, timer1, discordDarkMode);
//            Statement stmt = con.createStatement();
//            stmt.executeUpdate(query);
//            con.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    /** modifie un widget calendar
//     * @param idPreference                      id de l'utilisateur (pris via cookie)
//     * @param googleCalendarActivated affichage du calendar (boolean)
//     * @param googleCalendarFormat    format du calendrier : semaine, mois ou agenda (affiché sous forme de liste déroulante)
//     * @param timer1                  timer de refresh du widget
//     */
//    public void editWidgetGoogleCalendarBDD(int idPreference, int googleCalendarActivated, String googleCalendarFormat, int timer1) {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
//            String query;
//            query = String.format("INSERT INTO widgets (id, timer1, googleCalendarActivated, googleCalendarFormat) VALUES ('%s','%s','%s','%s');", id, timer1, googleCalendarActivated, googleCalendarFormat);
//            Statement stmt = con.createStatement();
//            stmt.executeUpdate(query);
//            con.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    /** modifie un widget google Drive
//     * @param idPreference                   id de l'utilisateur (pris via cookie)
//     * @param googleDriveActivated affichage du drive (boolean)
//     * @param googleDriveURL       URL du dossier Drive à afficher
//     * @param timer1               timer de refresh du widget
//     */
//    public void editWidgetGoogleDriveBDD(int idPreference, int googleDriveActivated, String googleDriveURL, int timer1) {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
//            String query;
//            query = String.format("INSERT INTO widgets (id, timer1, googleDriveActivated, googleDriveURL) VALUES ('%s','%s','%s','%s');", id, timer1, googleDriveActivated, googleDriveURL);
//            Statement stmt = con.createStatement();
//            stmt.executeUpdate(query);
//            con.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }



    // CRUD

    /**
     * Method qui ajoute tous les utilisateurs de la BDD dans un Array, pour pouvoir les afficher dans le panneau admin (en tableau)
     */
    public void getUserListBDD() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            Statement stmt = con.createStatement();
            String query;
            query = String.format("select id,pseudo from login;");
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                addIdUser(rs.getInt(1));
                addPseudoUser(rs.getString(2));
            }
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Method qui delete un utilisateur donné (en cliquant sur une croix dans le tableau admin CRUD)
     *
     * @param idUser ID de l'utilisateur (table login)
     */
    public void deleteUser(int idUser) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboard?useSSL=false", "root", "");
            String query1;
            String query2;
            query1 = String.format("DELETE FROM widgets where id = %s;", idUser);
            query2 = String.format("DELETE FROM login where id = %s;", idUser);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query1);
            stmt.executeUpdate(query2);
            con.close();


        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
