package chain;

import com.google.gson.JsonObject;

import java.util.Hashtable;

public abstract class Link {
    private final static Hashtable<Integer, Link> linkStorage = new Hashtable<>();

    public static synchronized void pauseLink(Link link) throws InterruptedException{
        linkStorage.put(link.hashCode(), link);
        linkStorage.wait();
    }

    public static synchronized void unpause(int linkHashCode) {
        Link link = linkStorage.remove(linkHashCode);
        link.notify();
    }

    public static synchronized void unpause(int linkHashCode, JsonObject additionalInfo){
        Link link = linkStorage.remove(linkHashCode);
        link.chain.getProcessObject().add("requestedInfo", additionalInfo);
    }

    protected final Chain chain;

    public Link(Chain chain) {
        this.chain = chain;
    }

    /**
     * execute code within this block. all exceptions must be caught and handled inside.
     *
     * @return true if the code is successfully executed false otherwise.
     */
    public abstract boolean execute();
}
