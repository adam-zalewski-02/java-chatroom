package howest.ti.chat.logic;

import howest.ti.chat.logic.events.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chatroom {

    private Map<String, String> users = new HashMap<>();

    public OutgoingEvent handleEvent(IncomingEvent e) {
        OutgoingEvent result = null;
        switch (e.getType()) {
            case JOIN:
                result = handleJoinEvent((JoinEvent) e);
                break;
            case MESSAGE:
                result = handleMessageEvent((MessageEvent) e);
                break;
        }
        return result;
    }

    private OutgoingEvent handleJoinEvent(JoinEvent e) {
        String username = e.getUsername();
        if (users.containsValue(username)) {
            String outgoingMessage = String.format("<em>From admin to me</em>: The username %s has already been chosen", username);
            return EventFactory.getInstance().createUnicastEvent(e.getClientId(), outgoingMessage);
        }
        String outgoingMessage = "";
        if (users.containsKey(e.getClientId())) {
            outgoingMessage = String.format("<em>From admin to everyone</em> User %s is rename to %s", users.get(e.getClientId()), username);
        } else {
            outgoingMessage = String.format("<em>From admin to everyone</em> User %s has joined the room",username);
        }
        users.put(e.getClientId(), username);
        return EventFactory.getInstance().createBroadcastEvent(outgoingMessage);

    }

    private OutgoingEvent handleMessageEvent(MessageEvent e) {
        System.out.println(e.getMessage());
        String outgoingMessage;
        if (!users.containsKey(e.getClientId())) {
            outgoingMessage = "<em>From admin to me</em>: Failed to send message - please choose a (new) username";
            return EventFactory.getInstance().createUnicastEvent(outgoingMessage, e.getClientId());
        }
        String username = users.get(e.getClientId());
        String message = e.getMessage();
        outgoingMessage = String.format("<em>From %s to everyone</em>: %s", username, message);
        return EventFactory.getInstance().createBroadcastEvent(outgoingMessage);
    }

    public List<String> getAllUsernames() {
        return new ArrayList<>(users.values());
    }

}
