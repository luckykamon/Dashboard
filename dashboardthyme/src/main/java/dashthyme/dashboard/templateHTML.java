package dashthyme.dashboard;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

public class templateHTML {

    /**
     * @param insert String a insérer dans le header comme un <script></script>, <meta> ou <link>
     * @return Le header du site avec insert mis dedans
     */
    public static String head(String insert) {
        //language=HTML
        return """
                <!DOCTYPE html>
                <html lang="FR">
                <head>
                    <title>Dashboard</title>
                    """ +
                insert +
                """
                                                
                            <meta charset="utf-8">
                                                    <meta name="viewport" content="width=device-width, initial-scale=1">
                            <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
                            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                            <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
                            <script>
                                function allowDrop(ev) {
                                    ev.preventDefault();
                                }
                                        
                                function drag(ev) {
                                    ev.dataTransfer.setData("text", ev.target.id);
                                }
                                        
                                function drop(ev) {
                                    ev.preventDefault();
                                    const data = event.dataTransfer.getData("text"); // id origin
                                    const divOrigin = document.getElementById(data); // origin
                                    const divParentOrigine = divOrigin.parentElement; //parent-origin
                                    let divDestination = event.target;
                                    while (!(divDestination.classList.contains("children"))) {
                                        divDestination = divDestination.parentElement;
                                    }
                                    const divParentDestination = divDestination.parentElement; //parent-destination
                                    const childOrigin = divParentOrigine.removeChild(divOrigin);
                                    const childDestination = divParentDestination.removeChild(divDestination);
                                    divParentOrigine.appendChild(childDestination);
                                    divParentDestination.appendChild(childOrigin);
                                    const valeur1=divParentOrigine.id.substring(10);
                                    const valeur2 = divParentDestination.id.substring(10);
                                    $.ajax({
                                                url: "/requestOrdreIdPreference?data1=" + valeur1 +"&data2=" +valeur2,
                                                contentType: "application/json", // Pour préciser le type de données qui transite
                                                type: "GET",
                                                success: function (result) {
                                                console.log("Success");
                                                },
                                                error: function (err) {
                                                    alert("Erreur");
                                                    console.log(err);
                                                },
                                                done: function (e) {
                                                    console.log("DONE");
                                                }
                                            });
                                }
                                </script>
                            <style>
                                /* Remove the navbar's default margin-bottom and rounded borders */
                                .navbar {
                                    margin-bottom: 0;
                                    border-radius: 0;
                                }
                                        
                                /* Add a gray background color and some padding to the footer */
                                .footer1 {
                                    background-color: #AAB7B8;
                                    padding: 25px;
                                }
                                        
                                .footer {
                                    background-color: #AAB7B8;
                                    padding: 25px;
                                    position: fixed;
                                    bottom: 0;
                                    width: 100%;
                                }
                                        
                                .carousel-inner img {
                                    width: 100%; /* Set width to 100% */
                                    margin: auto;
                                    min-height: 200px;
                                }
                                        
                                #titreYt {
                                    color: darkred;
                                }
                                        
                                #titreAm {
                                    color: orangered;
                                }
                                        
                                #titreWt {
                                    color: #2f4f4f;
                                }
                                        
                                .comment {
                                    height: 500px;
                                    overflow: scroll;
                                }
                                        
                                /* Hide the carousel text when the screen is less than 600 pixels wide */
                                @media (max-width: 600px) {
                                    .carousel-caption {
                                        display: none;
                                    }
                                }
                            </style>
                        </head>
                        """;
    }

    /**
     * @param page    String correspond à la page d'où est appelée la fonction
     * @param request Données du site
     * @return La nav du site
     */
    public static String nav(String page, HttpServletRequest request) {
        String navLogin = """
                <body>
                <nav class="navbar navbar-inverse">
                    <div class="container-fluid">
                        <div class="navbar-header">
                            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                            </button>
                            <a class="navbar-brand" href="/">Dashboard</a>
                        </div>
                        <div class="collapse navbar-collapse" id="myNavbar">
                            <ul class="nav navbar-nav">
                                <li""";
        if (page.equals("/")) {
            navLogin += " class=\"active\"";
        }
        navLogin += "><a href=\"/\">Accueil</a></li>\n" +
                "                <li";
        if (page.equals("/perso")) {
            navLogin += " class=\"active\"";
        }
        navLogin += "><a  href=\"/perso\">Espace personnel</a></li>\n" +
                "                <li";
        if (page.equals("/preferences")) {
            navLogin += " class=\"active\"";
        }
        navLogin += "><a href=\"/preferences\">Préférences</a></li>\n";
        if (!DashboardApplication.getCookieValue(request.getCookies(), "idLogin", "").equals("")) {
            int cookieLogin = Integer.parseInt(DashboardApplication.getCookieValue(request.getCookies(), "idLogin", ""));
            if (cookieLogin <= 3 && cookieLogin >= 1) {
                navLogin += "<li";
                if (page.equals("/CRUD")) {
                    navLogin += " class=\"active\"";
                }
                navLogin += """
                        ><a href="/CRUD">CRUD</a></li>
                        """;

            }
        }
        navLogin += """
                </ul>
                <ul class="nav navbar-nav navbar-right">""";
        navLogin += "<li";
        if (page.equals("/connexion")) {
            navLogin += " class=\"active\"";
        }
        String logInOut;
        String redirection;
        if (DashboardApplication.getCookieValue(request.getCookies(), "idLogin", "").equals("")) {
            logInOut = "Login";
            redirection = "/connexion";
        } else {
            logInOut = "Logout";
            redirection = "/logout";
        }
        navLogin += "><a href=\"" + redirection + "\"><span class=\"glyphicon glyphicon-log-in\"></span> " + logInOut + "</a></li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</nav>";
        return navLogin;
    }

