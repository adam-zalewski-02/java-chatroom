package howest.ti.chat.logic.events;

public enum EventType {
    UNICAST("unicast"),
    BROADCAST("broadcast"),
    MESSAGE("message"),
    DISCARD("discard"),
    JOIN("join");

    private String type;

    EventType(String type) {
        this.type = type;
    }

    public static EventType fromString(String type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.getType().equals(type)) {
                return eventType;
            }
        }

        return EventType.DISCARD;
    }

    public String getType() {
        return type;
    }
}
