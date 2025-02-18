## Rapport TP4

### Introduction

Dans ce TP, le but était d'implémenter un algorithme de Monte-Carlo afin de calculer la valeur de π. Pour ce faire, nous avons exploité le parallélisme en mémoire distribuée et en mémoire partagée.

Le répertoire `TP4_Monte_Carlo` contient les différents codes mis en place pour calculer π et effectuer des mesures de performances sur différents paradigmes.

###### Ce rapport a été écrit avec l'aide d'une IA, notamment pour la mise en page et la reformulation de certaines parties afin de faciliter la lecture et la compréhension du document.

### I. Généralités

#### Qu'est-ce qu'un algorithme de Monte-Carlo ?

Un algorithme de Monte-Carlo est un algorithme basé sur de l'aléatoire et dont le temps d'exécution est déterministe. Il s'agit donc d'un algorithme utilisant une source aléatoire mais dont le temps d'exécution est connu à l'avance. L'intérêt de ce type d'algorithme est d'obtenir un résultat de façon rapide tout en ayant une probabilité d'échec faible.

La méthode de Monte-Carlo a été inventée en 1947 par [Nicholas Metropolis](https://fr.wikipedia.org/wiki/Nicholas_Metropolis) et faisait allusion au jeu de hasard pratiqué dans le casino de Monte-Carlo.

De nos jours, ces méthodes de Monte-Carlo sont souvent utilisées pour le calcul d'intégrales de dimension supérieure à 1 Mais égalemment pour introduire une approche statistique du risque dans une décision financière.

Elle peut être égalemment être utilisé pour plein d'autre méthode de calcul tel que le calcul de la valeur de coup dans certains jeu (Go, échec), mais aussi pour calculer la probabilité de la performance en bourse.

Dans ce rapport nous allons montré comment fonctionne cet algorithme a travers le calcul de la valeur π, comment peut-il être implementer en java et nous allons égalemment effectuer le calcul de ses perforamances.

### II. Algorithme

##### Nous allons a présent montrer l'appliction de l'algorithme de Monte-Carlo à travers le calcul de π.

Pour calculer π avec la méthode de Monte-Carlo, on commence par tracer un carré de côté 1. Puis, à l'interieur de ce carré, on trace maintenant un quart de cercle de rayon 1\
<img src="res/Schema1_MonteCarlo.png" alt="Schéma montrant un carré de côté 1 avec un quart de cercle de rayon 1 à l'intérieur" width="350"/>

On effectue ensuite le tirage d'un grand nombre de points aléatoires à l'intérieur de ce carré de côté 1.\
<img src="res/Schema1_MonteCarlo_avec_points.png" alt="Schéma montrant un carré de côté 1 avec un quart de cercle de rayon 1 à l'intérieur rempli de points" width="350"/>

Ainsi, la probabilité que le point soit à l'intérieur du quart de cercle est égale à l'aire du quart de cercle divisée par l'aire du carré est donnée par :

$$ \frac{\text{Aire du quart de cercle}}{\text{Aire du carré}} = \frac{\frac{1}{4} \pi r^2}{r^2} $$

sachant que l'aire du quart de cercle est égale à $\frac{1}{4} \pi r^2$ et que l'aire du carré est égale à 1, on obtient ainsi que P(point dans le quart de cercle) qui est aussi P(distance du points <= 1) est égale à :

$$ \frac{\frac{1}{4} \pi r^2}{r^2} =  \frac{\pi}{4}$$

La probabilité P(X) doit être approché par le nombre de points dans le quart de cercle divisé par le nombre total de points ($\frac{\text{ncible}}{\text{ntotal}}$) avec ntotal un grand nombre pour plus d'exactitude.\
Ainsi, on peut approcher π par :\
P = $\frac{π}{4}$ ≃ $\frac{\text{ncible}}{\text{ntotal}}$ => π ≃ 4 * $\frac{\text{ncible}}{\text{ntotal}}$ 