    /**
     * @return Il s'agit du slide de la page d'accueil
     */
    public static String carousel() {
        //language=HTML
        return """
                <div id="myCarousel" class="carousel slide" data-ride="carousel">
                    <!-- Indicators -->
                    <ol class="carousel-indicators">
                        <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                        <li data-target="#myCarousel" data-slide-to="1"></li>
                    </ol>
                    <!-- Wrapper for slides -->
                    <div class="carousel-inner" role="listbox">
                        <div class="item active">
                            <img src="https://i.ibb.co/FH5PRZb/img1.png"
                                 style="height:55%; width: 100%" alt="Image1">
                            <div class="carousel-caption">
                                <h1 style="color:black">Vos services à portée de main</h1>
                            </div>
                        </div>

                        <div class="item">
                            <img src="https://i.ibb.co/JFJjZyp/img2.jpg"
                                 style="width:100%; height: 55%" alt="Image2">

                            <div class="carousel-caption">
                                <h1 style="color:white">Tous vos services accessibles en un clic</h1>
                            </div>
                        </div>
                    </div>
                    <!-- Left and right controls -->
                    <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
                        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                        <span class="sr-only">Previous</span>
                    </a>
                    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
                        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                        <span class="sr-only">Next</span>
                    </a>
                </div>""";
    }

    /**
     * @return Le footer du site
     */
    public static String footer() {
        return """
                <footer class="container-fluid text-center footer1 col-xs-12 col-sm-12 col-md-12 col-lg-12">
                    <p>Dashboard project by Corentin Herisson, Tiphaine Blanchard & Lucas Bodin</p>
                </footer>
                </body>
                </html>""";
    }

    /**
     * @param nbId numéro d'un id
     * @return Une div permettant d'effectuer le drag & drop avec un id égal à nameId + nbId
     */
    public static String beginDragAndDrop(int nbId) {
        return "<div id=\"ondragover" + "" + nbId + "\" ondrop=\"drop(event)\" ondragover=\"allowDrop(event)\">";
    }

