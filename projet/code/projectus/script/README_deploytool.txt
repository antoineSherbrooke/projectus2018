SCRIPT  projectus.sh
est un script qui doit se trouver au répertoire où le dépoyment sera fait.
Normalement il est envoyé à la place nécessaire par le script deploytool.sh



SCRIPT deploytool.sh
to run : ./deploytool.sh

Pour executer ce fichier il faut avoir un linux ou un mac os, il faut ensuite ce déplacer dans le dossier du projet,
puis dans le dossier script. Alors on peut lancer la commande ./deploytool.sh <un paramètre> <un autre paramètre>.

Le premier paramètre peut prendre plusieurs valeurs:
    - start : permet de démarrer le deamon sur l'ordinateur
    - stop : permet de stopper de deamon sur l'ordinateur
    - restart : permet de stopper puis de démarrer le deamon.
    - append : ajoute une nouvelle version de projectus sur le serveur en prenant comme nom release-<date du jour>, il fait stop, append et start.
    - choose : permet de choisir la release courrante
    - status : permet de verifier le status de projectus (stoppé ou démarré)

Le deuxième paramètres peut prendre trois valeurs :

    - par defaut si rien est indiqué tout va comme si stag était indiqué
    - prod pour indiquer que l'on effectue le script sur le server exitlabserver
    - staging pour indiquer que l'on effectue le script sur tictac
    - ou n'importe quel autres serveurs ( <user>@<domain> )

Si le serveur ne peut pas démarrer connectez-vous en SSH. Allez dans le dossier de projectus et lancer le script ./projectus.sh
vous verrez peut être d'ou provient d'erreur.
En production ou même sur stagging "evolution", module play permettant de changer la base de données, est désactivé si vous voulez
le reactiver il faut aller dans le fichier conf/application.conf et changer ces valeurs :

        db.default.enabled = true
        autoApply = true
        autoApplyDowns = true


FICHIER projectus.plist
est à installer dans /Library/LaunchDemons
Il sert à faire partir projectus après un reset de la machiine.