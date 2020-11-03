package logic;


/**
 *
 * @author aitor
 */
public class SignableFactory {
    /** Static signer for client/server communication. */
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
