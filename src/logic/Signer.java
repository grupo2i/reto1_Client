package logic;

import exceptions.EmailAlreadyExistsException;
import exceptions.PasswordDoesNotMatchException;
import exceptions.UnexpectedErrorException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import user.User;


/**
 * Handles messages from the server.
 * @author Martin Angulo
 */
public class Signer extends Thread implements Signable{
    ClientWorker worker = new ClientWorker();
    
    @Override
    public User signUp(User user) throws UserAlreadyExistsException, EmailAlreadyExistsException, UnexpectedErrorException {

    }

    @Override
    public User signIn(User user) throws UserNotFoundException, PasswordDoesNotMatchException, UnexpectedErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