    public static String about() {
        String corps = """
                {
                " client ": {
                  " host ": "localhost:8080"
                },
                " server ": {
                  "current_time ": """;
        corps += Instant.now().getEpochSecond();
        corps += """
                    ,
                    "services ": [
                      {
                        "name ": "Google",
                        "widgets ": [{
                            "name ": "Playlist youtube",
                            "description ": "Affiche la playlist des dernières vidéos sorties de la chaîne",
                            "params ": [
                              {
                                "name ": "url",
                                "type ": "string"
                              }
                            ]
                          },
                          {
                            "name ": "Nombre de vues de la chaîne",
                            "description ": "Affiche le nombre de vues de la chaîne",
                            "params ": [
                              {
                                "name ": "url",
                                "type ": "string"
                              }
                            ]
                          },
                          {
                            "name ": "Nombre de vues de la vidéo",
                            "description ": "Affiche le nombre de vues de la vidéo",
                            "params ": [
                              {
                                "name ": "url",
                                "type ": "string"
                              }
                            ]
                          },
                          {
                            "name ": "Liste des commentaires",
                            "description ": "Affiche les 20 premiers commentaires de la vidéo",
                            "params ": [
                              {
                                "name ": "url",
                                "type ": "string"
                              }
                            ]
                          },
                          {
                            "name ": "Calendrier",
                            "description ": "Affiche un calendrier (personnalisé si on est connecté avec notre compte google)",
                            "params ": [
                              {
                                "name ": "format",
                                "type ": "string"
                              }
                            ]
                          },
                          {
                            "name ": "Drive",
                            "description ": "Affiche notre drive personnel)",
                            "params ": [
                              {
                                "name ": "url",
                                "type ": "string"
                              }
                            ]
                          }
                        ]
                      },
                      {
                        "name ": "Amazon",
                        "widgets ": [{
                                "name ": "Amazon",
                                "description ": "Affiche une liste de résultats de recherche.",
                                "params ": [
                                  {
                                    "name ": "objet recherché",
                                    "type ": "string"
                                  }
                                ]
                              }
                        ]
                      },
                      {
                        "name ": "Discord",
                        "widgets ": [
                              {
                                "name ": "Discord",
                                "description ": "Affiche la liste des personnes connectées sur un serveur disord",
                                "params ": [{
                                    "name ": "couleur du fond",
                                    "type ": "string"
                                  },
                                  {
                                    "name ": "id du serveur",
                                    "type ": "string"
                                  }]
                              }]
                      },
                      {
                        "name ": "Météo",
                        "widgets ": [{
                          "name ": "Météo",
                          "description ": "Affiche la météo d'une ville avec la température et des pictogrammes",
                          "params ": [
                            {
                              "name ": "Température",
                              "type ": "integer"
                            },
                            {
                              "name ": "Pictogrammes",
                              "type ": "integer"
                            },
                            {
                              "name ": "Nombre de jours",
                              "type ": "integer"
                            },
                            {
                              "name ": "Ville",
                              "type ": "string"
                            }]
                        }
                        ]
                      }
                    ]
                  }
                }
                                """;
        return corps;
    }

    public static String userDisconnected() {
        return """
                <div id="element"><div id="ondragover0" ondrop="drop(event)" ondragover="allowDrop(event)"><div id="dragAmazon0" class="children container col-xs-12 col-sm-6 col-md-4 col-lg-4" draggable="true" ondragstart="drag(event)">
                            <div class="well" style="height: 500px; padding: 5%">
                                <div>
                                    <p style="font-size: 30px" class="text-right  col-xs-8 col-sm-8 col-md-8 col-lg-8">Amazon</p>
                                </div>
                                <div>
                <form action="test()">
                    <label><input class="form-control" type="text" placeholder="Saisissez votre recherche" name="amazonTitreRecherche0"></label>
                    <button class="btn btn-primary" type="button" onclick="sendData0()">Rechercher</button>
                <script>
                    function sendData0() {
                var valeur = $('input[name=amazonTitreRecherche0]').val();
                $.ajax({
                    url: "/requestAmazonSearch?data="+valeur,
                    contentType: "application/json", // Pour préciser le type de données qui transite
                    type: "GET",
                    success: function (result) {
                        $("#amazon0                        ").append("<div class='resulatsdeRecherche'>" + result + "</div>");
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
                                    <div style="width: 100%; height: 78%" class="text-center comment" id="amazon0"></div>
                <script>$(document).ready(function(){
                    setInterval(function(){
                        $("#amazon0").load(" #amazon0");
                        console.log("amazon !");
                    },60 * 60000);
                });</script></div></div></div><div id="ondragover1" ondrop="drop(event)" ondragover="allowDrop(event)"><div id="dragWeather1" class="children container col-xs-12 col-sm-6 col-md-4 col-lg-4" draggable="true" ondragstart="drag(event)">
                        <div class="well" style="height: 500px; padding: 5%">
                             <div>
                                <p style="font-size: 30px" class="text-right  col-xs-8 col-sm-8 col-md-8 col-lg-8">Météo</p>
                             </div>
                            <div style="width: 100%; height: 86%" class="text-center" id="weather1">
                <iframe src="https://www.meteoblue.com/fr/meteo/widget/daily/rennes_france_2983990?geoloc=fixed&amp;days=5&amp;tempunit=CELSIUS&amp;windunit=KILOMETER_PER_HOUR&amp;precipunit=MILLIMETER&amp;coloured=coloured&amp;pictoicon=1&amp;maxtemperature=1&amp;mintemperature=1&amp;windspeed=0&amp;windgust=0&amp;winddirection=0&amp;uv=0&amp;humidity=0&amp;precipitation=0&amp;precipitationprobability=0&amp;spot=0&amp;pressure=0&amp;layout=light" scrolling="NO" allowtransparency="true" sandbox="allow-same-origin allow-scripts allow-popups allow-popups-to-escape-sandbox" style="width: 100%; height: 100%; border: 0"></iframe>            </div>
                        </div>
                <script>$(document).ready(function(){
                    setInterval(function(){
                        $("#weather1").load(" #weather1");
                        console.log("weather!");
                    },60 * 60000);
                });</script>    </div></div>        </div>
                """;
    }

}
