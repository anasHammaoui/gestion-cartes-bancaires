# Gestion des Cartes Bancaires et Détection de Fraude

## Présentation
Ce projet a pour objectif la gestion des cartes bancaires (débit, crédit, prépayée) et la détection des fraudes associées. Chaque client peut posséder une ou plusieurs cartes, et ces cartes génèrent différentes transactions (paiement, retrait, achat en ligne). Le système permet :

- La gestion du cycle de vie des cartes (création, activation, suspension, renouvellement)
- Le suivi des opérations en temps réel
- La détection des activités suspectes et génération d’alertes de fraude

## Technologies utilisées
- **Langage de programmation :** Java
- **Architecture :** Orientée objet
- **Base de données :** MySQL
- **Outils :** Git, IDE IntelliJ

## Structure du projet
src/
├── entity/    
├── service/
├── dao/      
├── ui/
└── utils/  