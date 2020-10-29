/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import user.User;

/**
 * 
 * @author aitor
 */
public interface Signable {

    /**
     * Sign up method to be implemented by all Signables.
     * @param user User to sign up.
     * @return User just in case it is needed by the application.
     */
    public User signUp(User user);

    /**
     * Sign in method to be implemented by all Signables.
     * @param user User to sign in.
     * @return User just in case it is needed by the application.
     */
    public User signIn(User user);
    
}
