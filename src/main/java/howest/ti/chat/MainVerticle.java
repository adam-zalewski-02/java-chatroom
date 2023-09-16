package howest.ti.chat;

import howest.ti.chat.logic.Chatroom;
import howest.ti.chat.logic.events.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class MainVerticle extends AbstractVerticle {
    private static final int PORT = 8080;
    private static final String CHNL_TO_SERVER = "events.to.server";
    private static final String CHNL_TO_CLIENTS = "events.to.clients";

    private static final String CHNL_TO_CLIENT_UNICAST = "events.to.client.";
    private static final String CHNL_ROOT_PATH = "/events/*";
    private static final String USERNAMES_ROUTE = "/users";

    private static final Chatroom CHATROOM = new Chatroom();

    public void start() {

        Router router = Router.router(vertx);

        router.route(CHNL_ROOT_PATH).handler(createSockJsHandler());
        router.route(USERNAMES_ROUTE).handler(this::getAllUsernamesHandler);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(PORT);

        registerConsumers();
    }

    private void registerConsumers() {
        vertx.eventBus().consumer(CHNL_TO_SERVER, this::handleIncomingMessage);
    }

    private SockJSHandler createSockJsHandler() {
        final SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        final PermittedOptions inbound = new PermittedOptions().setAddressRegex("events\\..+");
        final PermittedOptions outbound = inbound;

        final SockJSBridgeOptions options = new SockJSBridgeOptions().addInboundPermitted(inbound).addOutboundPermitted(outbound);

        sockJSHandler.bridge(options);
        return sockJSHandler;
    }

    private void handleIncomingMessage(Message<JsonObject> msg) {
        IncomingEvent incomingEvent = EventFactory.getInstance().createIncomingEvent(msg.body());

        OutgoingEvent result = CHATROOM.handleEvent(incomingEvent);
        handleOutgoingMessage(result);
    }

    private void handleOutgoingMessage(OutgoingEvent result) {
        switch (result.getType()) {
            case BROADCAST:
                broadcastMessage((BroadcastEvent) result);
                break;
            case UNICAST:
                unicastMessage((UnicastEvent) result);
                break;
            default:
                throw new IllegalArgumentException("Type of outgoing message not supported");
        }
    }

    private void broadcastMessage(BroadcastEvent event) {
        vertx.eventBus().publish(CHNL_TO_CLIENTS, event.getMessage());
    }

    private void unicastMessage(UnicastEvent event) {
        vertx.eventBus().publish(CHNL_TO_CLIENT_UNICAST + event.getRecipient(), event.getMessage());
    }

    private void getAllUsernamesHandler(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json")
                .putHeader("Access-Control-Allow-Origin", "*")
                .end(Json.encodePrettily(CHATROOM.getAllUsernames()));
    }

}
