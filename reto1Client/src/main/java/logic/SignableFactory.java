package logic;

import exceptions.UnexpectedErrorException;


/**
 *
 * @author aitor
 */
public class SignableFactory {
    /**
     * Return an static Signable for client/server communication.
     * @return The Signable to use.
     * @throws exceptions.UnexpectedErrorException
     */
    public static Signable getSignable() throws UnexpectedErrorException{
        return new Signer();
    }
}
