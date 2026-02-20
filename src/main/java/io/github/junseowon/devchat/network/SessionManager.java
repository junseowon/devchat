package io.github.junseowon.devchat.network;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SessionManager {
    private final List<ClientSession> activeSessions = new CopyOnWriteArrayList<>();

    public void addSession(ClientSession session) {
        activeSessions.add(session);
        System.out.println("[SESSION] Current active users: " + activeSessions.size());
    }

    public void removeSession(ClientSession session) {
        activeSessions.remove(session);
        System.out.println("[SESSION] Current active users: " + activeSessions.size());
    }

    public void broadcast(String message) {
        for (ClientSession session : activeSessions) {
            session.send(message);
        }
    }

    public void clearAll() {
        activeSessions.clear();
    }
}