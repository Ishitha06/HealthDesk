package healthdesk.e4.networking;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles WebSocket communication with HealthDesk counter clients.
 *
 * Responsibilities:
 * - Assign counter IDs to connected clients.
 * - Enforce the maximum of three active counter connections.
 * - Receive and respond to client messages.
 * - Release counter slots when clients disconnect.
 */
public class CounterConnectionHandler extends HealthDeskWebSocketServer {

    private final CounterManager counterManager;
    private final Map<WebSocket, Integer> connectedCounters;

    /**
     * Initializes the counter connection handler.
     *
     * @param port WebSocket server port
     */
    public CounterConnectionHandler(int port) {
        super(port);

        counterManager = new CounterManager();
        connectedCounters = new ConcurrentHashMap<>();
    }

    /**
     * Called when a client establishes a WebSocket connection.
     *
     * @param connection established WebSocket connection
     * @param handshake client handshake information
     */
    @Override
    public void onOpen(WebSocket connection, ClientHandshake handshake) {

        int counterId = counterManager.assignCounter();

        // Reject connection if all three counter slots are occupied.
        if (counterId == -1) {
            connection.send("ERROR|Maximum 3 counters allowed");
            connection.close(1008, "Counter capacity reached");
            System.out.println("Connection rejected: maximum counter capacity reached.");
            return;
        }

        connectedCounters.put(connection, counterId);

        System.out.println(
                "Counter " + counterId + " connected: "
                        + connection.getRemoteSocketAddress()
        );

        connection.send("CONNECTED|" + counterId);
    }

    /**
     * Handles messages received from connected counter clients.
     *
     * @param connection client WebSocket connection
     * @param message received message
     */
    @Override
    public void onMessage(WebSocket connection, String message) {

        Integer counterId = connectedCounters.get(connection);

        if (counterId == null) {
            connection.send("ERROR|COUNTER_NOT_REGISTERED");
            return;
        }

        System.out.println("Counter " + counterId + " sent: " + message);

        try {

            ProtocolMessage request = ProtocolMessage.parse(message);

            ProtocolResponse response = ProtocolResponse.error(
                    request.getRequestId(),
                    "E2_NOT_INTEGRATED"
            );

            connection.send(response.toMessage());

            System.out.println(
                    "Request " + request.getRequestId()
                            + " received from Counter " + counterId
                            + ". Awaiting E2 integration."
            );

        } catch (IllegalArgumentException exception) {

            ProtocolResponse response = ProtocolResponse.error(
                    "UNKNOWN",
                    "MALFORMED_REQUEST"
            );

            connection.send(response.toMessage());

            System.out.println(
                    "Malformed request rejected from Counter " + counterId
            );
        }
    }

    /**
     * Releases the assigned counter slot when a client disconnects.
     *
     * @param connection closed WebSocket connection
     * @param code closing status code
     * @param reason closing reason
     * @param remote whether the remote client initiated the closure
     */
    @Override
    public void onClose(WebSocket connection, int code, String reason, boolean remote) {

        Integer counterId = connectedCounters.remove(connection);

        if (counterId != null) {
            counterManager.releaseCounter(counterId);

            System.out.println("Counter " + counterId + " disconnected.");
        }
    }

    /**
     * Handles WebSocket networking errors.
     *
     * @param connection connection associated with the error, if available
     * @param exception encountered networking exception
     */
    @Override
    public void onError(WebSocket connection, Exception exception) {

        System.err.println("E4 networking error: " + exception.getMessage());
    }
}