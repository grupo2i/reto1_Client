/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;


/**
 *
 * @author aitor
 */
public class SignableFactory {
    private static Signer signer;
    
    /**
     * Return an static Signable for client/server communication.
     * @return The Signable to use.
     */
    public static Signable getSignable(){
        if(signer == null)
            signer = new Signer();
        return signer;
    }
}
