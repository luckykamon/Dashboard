## Projet Dashboard
### Liste des services et widgets
* Google
  * Calendrier _(Affiche un calendrier (personnalisé si on est connecté avec notre compte google))_
  * Drive _(Affiche un dossier de notre drive personnel)_
  * Playlist youtube _(Affiche la playlist des dernières vidéos sorties de la chaîne)_
  * Nombre de vues de la chaîne _(Affiche le nombre de vues de la chaîne)_
  * Nombre de vues de la vidéo _(Affiche le nombre de vues de la vidéo)_
  * Liste des commentaires _(Affiche les 20 premiers commentaires de la vidéo)_
    
* Amazon
  * Amazon _(Affiche une liste de résultats de recherche en fonction du mot clé renseigné)_
    
* Discord
  * Discord _(Affiche la liste des personnes connectées sur un serveur disord)_
    
* Météo
  * Météo _(Affiche la météo d'une ville avec la température et des pictogrammes)_
  
### Explication du fonctionnement du site

* Chaque widget a un timer propre et est personnalisable (par exemple avec le choix du serveur à afficher pour discord ainsi que le thème: dark
ou light ou encore avec le choix de la ville pour la météo ou de l'article recherché pour Amazon).

* Attributes est une classe qui comprends l'ensemble de toutes les variables utilisées dans le projet, 
ainsi que les getters et des méthodes "addNomAttribut" qui crééent des ArrayList pour chaque widget d'un utilisateur pour 
enregistrer les préférences en BDD.

* DashboardApplication est la classe "main" contenant toutes les routes (GetMapping, HttpServlet etc)
    * Les méthodes display(nomDuWidget) servent à afficher le widget sur le site.
    * Les méthodes _Accueil()_, _Connexion()_, _Perso()_, _Preferences()_, _Inscription()_, _login()_ et _logout()_
      servent à afficher les pages du sites et font appel aux méthodes citées ci-dessous.
 
* Les différentes parties d'une page html sont réparties dans les méthodes _head()_, _nav()_, _carousel()_, _footer()_
et _footer2()_, contenues dans la classe templateHTML

* Chaque service à un dossier spécifique qui ce trouve dans : _src.java.dashthyme.dashboard.services.nomDuService_

* La classe _MysqlCon_ contient toutes les méthodes en lien avec la Base de donnée (Insert, Update, Delete, etc)

* Les maquettes des pages html se trouvent dans le dossier _src.resources.templates_



___Les languages utilisés sont :___
* Le java
* Le html
* Le javascript

