package chain;

import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.LinkedList;

public abstract class Chain {
    private final LinkedList<Link> chain;
    private boolean endEarly;
    private final JsonObject processObject;

    public Chain(JsonObject processObject) {
        this.processObject = processObject;
        chain = new LinkedList<>();
        endEarly = false;
    }

    /**
     * execute every link in the chain
     *
     * @return true if all the links or links until the endEarly flag raise return true
     * false if one of the link returns false
     */
    public boolean execute() {
        if (chain.isEmpty()) throw new RuntimeException("The chain is empty");
        for (Link link : chain) {
            if (!link.execute()) return false;
            if (endEarly) break;
        }
        return true;
    }

    /**
     * set the flag of endEarly=true
     */
    public void endEarly() {
        endEarly = true;
    }

    /**
     * get the process object to modify by links
     *
     * @return JsonObject
     */
    public JsonObject getProcessObject() {
        return processObject;
    }

    /**
     * add links into the chain
     * @param links the list of links
     */
    public void addLink(Link... links){
        Collections.addAll(this.chain, links);
    }
}
