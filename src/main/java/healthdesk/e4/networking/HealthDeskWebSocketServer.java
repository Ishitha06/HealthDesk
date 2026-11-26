package healthdesk.e4.networking;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;

/**
 * WebSocket server for HealthDesk's network communication engine (E4).
 *
 * <p>This server accepts persistent connections from browser-based OPD
 * counter clients and provides the entry point for incoming requests.</p>
 *
 * <p>E4 is responsible for network communication only. Request validation,
 * concurrency control, and data management belong to E2, E1, and E3.</p>
 */
public class HealthDeskWebSocketServer extends WebSocketServer {

    /** Default port used by the HealthDesk WebSocket server. */
    private static final int DEFAULT_PORT = 8080;

    /**
     * Creates the HealthDesk WebSocket server.
     */
    public HealthDeskWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    /**
     * Called when a client establishes a WebSocket connection.
     *
     * @param connection the established client connection
     * @param handshake  handshake information from the client
     */
    @Override
    public void onOpen(WebSocket connection, ClientHandshake handshake) {
        connection.send("CONNECTED|HealthDesk|Connection established");
        System.out.println("Client connected: " + connection.getRemoteSocketAddress());
    }

    /**
     * Called when a client sends a message.
     *
     * @param connection the sender's connection
     * @param message   the received message
     */
    @Override
    public void onMessage(WebSocket connection, String message) {
        System.out.println("Received message: " + message);

        // Temporary response until E2 request processing is integrated.
        connection.send("RECEIVED|" + message);
    }

    /**
     * Called when a client disconnects.
     *
     * @param connection the disconnected client
     * @param code       disconnect status code
     * @param reason     disconnect reason
     * @param remote     true if the remote endpoint initiated disconnection
     */
    @Override
    public void onClose(WebSocket connection, int code, String reason, boolean remote) {
        System.out.println("Client disconnected: " + connection.getRemoteSocketAddress());
    }

    /**
     * Called when a WebSocket connection encounters an error.
     *
     * @param connection the connection involved, if available
     * @param exception  the encountered exception
     */
    @Override
    public void onError(WebSocket connection, Exception exception) {
        System.err.println("WebSocket error: " + exception.getMessage());
        exception.printStackTrace();
    }

    /**
     * Called when the WebSocket server starts successfully.
     */
    @Override
    public void onStart() {
        System.out.println("====================================");
        System.out.println("HealthDesk E4 WebSocket Server");
        System.out.println("Server started on port " + DEFAULT_PORT);
        System.out.println("Waiting for counter clients...");
    }
}