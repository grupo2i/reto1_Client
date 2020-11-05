package logic;

import exceptions.EmailAlreadyExistsException;
import exceptions.PasswordDoesNotMatchException;
import exceptions.UnexpectedErrorException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import user.User;

/**
 * Handles messages from the server.
 * @author Martin Angulo, Aitor Fidalgo
 */
public class Signer implements Signable{
    /**
     * Sign up function implementation.
     * @param user User to sign up.
     * @return The User in case the application needs it.
     * @throws UserAlreadyExistsException If the username is found in the DB.
     * @throws EmailAlreadyExistsException If the email is found in the DB.
     * @throws exceptions.UnexpectedErrorException If anything else goes wrong.
     */
    @Override
    public User signUp(User user) throws UserAlreadyExistsException, EmailAlreadyExistsException, UnexpectedErrorException {
        ClientWorker worker = new ClientWorker(new Message(Message.Type.SIGN_UP, user));
        worker.start();
        try {
            worker.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Signer.class.getName()).log(Level.SEVERE, "InterruptedException: {0}", ex.getMessage());
        }
        Message serverResponse = worker.getMessage();
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
    public User signIn(User user) throws UserNotFoundException, PasswordDoesNotMatchException, UnexpectedErrorException{
        ClientWorker worker = new ClientWorker(new Message(Message.Type.SIGN_IN, user));
        worker.start();
        try {
            worker.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Signer.class.getName()).log(Level.SEVERE, "InterruptedException: {0}", ex.getMessage());
        }
        Message serverResponse = worker.getMessage();
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