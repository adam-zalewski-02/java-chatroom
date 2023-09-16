package howest.ti.chat.logic.events;

public class UnicastEvent extends OutgoingEvent{

    private String recipient;

    public UnicastEvent(String message, String recipient) {
        super(EventType.UNICAST, message);
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }
}
