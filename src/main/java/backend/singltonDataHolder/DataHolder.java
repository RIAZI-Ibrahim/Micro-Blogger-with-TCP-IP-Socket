package backend.singltonDataHolder;

import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHolder {
    private static volatile DataHolder instance;
    private static Object lock = new Object();
    private final Map<String, List<String>> subscriptionsMap = new HashMap<>();
    private final Map<String, WebSocket> connectedClientsMap = new HashMap<>();

    private DataHolder() {
    }

    public static DataHolder getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (lock) {
            if (instance == null) {
                instance = new DataHolder();
            }
            return instance;
        }
    }

    public synchronized boolean clientExists(String userName) {
        return connectedClientsMap.containsKey(userName);
    }

    public synchronized void addConnectedClient(String userName) {
        connectedClientsMap.put(userName, null);
    }

    public synchronized void upateConnectedClient(String userName, WebSocket conn) {
        connectedClientsMap.put(userName, conn);
    }

    public synchronized void removeConnectedClient(WebSocket conn) {
        connectedClientsMap.values().remove(conn);
    }

    public void addSubscription(String client, String sub) {

        synchronized (subscriptionsMap) {
            if (subscriptionsMap.get(sub) == null) {
                List<String> subscribers = new ArrayList<>();
                subscribers.add(client);
                subscriptionsMap.put(sub, subscribers);
            } else {
                if(!subscriptionsMap.get(sub).contains(client))
                subscriptionsMap.get(sub).add(client);
            }
        }

    }

    public void removeSubscription(String client, String sub) {

        synchronized (subscriptionsMap) {
            if (subscriptionsMap.get(sub) == null) {
                return;
            } else {
                subscriptionsMap.get(sub).remove(client);
            }
        }
    }


    public List<String> getSubscribers(String subription) {
        synchronized (subscriptionsMap) {
            return subscriptionsMap.get(subription);
        }
    }

    public WebSocket getClientAddress(String clientName) {
        synchronized (connectedClientsMap) {
            return connectedClientsMap.get(clientName);

        }
    }
    public String getClientName(WebSocket address) {
        synchronized (connectedClientsMap) {
            for(String name : connectedClientsMap.keySet()){
                if(connectedClientsMap.get(name)==address)
                    return name;
            }
            return null;
        }
    }


    public void printSubsClients(){
        subscriptionsMap.forEach((k,v)-> System.out.println(k+":"+v));
    }


}
