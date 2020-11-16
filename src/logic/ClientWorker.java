package logic;

import exceptions.UnexpectedErrorException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

/**
 * Handles messages from the server.
 * @author Martin Angulo, Aitor Fidalgo
 */
public class ClientWorker extends Thread {
    /** Client socket that connects to the servers socket. */
    private Socket clientSocket = null;
    /** Input stream to receive objects from the server. */
    private ObjectInputStream serverInput = null;
    /** Output stream to send objects to the server. */
    private ObjectOutputStream clientOutput = null;
    
    Message message = null;
    
    /**
     * Signer constructor, connects to the server and initializes IO.
     * @param message
     * @throws exceptions.UnexpectedErrorException
     */
    public ClientWorker(Message message) throws UnexpectedErrorException {
        try {
            this.message = message;
            //Getting the client-server communication properties.
            ResourceBundle configFile = ResourceBundle.getBundle("configuration.config");
            Integer port = Integer.valueOf(configFile.getString("Port"));
            String serverHost = configFile.getString("ServerHost");
            //Initializing the client-server communication.
            clientSocket = new Socket(serverHost, port);
            serverInput = new ObjectInputStream(clientSocket.getInputStream());
            clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            Logger.getLogger(ClientWorker.class.getName()).log(Level.INFO, "Client worker started successfully.");
        } catch (IOException ex) {
            Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, "ClientWorker constructor: {0}", ex.getMessage());
            throw new UnexpectedErrorException(ex.getMessage());
        }
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
            Logger.getLogger(ClientWorker.class.getName()).log(Level.INFO, "Client signer disconnected.");
        } catch(IOException ie) {
            Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, "Socket close error: {0}", ie.getMessage());
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
            Logger.getLogger(ClientWorker.class.getName()).log(Level.INFO, "Message sent to the server.");
            //Receive response
            serverResponse = (Message)serverInput.readObject();
            Logger.getLogger(ClientWorker.class.getName()).log(Level.INFO, "Server response recieved.");
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, "Error sending message: {0}", ex.getMessage());
            serverResponse = new Message(Message.Type.UNEXPECTED_ERROR, null);
        }finally{
            disconnect();
        }
        return serverResponse;
    }
    
    /*
     * Handles messages from the server.
     */
    @Override
    public void run() {
        message = sendMessage(message);
    }
    /**
     * @return The message.
     */
    public Message getMessage() {
        return message;
    }
}
