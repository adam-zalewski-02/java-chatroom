package howest.ti.chat.logic.events;

public class DiscardEvent extends IncomingEvent{
    public DiscardEvent(String message) {
        super(EventType.DISCARD, message);
    }
}
