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
    public User signUp(User user);
    public User signIn(User user);
    
}
