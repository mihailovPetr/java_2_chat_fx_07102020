package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private List<ClientHandler> clients;
    private AuthService authService;
    private ServerSocket server;
    public final int PORT;

    public Server() {
        this(8189);
    }

    public Server(int port) {
        clients = new CopyOnWriteArrayList<>();
        authService = new SimpleAuthService();
        PORT = port;
    }


    public void start() {

        Socket socket = null;

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started");

            while (true) {
                socket = server.accept();
                new ClientHandler(this, socket, authService);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public void sendMsgTo(ClientHandler sender, String destination, String msg) {
        if (destination == null || msg == null) {
            return;
        }

        String message = String.format("[ %s ]: %s", sender.getNickname(), msg);

        if (destination.equals("/all")) {
            for (ClientHandler c : clients) {
                c.sendMsg(message);
            }
        } else {
            for (ClientHandler c : clients) {
                if (c.getNickname().equals(destination)) {
                    c.sendMsg(message);
                    sender.sendMsg(message);
                    break;
                }

            }
        }
    }


}
