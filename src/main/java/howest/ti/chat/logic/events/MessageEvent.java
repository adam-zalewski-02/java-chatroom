package howest.ti.chat.logic.events;

public class MessageEvent extends IncomingEvent{

    private String message;

    public MessageEvent(String clientId, String message) {
        super(EventType.MESSAGE, clientId);
        this.message = message;
    }

    public MessageEvent(EventType type, String clientId, String message) {
        super(type, clientId);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
