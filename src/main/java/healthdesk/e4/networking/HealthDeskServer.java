package healthdesk.e4.networking;

/**
 * Main entry point for the HealthDesk network communication engine (E4).
 *
 * Starts the WebSocket server that allows counter clients to connect
 * to the HealthDesk system.
 */
public class HealthDeskServer {

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        final int serverPort = 8080;

        CounterConnectionHandler server =
                new CounterConnectionHandler(serverPort);

        System.out.println("Starting HealthDesk server...");

        server.start();
    }
}