# Sae traitement d'images

## 1. notre solution personelle pour le traitement d'images

#### 1.1 fonctionnement de notre solution

Notre solution, est une solution qui ne nécessite pas de longs temps de traitements, elle ne parcourt l'image qu'une seule fois,
se contente de recenser les pixels ayant des couleurs proches les unes des autres et de toutes les regrouper dans un même groupe.
Le nombre de groupes créés dépends du nombre de couleurs présentes dans l'image ainsi que d'une variable "tolérance" qui permet de
choisir la distance maximale entre deux couleurs pour qu'elles soient regroupées dans un même groupe. Plus la tolérance est grande,
plus le nombre de groupes sera petit, permettant un traitement plus rapide, mais une image moins fidèle à l'originale, parfait pour 
les nombres de couleurs faibles. A l'inverse, plus la tolérance est faible, plus le nombre de groupes sera grand, permettant un
traitement plus long, mais une image plus fidèle à l'originale, parfait pour les nombres de couleurs élevés.

#### 1.2 mise en place de notre solution

Notre solution se présente sous la forme d'une classe "MainQ1Sae" qui contient une méthode "main" qui permet de lancer le traitement.
Elle prend comme arguments le chemin de l'image à traiter, le chemin de l'image de sortie, le nombre de couleurs, et la tolérance.
