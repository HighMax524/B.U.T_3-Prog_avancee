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


## V. Mesures de performances