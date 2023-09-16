package howest.ti.chat.logic.events;

public class BroadcastEvent extends OutgoingEvent{

    public BroadcastEvent(String message) {
        super(EventType.BROADCAST, message);
    }
}
