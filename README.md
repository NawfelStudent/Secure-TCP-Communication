# Secure TCP Communication

## Description
This project demonstrates secure communication between a client and a server using the TCP protocol. The security is ensured through the use of RSA for key exchange and AES for encrypting messages. The server is multithreaded, allowing it to handle multiple client connections simultaneously. This project serves as a practical example of integrating cryptography into network applications.

## Features
- **RSA Key Exchange**: Securely exchanges the AES session key between the client and server.
- **AES Encryption**: Encrypts and decrypts messages exchanged between the client and server.
- **Multithreaded Server**: Handles multiple client connections concurrently.

## Requirements
- Java SE 8 or higher

## How to Run
1. **Compile the Code**:
   ```bash
   javac -d . server/ServerMT.java client/ClientTCP.java
   ```

2. **Start the Server**:
   ```bash
   java server.ServerMT
   ```

3. **Start the Client**:
   ```bash
   java client.ClientTCP
   ```

4. **Test the Communication**:
   - Enter a message in the client terminal.
   - The server will decrypt the message, process it, and send an encrypted response back to the client.

## Project Structure
```
Secure-TCP-Communication/
├── client/
│   └── ClientTCP.java
├── server/
│   └── ServerMT.java
└── README.md
```

## License
This project is licensed under the MIT License.