package chain.gmail;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

import java.util.Hashtable;
import java.util.LinkedList;

public class LinkCreateSession extends Link {
    public static final Hashtable<String, LinkedList<JsonObject>> mailQueue = new Hashtable<>();

    public LinkCreateSession(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        String key = chain.getProcessObject().get("body").getAsJsonObject().get("to").getAsString();
        LinkedList<JsonObject> list;
        synchronized (mailQueue) {
            if (mailQueue.containsKey(key)) {
                list = mailQueue.get(key);
            } else {
                list = new LinkedList<>();
                mailQueue.put(key, list);
            }
        }
        synchronized (list) {
            list.add(chain.getProcessObject());
        }
        return true;
    }
}
