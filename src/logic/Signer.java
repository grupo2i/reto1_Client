package logic;

import exceptions.EmailAlreadyExistsException;
import exceptions.PasswordDoesNotMatchException;
import exceptions.UnexpectedErrorException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import user.User;

/**
 * Handles messages from the server.
 * @author Martin Angulo
 */
public class Signer implements Signable{
    /** Client socket that connects to the servers socket. */
    private Socket clientSocket = null;
    /** Input stream to receive objects from the server. */
    private ObjectInputStream serverInput = null;
    /** Output stream to send objects to the server. */
    private ObjectOutputStream clientOutput = null;
    
    /**
     * Signer constructor, connects to the server and initializes IO.
     * @throws exceptions.UnexpectedErrorException
     */
    public Signer() throws UnexpectedErrorException {
        try {
            //Getting the client-server communication properties.
            ResourceBundle configFile = ResourceBundle.getBundle("configuration.config");
            Integer port = Integer.valueOf(configFile.getString("Port"));
            String serverHost = configFile.getString("ServerHost");
            //Initializing the client-server communication.
            clientSocket = new Socket(serverHost, port);
            serverInput = new ObjectInputStream(clientSocket.getInputStream());
            clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            Logger.getLogger(Signer.class.getName()).log(Level.INFO, "Client signer started successfully.");
        } catch (IOException ex) {
            throw new UnexpectedErrorException(ex.getMessage());
        }
    }

    /**
     * Helper method to send messages to the server and receive responses.
     * @param message Message to send. 
     * @return Server response.
     */
    private Message sendMessage(Message message) throws UnexpectedErrorException {
        Message serverResponse = null;
        try {
            //Send message to server
            clientOutput.writeObject(message);
            clientOutput.flush();
            Logger.getLogger(Signer.class.getName()).log(Level.INFO, "Message sent to the server.");
            //Receive response
            serverResponse = (Message)serverInput.readObject();
            Logger.getLogger(Signer.class.getName()).log(Level.INFO, "Server response recieved.");
        } catch (ClassNotFoundException | IOException ex) {
            throw new UnexpectedErrorException(ex.getMessage());
        }finally{
            disconnect();
        }
        return serverResponse;
    }
    
    /**
     * Helper method to disconnect the IO and Socket from the server.
     */
    private void disconnect() {
        try {
            if(clientOutput != null)
                clientOutput.close();
            if(serverInput != null)
                serverInput.close();
            if (clientSocket != null)
                clientSocket.close();
            Logger.getLogger(Signer.class.getName()).log(Level.INFO, "Client signer disconnected.");
        } catch(IOException ie) {
            System.out.println("Socket Close Error: " + ie.getMessage());
        }
    }
    
    /**
     * Sign up function implementation.
     * @param user User to sign up.
     * @return The User in case the application needs it.
     * @throws UserAlreadyExistsException If the username is found in the DB.
     * @throws EmailAlreadyExistsException If the email is found in the DB.
     * @throws exceptions.UnexpectedErrorException If anything else goes wrong.
     */
    @Override
    public synchronized User signUp(User user) throws UserAlreadyExistsException, EmailAlreadyExistsException, UnexpectedErrorException {
        Message serverResponse = sendMessage(new Message(Message.Type.SIGN_UP, user));
        switch(serverResponse.getType()) {
        case SIGN_UP:
            User signUpUser = (User)serverResponse.getData();
            return signUpUser;
        case USER_ALREADY_EXISTS:
            throw new UserAlreadyExistsException(user.getLogin());
        case EMAIL_ALREADY_EXISTS:
            throw new EmailAlreadyExistsException(user.getEmail());
        default:
            throw new UnexpectedErrorException("No response recieved from the server.");
        }
    }

    /**
     * Sign in function implementation.
     * @param user User to sign in.
     * @return The User in case the application needs it.
     * @throws exceptions.UserNotFoundException If the username is not found in the DB.
     * @throws exceptions.PasswordDoesNotMatchException If the password does not 
     * match with the username in the DB.
     * @throws exceptions.UnexpectedErrorException If anything else goes wrong.
     */
    @Override
    public synchronized User signIn(User user) throws UserNotFoundException, PasswordDoesNotMatchException, UnexpectedErrorException{
        Message serverResponse = sendMessage(new Message(Message.Type.SIGN_IN, user));
        switch(serverResponse.getType()) {
        case SIGN_IN:
            User logInUser = (User)serverResponse.getData();
            return logInUser;
        case USER_NOT_FOUND:
            throw new UserNotFoundException(user.getLogin());
        case PASSWORD_DOES_NOT_MATCH:
            throw new PasswordDoesNotMatchException();
        default:
            throw new UnexpectedErrorException("No response recieved from the server.");
        }
    }
}