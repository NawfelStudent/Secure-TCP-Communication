package server;

import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class ServerMT  {
    public static void main(String[] args) {
        int port = 3000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur en écoute sur le port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion acceptée de " + clientSocket.getInetAddress());

                // Créer un nouveau thread pour gérer le client
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur du serveur: " + e.getMessage());
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private static KeyPair rsaKeyPair;

    static {
        try {
            // Générer une paire de clés RSA
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            rsaKeyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors de la génération des clés RSA", e);
        }
    }

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            // Envoyer la clé publique RSA au client
            output.writeObject(rsaKeyPair.getPublic());

            // Recevoir la clé AES chiffrée
            byte[] encryptedAesKey = (byte[]) input.readObject();

            // Déchiffrer la clé AES avec la clé privée RSA
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPrivate());
            byte[] aesKeyBytes = rsaCipher.doFinal(encryptedAesKey);
            SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");

            System.out.println("Clé AES reçue et déchiffrée." + aesKey);

            // Lire le message chiffré du client
            byte[] encryptedMessage = (byte[]) input.readObject();

            // Déchiffrer le message avec AES
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decryptedMessageBytes = aesCipher.doFinal(encryptedMessage);
            String decryptedMessage = new String(decryptedMessageBytes);
            System.out.println("Message reçu et déchiffré: " + decryptedMessage);

            // Répondre au client avec un message chiffré
            String response = "Message reçu: " + decryptedMessage;
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encryptedResponse = aesCipher.doFinal(response.getBytes());
            output.writeObject(encryptedResponse);

        } catch (IOException | ClassNotFoundException | GeneralSecurityException e) {
            System.err.println("Erreur avec un client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
}
