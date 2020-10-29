/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import message.Message;
import user.User;

/**
 * Handles messages from the server.
 * @author Martin Angulo
 */
public class Signer implements Signable{
    /** Port to connect to the server. */
    private static final int PORT = 5005;
    /** Client socket that connects to the servers socket. */
    private Socket clientSocket = null;
    /** Input stream to receive objects from the server. */
    private ObjectInputStream serverInput = null;
    /** Output stream to send objects to the server. */
    private ObjectOutputStream clientOutput = null;
    
    /**
     * Signer constructor, connects to the server and initializes IO.
     */
    public Signer() {
        try {
            clientSocket = new Socket(InetAddress.getLocalHost(), PORT);
            serverInput = new ObjectInputStream(clientSocket.getInputStream());
            clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (UnknownHostException ex) {
            System.out.println("UnknownHostException: " + ex.getMessage());
        } catch (IOException ie) {
            System.out.println("IOException: " + ie.getMessage());
        }
    }

    /**
     * Helper method to send messages to the server and receive responses.
     * @param message Message to send. 
     * @return Server response.
     */
    private Message sendMessage(Message message) {
        Message serverResponse = null;
        try {
            //Send message to server
            clientOutput.writeObject(message);
            clientOutput.flush();
            //Receive response
            serverResponse = (Message)serverInput.readObject();
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException: " + ex.getMessage());
        }
        return serverResponse;
    }
    
    /**
     * Helper method to disconnect the IO and Socket from the server.
     */
    private void Disconnet() {
        try {
            if(clientOutput != null)
                clientOutput.close();
            if(serverInput != null)
                serverInput.close();
            if (clientSocket != null)
                clientSocket.close();
        } catch(IOException ie) {
            System.out.println("Socket Close Error: " + ie.getMessage());
        }
    }
    
    /**
     * Sign up function implementation.
     * @param user User to sign up.
     * @return The User in case the application needs it.
     */
    @Override
    public User signUp(User user) {
        Message serverResponse = sendMessage(new Message(Message.Type.SIGN_UP, user));
        switch(serverResponse.getType()) {
        case SIGN_UP:
            User signUpUser = (User)serverResponse.getData();
            signUpUser.printData();
            break;
        default:
            break;
        }
        return null;
    }

    /**
     * Sign in function implementation.
     * @param user User to sign in.
     * @return The User in case the application needs it.
     */
    @Override
    public User signIn(User user) {
        Message serverResponse = sendMessage(new Message(Message.Type.SIGN_IN, user));
        switch(serverResponse.getType()) {
        case SIGN_IN:
            User logInUser = (User)serverResponse.getData();
            System.out.println("Login: " + logInUser.getLogin());
            System.out.println("Password: " + logInUser.getPassword());
            break;
        default:
            break;
        }
        return null;
    }
}
