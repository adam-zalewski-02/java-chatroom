package howest.ti.chat.logic.events;

public class JoinEvent extends IncomingEvent{
    private String username;

    public JoinEvent( String clientId, String username) {
        super(EventType.JOIN, clientId);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
