package client;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Base64;
import javax.crypto.*;

public class ClientTCP {
    public static void main(String[] args) {

        String serverAddress ="127.0.0.1";
        int serverPort =3000;

        try(
            Socket socket = new Socket(serverAddress, serverPort);
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in)) )
        {

            System.out.println("Connecté au serveur " + serverAddress + ":" + serverPort);

            // Recevoir la clé publique RSA du serveur
            PublicKey serverPublicKey = (PublicKey) input.readObject();

            // Générer une clé AES
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey aesKey = keyGen.generateKey();

            System.out.println("Clé AES générée: " + Base64.getEncoder().encodeToString(aesKey.getEncoded()));

            // Chiffrer la clé AES avec la clé publique RSA
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
            byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());

            // Envoyer la clé AES chiffrée au serveur
            output.writeObject(encryptedAesKey);

            // Lire un message depuis la console
            System.out.print("Entrez un message: ");
            String message = consoleInput.readLine();

            // Chiffrer le message avec AES
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encryptedMessage = aesCipher.doFinal(message.getBytes());

            // Envoyer le message chiffré au serveur
            output.writeObject(encryptedMessage);

            // Recevoir la réponse chiffrée du serveur
            byte[] encryptedResponse = (byte[]) input.readObject();

            // Déchiffrer la réponse avec AES
            aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decryptedResponseBytes = aesCipher.doFinal(encryptedResponse);
            String decryptedResponse = new String(decryptedResponseBytes);

            System.out.println("Réponse du serveur: " + decryptedResponse);

        } catch (IOException | ClassNotFoundException | GeneralSecurityException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }
}
