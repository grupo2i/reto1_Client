/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import exceptions.EmailAlreadyExistsException;
import exceptions.PasswordDoesNotMatchException;
import exceptions.UnexpectedErrorException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import user.User;

/**
 * 
 * @author aitor
 */
public interface Signable {

    /**
     * Sign up method to be implemented by all Signables
     * @param user User to sign up.
     * @return User just in case it is needed by the application.
     * @throws UserAlreadyExistsException If the username is found in the DB.
     * @throws EmailAlreadyExistsException If the email is found in the DB.
     * @throws exceptions.UnexpectedErrorException If anything else goes wrong.
     */
    public User signUp(User user) throws UserAlreadyExistsException, EmailAlreadyExistsException, UnexpectedErrorException;

    /**
     * Sign in method to be implemented by all Signables
     * @param user User to sign in.
     * @return User just in case it is needed by the application.
     * @throws exceptions.UserNotFoundException If the username is not found in the DB.
     * @throws exceptions.PasswordDoesNotMatchException If the password does not 
     * match with the username in the DB.
     * @throws exceptions.UnexpectedErrorException If anything else goes wrong.
     */
    public User signIn(User user) throws UserNotFoundException, PasswordDoesNotMatchException, UnexpectedErrorException;
    
}
