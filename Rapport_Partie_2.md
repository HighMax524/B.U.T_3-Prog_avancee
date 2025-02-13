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

Dans ce rapport nous allons montré comment fonctionne cet algorithme a travers le calcul de la valeur π.