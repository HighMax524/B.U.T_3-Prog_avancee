# Rapport TP4


## Table des matières
- [Introduction](#introduction)
- [I. Généralités](#i-généralités)
- [II. Algorithme](#ii-algorithme)
- [III. Parallélisation](#iii-parallélisation)
  - [Itération parallèle](#itération-parallèle)
  - [Master/Worker](#masterworker)
- [IV. Implémentation](#iv-implémentation)
- [V. Mesures de performances](#v-mesures-de-performances)

## Introduction

Dans ce TP, le but était d'implémenter un algorithme de Monte-Carlo afin de calculer la valeur de π. Pour ce faire, nous avons exploité le parallélisme en mémoire distribuée et en mémoire partagée.

Le répertoire `TP4_Monte_Carlo` contient les différents codes mis en place pour calculer π et effectuer des mesures de performances sur différents paradigmes.

###### Ce rapport a été écrit avec l'aide d'une IA, notamment pour la mise en page et la reformulation de certaines parties afin de faciliter la lecture et la compréhension du document.

## I. Généralités

### Qu'est-ce qu'un algorithme de Monte-Carlo ?

Un algorithme de Monte-Carlo est un algorithme basé sur de l'aléatoire et dont le temps d'exécution est déterministe. Il s'agit donc d'un algorithme utilisant une source aléatoire mais dont le temps d'exécution est connu à l'avance. L'intérêt de ce type d'algorithme est d'obtenir un résultat de façon rapide tout en ayant une probabilité d'échec faible.

La méthode de Monte-Carlo a été inventée en 1947 par [Nicholas Metropolis](https://fr.wikipedia.org/wiki/Nicholas_Metropolis) et faisait allusion au jeu de hasard pratiqué dans le casino de Monte-Carlo.

De nos jours, ces méthodes de Monte-Carlo sont souvent utilisées pour le calcul d'intégrales de dimension supérieure à 1 Mais égalemment pour introduire une approche statistique du risque dans une décision financière.

Elle peut être égalemment être utilisé pour plein d'autre méthode de calcul tel que le calcul de la valeur de coup dans certains jeu (Go, échec), mais aussi pour calculer la probabilité de la performance en bourse.

Dans ce rapport nous allons montré comment fonctionne cet algorithme a travers le calcul de la valeur π, comment peut-il être implementer en java et nous allons égalemment effectuer le calcul de ses perforamances.

## II. Algorithme

##### Nous allons a présent montrer l'appliction de l'algorithme de Monte-Carlo à travers le calcul de π.

Pour calculer π avec la méthode de Monte-Carlo, on commence par tracer un carré de côté 1. Puis, à l'interieur de ce carré, on trace maintenant un quart de cercle de rayon 1\
<img src="res/Schema1_MonteCarlo.png" alt="Schéma montrant un carré de côté 1 avec un quart de cercle de rayon 1 à l'intérieur" width="350"/>

On effectue ensuite le tirage d'un grand nombre de points aléatoires à l'intérieur de ce carré de côté 1.\
<img src="res/Schema1_MonteCarlo_avec_points.png" alt="Schéma montrant un carré de côté 1 avec un quart de cercle de rayon 1 à l'intérieur rempli de points" width="350"/>

Ainsi, la probabilité que le point soit à l'intérieur du quart de cercle est égale à :

$$ \frac{\text{Aire du quart de cercle}}{\text{Aire du carré}} = \frac{\frac{1}{4} \pi r^2}{r^2} $$

sachant que l'aire du quart de cercle est égale à $\frac{1}{4} \pi r^2$ et que l'aire du carré est égale à $r^2$ soit 1, on obtient ainsi que P(point dans le quart de cercle) qui est aussi P(distance du points <= 1) est égale à :

$$ \frac{\frac{1}{4} \pi r^2}{r^2} =  \frac{\pi}{4}$$

La probabilité P(X) doit être approché par le nombre de points dans le quart de cercle divisé par le nombre total de points ($\frac{\text{ncible}}{\text{ntotal}}$) avec ntotal un grand nombre pour plus d'exactitude.\
Ainsi, on peut approcher π par :\
P = $\frac{π}{4}$ ≃ $\frac{\text{ncible}}{\text{ntotal}}$ => π ≃ 4 * $\frac{\text{ncible}}{\text{ntotal}}$ 

#### Pseudo code de l'algorithme de base 
``` java
ncible = 0
for (p = 0; ntotal > 0; ntotal--){
    x_p, y_p = random.random(), random.random(); // Géneration d'un point aléatoire entre 0 et 1
    if (x_p**2 + y_p**2 <= 1){  // Si la distance du point est inférieur ou égale à 1 donc dans le quart de cercle
        ncible ++;
    }
}
pi = 4 * ncible / ntotal;

```

## III. Parallélisation

Nous allons a présent voir différent parallélisation de l'algorithme de Monte-Carlo pour le calcul de π.

### Itération parallèle

Tout d'abord on commence par choisir un modèle de parallélisation. Dans notre cas, nous avons choisi un modèle de parallélisme par tâches.\
Les différentes tâche sont les suivantes :
- Génération de points aléatoires
- Calcul de π

De plus, on peut découper La 1ère tâche en plusieurs sous-tâches pour plus de rapidité :
- Génération de points aléatoires
- Comptage des points dans le quart de cercle

On obtient ainsi une décomposition comme suit :
- Génération de points aléatoires
    - Génération de points aléatoires
    - Comptage des points dans le quart de cercle
- Calcul de π

On peut observer ici, une section critique qui est ncible. En effet, plusieurs tâches peuvent essayer d'incrémenter ncible en même temps. Il faut donc utiliser un verrou mutex (avec la méthode synchronized) pour éviter les problèmes d'&ccèes en simultané.

Suite a ces analyses, on on peut modifier le code de l'algorithme de Monte-Carlo précedent afin de le rendre parallèle.
``` java
ncible = 0
parallel for (p = 0; ntotal > 0; ntotal--){
    x_p, y_p = Math.random(), Math.random(); // Géneration d'un point aléatoire entre 0 et 1
    synchronized(ncible){
        if (x_p**2 + y_p**2 <= 1){  // Si la distance du point est inférieur ou égale à 1 donc dans le quart de cercle
            ncible ++;
        }
    }
}
```
Avec cette modification, on obtient quelque chose de moins efficace qu'une boucle for classique car 75% des Threads (correspondant a ceux des points dans le quart de cercle) vont être bloqués par le verrou mutex.\
Ainsi, si on veut améliorer cet algorithme, il faudrait compter les points ne tombant pas dans le quart de cercle (representant donc 25% des points) et les soustraire à ntotal.

Cependant, il est possible de faire mieux en utilisant un autre Paradigme de parallélisme.

### Master/Worker
Ce paradigme fonctionne de la manière suivante:\
On a deux types de composants :
- Un maître qui:
    - execute le code principal
    - Intialise les travailleurs
    - distribue les tâches aux travailleurs.
    - attend les résultats
  

- Des workers qui:
    - attendent les tâches
    - exécutent les tâches
    - renvoient les résultats au maître

Voici un schéma représentant le fonctionnemment de ce paradigme :\
<img src="res/Diagramme_MasterWorker.png" alt="Schéma représentant le fonctionnement du paradigme Master/Worker" width="500"/>


Cette architecture suit donc une approche de décomposition des tâches, dans laquelle un problème complexe est divisé en sous-tâches plus petites et plus gérables.

Il s'agit ainsi d'un paradigme de parallélisme de tâches simple à mettre en place avec une communication de 1 à tous. Mais qui présente néanmoins quelques inconvénients qui sont la distribution de 1 vers tous et la centralisation des résultats.

#### pseudo code de l'algorithme de Monte-Carlo en Master/Worker
``` java
ncible = 0
ncibleWorker = [0 * nbWorker]
npointWorker = ntotal / nbWorker
parallel for (worker = 0; worker < nbWorker; worker++){
    nciblesWorker[worker] = cibleWorker(npointWorker);
}
cible = sum(nciblesWorker);
pi = 4 * n_circle / ntotal;

function cibleWorker(npointWorker){
    ncibleWorker = 0
    for (p = 0; npointWorker > 0; npointWorker--){
        x_p, y_p = Math.random(), Math.random(); // Géneration d'un point aléatoire entre 0 et 1
        if (x_p**2 + y_p**2 <= 1){  // Si la distance du point est inférieur ou égale à 1 donc dans le quart de cercle
            ncibleWorker ++;
        }
    }
    return ncibleWorker;
}
```
En conclusion, l'algorithme Master/Worker permet de paralléliser efficacement le calcul de π en répartissant les tâches entre plusieurs travailleurs, ce qui améliore les performances globales tout en simplifiant la gestion des tâches et des résultats.

## IV. Implémentation

Maintenant, nous allons voir comment nous avons implémenté ces algorithmes en Java au cours de ce TP.

Pour commencer, nous avons récuperer 2 codes sources :
- **Assignement102.java** qui est le code source de base de l'algorithme de Monte-Carlo pour le calcul de π en itération parallèle
- **Pi.java** qui est le code source de base de l'algorithme de Monte-Carlo pour le calcul de π en Master/Worker

###### Assignement102.java à été écrit par [Karthik Jain](https://www.krthkj.com) et Pi.java à été écrit par le [Dr. Steve Kautz](https://faculty.sites.iastate.edu/smkautz/)
###### Ces codes sources ont été modifiés pour les besoins de ce TP

Pour **Assignement102**, nous avions :
- Une classe **PiMonteCarlo** qui est la classe principale et qui encapsule le calcul de π
- Une classe interne **MonteCarlo** qui implémente Runnable, elle permet de génerer un point aléatoire et vérfie si sa distance est inferieur à 1.
- Une classe **Assignement102** qui permet d'instancier PiMonteCarlo et affiche la valeur éstimée de π.
###### Ajouter diagramme UML

Ce code utilise interface Runnable pour gérer l'exécution parallèle des calculs.
Elle va créer un pool de threads avec un newWorkStealingPool afin d'optimiser l'utilisation des threads en les repartissant en fonction des coeurs disponibles sur la machine.

Chacune des tâches simule un unique lancé, ce qui géneres un grand nombre de threads légers.


Et pour **Pi**, nous avions :
- Une classe **Pi** qui execute le programme en appelant la méthode Master.doRun(50000,10) qui permet de lancer le calcul de π avec 50000 points et 10 travailleurs.
- Une classe **Master** qui instance un certains nombre (fourni par l'utilisateur) de Workers reparti le nombre de lancer entre les workers et qui attend les résultats pour calculer π. 
- Une classe **Worker** qui récupère le nombre de lancer, génere autant de point qu'il y a de lancer et renvoi le nombre de point dont la distance est inférieur à 1.
###### Ajouter diagramme UML

Ce code utilise les interfaces Callable et Future de la bibliothèque API concurrent afin de gérer l’exécution parallèle des tâches.
Elle utilise égalemment ExecutorService avec un FixedThreadPool pour gérer les threads efficacement.

### Qu'est-ce qu'un Future?
Un Future représente le résultat d'une opération effectuée de manière asynchrone.

Un Future permet de : 
- Récupérer le résultat d'une opération asynchrone : **get()**
- Vérifier si l'opération est terminée : **isDone()**
- D'annuler l'opération : **cancel()**

Pout implémenter l'interface Future, on peut utiliser un FutureTask afin de représenté une tâche qui peut être exécutée et récupérer son résultat.\
Un FutureTask peut être utilisé pour encapsuler une Callable ou une Runnable et est éxecuté par un thread ou un ExecutorService afin de stocké le resultat d'un calcul asynchrone.

Les Callable et Runnable sont des interfaces qui permettent d'éxecuté des tâches. La différence entre les deux est que Callable renvoi un résultat tandis que Runnable ne renvoi rien.

### Mais qu'est-ce qu'un Executor ?
Un Executor est un service de support pour les threads mais à un niveau plus élevé que la classe Thread. Ce service permet d'éxecuter des tâches de manière asynchrone en utilisant un pool de threads.

Utiliser un ExecutorService permet de :
- Réutiliser les threads
- Gérer automatiquement le nombre optimal de threads avec la méthode **fixedThreadPool()**.
- Facilité l'annulation des tâches avec l'aide d'un **Future**
- Améliorer les performances.

Pour ces 2 codes, on utilise égalemment la méthode System.currentTimeMillis() pour calculer le temps d'exécution que met le programme à calculer π. Cela nous permettra par la suite d'effectuer des mesures de performances afin de comparer lequel de ces 2 programmes est le plus efficace.

## V. Mesures de performances

Nous allons désormais analyser les performances de ces deux programmes afin d'observer lequel de ces deux paradigme est le plus efficace pour le calcul de π.

Pour cela, nous avons effectué des mesures de performances sur la machine ayant la configuration suivante : 
- <u>**Processeur :**</u> Intel Core i7-9700 CPU 3GHz
- <u>**Nombre de coeurs :**</u> 8
- <u>**Nombre de threads:**</u> 8
- <u>**Mémoire RAM :**</u> 32Go
- <u>**Cache :**</u> 12 Mo
- <u>**Vitesse du bus:**</u> 8GT/s (GigaTransfert)
- <u>**PDT (Puissance de dissipation thermique) :**</u> 65W

<u style="color:red">**ATTENTION :**</u> Les résultats présentés dans ce rapport seront différent en fonction de l'architecture de la machine utilisée.

### Comment mesurer les performences ?
Pour pouvoir mesurer les performances, le code contient, comme présenter précédemment, un appel à la méthode System.currentTimeMillis() avant et après l'exécution de la portion de code permettant de paralléliser le calcul de π\
De plus, nous avons modifié les codes afin que les deux programmes effectuent le calcul de π avec un nombre de points définis par l'utilisateur mais égalemment que les deux codes renvoient les résultats de la même manière. 

Ensuite, pour chacun de ces codes, nous avons fait en sorte d'écrire les résultats dans un fichier texte **result_pi.txt**.

Pour avoir des résultats plus précis, nous avons, pour chaque processus, éxecuter 10 fois le calcul de π afin de faire une moyenne du temps d'éxecution.

Une fois ces calculs effectués, nous pouvons effectuer des tests de scalabilité.

### Qu'est ce que la scalabilité ?
La scalabilité correspond à la capacité d'un système à s'adapter à une augmentation de la charge de travail.\

Pour évaluer cette scalabilité, il faut mesurer l'accelaration (ou speedup).

Voici le calcul du speedup :

$$Speedup = \frac{\text{Temps d'éxecution pour 1 processus}}{\text{Temps d'éxecution pour n processus}}$$

Le speedup idéal est linéaire, c'est à dire que si on double le nombre de processus, le temps d'éxecution doit 2 fois plus rapide.

<img src="res/Schema_spedup.png" alt="Schéma représentant le speedup" width="428"/>

On observe ainsi que l'objectif est d'atteindre un speedup qui se rapproche le plus possible de la droite idéale SP=P. Néanmoins il sera difficle de l'égalé étant donné qu'un écart va se créer au fur et à mesure dû à la communication entre les processus.

Il éxiste plusieurs types de scalabilité :


### Scalabilité forte
La scalabilité forte mesure la capacité d'un programme à s'adapter à une augmentation du nombre de processus en gardant la taille du problème constante.

Dans notre cas, cela revient à augmenter le nombre de threads/workers tout en gardant le même nombre de points à calculer pour chaque thread/worker.\
Cela signifie que pour 100000 points lancés au départ, chaque thread/worker devra calculer 100000/nbThread points. Ainsi avec 2 workers chacun devra calculer 50000 points, pour 4 workers chacun devra calculer 25000 points, etc.

Ainsi, en scalabilité forte, nous nous attendons à obtenir un résultat similaire au schéma précédent.

### Scalabilité faible
La scalibilté faible quant à elle mesure la capacité d'un programme à s'adapter à une augmentation du nombre de processus en augmentant la taille du problème.

Dans notre cas, cela revient à augmenter le nombre de threads/workers tout en augmentant le nombre de points à calculer pour chaque thread/worker.\
Cela signifie que pour 100000 points lancés au départ, chaque thread/worker devra calculer 100000. Ainsi avec 2 workers chacun devra calculer 100000 points, pareil pour 4 workers, etc.\
Ainsi le nombre de points à calculer au total sera de 100000 * nbThread.

Avec la scalabilité faible, nous nous attendons à obtenir un résultat similaire au schéma suivant :\
<img src="res/Schema_weak_scal.png" alt="Schéma représentant le speedup en scalabilité faible" width="428"/>

### Analyse des résultats
Nous allons a présent analyser les différents résultats obtenus pour les deux programmes.

### Résultats pour Pi
Pour le programme Pi, nous avons décidé de lancer le calcul de π avec 10 000 000 points et 10 travailleurs.

#### En scalabilité forte
Nous avons obtenu les résultats suivants :

| Nombre de Workers | Temps moyen (ms) | Speedup |
|-------------------|------------------|---------|
| 1                 |                  | 1.0     |
| 2                 |                  |         |
| 3                 |                  |         |
| 4                 |                  |         |
| 5                 |                  |         |
| 6                 |                  |         |
| 7                 |                  |         |
| 8                 |                  |         |
| 9                 |                  |         |
| 10                |                  |         |

En calculant le speedup, nous obtenons la courbe suivante :

#### En scalabilité faible
Nous avons obtenu les résultats suivants :

| Nombre de Workers | Temps moyen (ms) | Speedup |
|-------------------|------------------|---------|
| 1                 |                  | 1.0     |
| 2                 |                  |         |
| 3                 |                  |         |
| 4                 |                  |         |
| 5                 |                  |         |
| 6                 |                  |         |
| 7                 |                  |         |
| 8                 |                  |         |
| 9                 |                  |         |
| 10                |                  |         |

En calculant le speedup, nous obtenons la courbe suivante :

