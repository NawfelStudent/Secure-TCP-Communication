# Communication TCP Sécurisée

## Description
Ce projet démontre une communication sécurisée entre un client et un serveur en utilisant le protocole TCP. La sécurité est assurée grâce à l'utilisation de RSA pour l'échange de clés et d'AES pour le chiffrement des messages. Le serveur est multithreadé, ce qui lui permet de gérer plusieurs connexions clients simultanément. Ce projet est un exemple pratique d'intégration de la cryptographie dans les applications réseau.

## Fonctionnalités
- **Échange de clés RSA** : Permet d'échanger de manière sécurisée la clé de session AES entre le client et le serveur.
- **Chiffrement AES** : Chiffre et déchiffre les messages échangés entre le client et le serveur.
- **Serveur multithreadé** : Gère plusieurs connexions clients en parallèle.

## Prérequis
- Java SE 8 ou supérieur

## Instructions d'exécution
1. **Compiler le code** :
   ```bash
   javac -d . server/ServerMT.java client/ClientTCP.java
   ```

2. **Démarrer le serveur** :
   ```bash
   java server.ServerMT
   ```

3. **Démarrer le client** :
   ```bash
   java client.ClientTCP
   ```

4. **Tester la communication** :
   - Entrez un message dans le terminal du client.
   - Le serveur déchiffrera le message, le traitera et renverra une réponse chiffrée au client.

## Structure du projet
```
Secure-TCP-Communication/
├── client/
│   └── ClientTCP.java
├── server/
│   └── ServerMT.java
└── README.md
```

## Licence
Ce projet est sous licence MIT.