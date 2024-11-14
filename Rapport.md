**Rapport du TP1 : Thread Java**

**Exercice I** 

Pour cet exercice un code nous a été fourni dans lequel il y avait :

- Une classe **UnMobile** qui étend JPanel et implémente Runnable. Elle représente un mobile qui se déplace horizontalement dans une fenêtre.
- Une Classe **UneFenetre** qui étend JFrame, crée une instance de UnMobile, et lance un thread pour gérer le mouvement.
- Une classe **TPMobile** qui instancie la classe UneFenetre. Il s’agit ici du Main

Le but de ce premier exercice était d’écrire le code afin que le mobile inverse sa direction une fois arrivé au bout de la fenêtre.

**Exercice II** 

Dans cet exercice, il fallait ajouter un bouton Start/Stop ayant pour but d’arrêter le mobile une fois que celui-ci était en marche ou de le refaire avancer s’il était en pause. 

Pour cela, il a fallu créer une variable booléenne isRunning instanciée a true et lorsque l’on clique sur le bouton cela appelle la méthodes suspend() ou resume() sur le thread afin de l’arreter si isRunning est à true ou de reprendre si il est à false.




**Exercice III** 

Pour finir, dans ce dernier exercice il fallait créer un deuxième mobile en séparant la fenêtre afin d’afficher les deux mobiles. Chacun de ses deux mobiles avait son propre thread ainsi qu’un bouton associé à celui-ci de sorte à ce que chaque bouton agisse sur un seul mobile qui fonctionnait juste pour lui.

