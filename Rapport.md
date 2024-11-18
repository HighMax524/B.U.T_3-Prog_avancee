## Rapport du TP1 : Thread Java

**Exercice I** 

Pour cet exercice un code nous a été fourni dans lequel il y avait :

- Une classe **UnMobile** qui étend JPanel et implémente Runnable. Elle représente un mobile qui se déplace horizontalement dans une fenêtre.
- Une Classe **UneFenetre** qui étend JFrame, crée une instance de UnMobile, et lance un thread pour gérer le mouvement.
- Une classe **TPMobile** qui instancie la classe UneFenetre. Il s’agit ici du Main.

![Diagramme UML du TPMobile](Diagramme_TP_Mobile.png)

Le but de ce premier exercice était d’écrire le code afin que le mobile inverse sa direction une fois arrivé au bout de la fenêtre.

**Exercice II** 

Dans cet exercice, il fallait ajouter un bouton Start/Stop ayant pour but d’arrêter le mobile une fois que celui-ci était en marche ou de le refaire avancer s’il était en pause. 

Pour cela, il a fallu créer une variable booléenne isRunning instanciée a true et lorsque l’on clique sur le bouton cela appelle la méthodes suspend() ou resume() sur le thread afin de l’arreter si isRunning est à true ou de reprendre si il est à false.




**Exercice III** 

Pour finir, dans ce dernier exercice il fallait créer un deuxième mobile en séparant la fenêtre afin d’afficher les deux mobiles. Chacun de ses deux mobiles avait son propre thread ainsi qu’un bouton associé à celui-ci de sorte à ce que chaque bouton agisse sur un seul mobile qui fonctionnait juste pour lui.

## Rapport du TP2 : Affichage

**Exercice I** 
Pour cet exercice nous avions deux tâches TA et TB. Chacune de ces tâches avait un rôle précis. La première tâche TA affichait le message « AAA » tandis que le message de la tâche TB est « BB ». Le but était d’afficher le message de l’une des tâches avant celui de l’autre. Ainsi le message devait être soit AAABB soit BBAAA.
Pour cela il a fallu créer :
-	La classe **Main** qui permet de lancer le programme et d’instancier et de démarrer les différentes tâches.
-	Une classe **Affichage** qui étend Thread. Cette classe a pour but d’afficher les différents messages des tâches grâce à une boucle contenant la méthode System.out.print()

Pour faire en sorte que la boucle affiche les messages dan le bonne ordre (« AAABB » ou «BBAAA » et non « AABAB » ou « BAAAB » ou autre) il a fallu utiliser la méthode synchronized.
Cette méthode permet de s’assurer que la section critique est utilisée seulement par un seul thread a chaque fois afin que chaque thread affiche son message en entier avant qu’un autre affiche son message également. 
Pour faire cela il y a 2 méthodes possible : 
-	Celle où on ne connaît pas la ressource critique. Pour cela on crée une fausse classe Exclusion puis on synchronized sur cette classe
![Image du code sans connaissance de la ressource critique](image_synchronized.png)

-	Celle où on connaît la ressource critique comme dans l’exercice où il s’agit de « out ». Dans ce cas il faut faire ce qui est dans le code de Affichage.java
![Image du code avec connaissance de la ressource critique](image_synchronized_out.png)
  
**Exercice II**

Dans cet exercice, il fallait reprendre l’exercice 1 mais en utilisant cette fois-ci la classe semaphoreBinaire.
Cette classe fonctionne de la manière suivante. Tout d’abord, elle étend la classe semaphore.
Ensuite, elle implémente un sémaphore binaire, où la valeur est soit 0, ce qui signifie que la ressource critique est utilisé/occupé soit 1 ce qui signifie qu’elle est libre. 
Cela permet de contrôler l’accès à la ressource critique par les threads.
Ensuite, il faut mmodifier la classe Affichage. Pour cela, on utilise la méthode syncWait() avant la section critique afin de prévenir les threads qu’un autre thread utilise cette section. Une fois fini il y a un appelle de la méthode syncSignal() qui signale que la ressource a été liberée par un Thread et qu’elle est maintenant libre.

## Rapport du TP3 : Bal

**Exercice I** 
Cet exercice consiste a réaliser le code simulant la gestion d'une boîte aux lettres. Pour cela on a une tâche ui est le producteur celui-ci créé une lettre et la dépose dans la boîte aux lettres (Bal). En parallèle un consommateur va lire/retirer la lettre une fois que celle-ci se trouve dans la Bal. De plus, le consommateur peut déposer une lettre seulement si la Bal est vide, tandis que le consomateur ne peut lire de lettre seulement si il y en a une dans la Bal.

Pour cela il a fallu créer :
-	La classe **Main** qui permet de lancer le programme et d’instancier et de démarrer les différentes tâches.
-	Une classe **Bal** qui représente la boîte aux lettres.
-	Une classe **Producteur** qui étend **Thread**. Cette classe représente donc le producteur qui peut déposer une lettre.
-	Une classe **Consommateur** qui étend **Thread**. Cette classe représente donc le consommateur qui peut retirer une lettre.

I1) 
Pour faire en sorte que le consommateur ne puisse deposer une lettre seulement lorsque la Bal est vide et que le consommateur, lui, puissent lire une lettre seulement si il y en a une il a fallu mettre un verrou mutex sur la ressource critique qui est la Bal (dans le code il s'agit de **this**). Pour cela, il faut utiliser la méthode synchronized sur this est cette méthode entoure la section critique qui correspond à l'ensemble de la méthode deposer() et l'ensemble de la metode retirer().
Cette méthode permet de s’assurer que la section critique (la Bal) est utilisée seulement par un seul thread a chaque fois afin qu'elle ne contiennent qu'une seule lettre a chaque fois et que le consommateur puisse retirer cette lettre seulement lorsqu'elle est présente dans la Bal.

I2)
Pour cette deuxième partie de l'exercice il fallait modifier le code afin que pour la tache déposer, le producteur écrive lui-même une lettre au clavier et que les deux tâches deposer et retirer s'arrêtent lorsque le producteur écrit une lettre ne contenant que le mot "Q".
Pour cela il a fallut importer la classe **Scaner**. Cette classe permet ainsi au producteur d'écrire lui-même une lettre au clavier. Ensuite il faut faire une condition pour laquelle on compare la lettre avec le terme permettant d'arreter les tâche ("Q"). Et on arrête la tâche si c'est le cas.




