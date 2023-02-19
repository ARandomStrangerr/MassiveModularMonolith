package chain.gmail.handle_request;

import chain.Chain;
import chain.Link;
import com.google.gson.JsonObject;

import java.util.Hashtable;
import java.util.LinkedList;

class LinkQueueMail extends Link {
    private static final Hashtable<String, LinkedList<JsonObject>> mailMap = new Hashtable<>();

    public LinkQueueMail(Chain chain) {
        super(chain);
    }

    @Override
    public boolean execute() {
        String key = chain.getProcessObject().get("body").getAsJsonObject().get("username").getAsString();
        LinkedList<JsonObject> queue;
        if (mailMap.containsKey(key)) {
            queue = mailMap.get(key);
        } else {
            queue = new LinkedList<>();
            mailMap.put(key, queue);
            new Thread(getRunnable()).start();
        }
        synchronized (queue) {
            queue.add(this.chain.getProcessObject());
        }
        return false;
    }

    private Runnable getRunnable() {
        return () -> {

        };
    }
}
