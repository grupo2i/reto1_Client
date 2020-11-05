package logic;

import exceptions.UnexpectedErrorException;


/**
 * Returns Signable implementations.
 * @author Aitor Fidalgo
 */
public class SignableFactory {
    /**
     * Return a Signable implementation for client/server communication.
     * @return The Signable to use.
     * @throws exceptions.UnexpectedErrorException
     */
    public static Signable getSignable() throws UnexpectedErrorException{
        return new Signer();
    }
}
