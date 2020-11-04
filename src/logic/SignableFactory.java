package logic;


/**
 *
 * @author aitor
 */
public class SignableFactory {
    /**
     * Return an static Signable for client/server communication.
     * @return The Signable to use.
     */
    public static Signable getSignable(){
        return new Signer();
    }
}
