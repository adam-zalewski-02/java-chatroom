package howest.ti.chat.logic.events;

public abstract class OutgoingEvent extends Event{

    private String message;

    public OutgoingEvent(EventType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
