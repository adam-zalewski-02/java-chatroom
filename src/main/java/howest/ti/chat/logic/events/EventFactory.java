package howest.ti.chat.logic.events;

import io.vertx.core.json.JsonObject;

public class EventFactory {

    private static final EventFactory instance = new EventFactory();

    public static EventFactory getInstance() {
        return instance;
    }

    public IncomingEvent createIncomingEvent(JsonObject json) {
        EventType eventType = EventType.fromString(json.getString("type"));
        String clientId = json.getString("clientId");
        IncomingEvent event = new DiscardEvent(clientId);
        switch (eventType) {
            case MESSAGE:
                MessageEvent messageEvent = new MessageEvent(clientId, json.getString("message"));
                event = messageEvent;
                break;
            case JOIN:
                JoinEvent joinEvent = new JoinEvent(clientId, json.getString("username"));
                event = joinEvent;
                break;
            default:
                break;
        }
        return event;
    }

    public BroadcastEvent createBroadcastEvent(String msg) {
        return new BroadcastEvent(msg);
    }

    public UnicastEvent createUnicastEvent(String recipient, String msg) {
        return new UnicastEvent(recipient, msg);
    }
}
