package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import static server.MessageCode.*;

public class ClientHandler {

    private Server server;
    private Socket socket;
    private AuthService authService;
    private DataInputStream in;
    private DataOutputStream out;
    private User user = null;

    public ClientHandler(Server server, Socket socket, AuthService authService) {
        try {

            this.server = server;
            this.socket = socket;
            this.authService = authService;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Client connected " + socket.getRemoteSocketAddress());

            new Thread(() -> {

                try {
                    while (true) {
                        String str = in.readUTF();
                        Request request = new Request(str);

                        switch (request.getCode()) {
                            case Authentification:
                                authentificate(request);
                                break;
                            case Message:
                                sendMsgTo(request.getDestination(), request.getMessage());
                                break;
                            case Disconnect:
                                disconnect();
                                break;
                            case Undefined:
                                break;
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
                }

            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void authentificate(Request request) {
        if (user != null || request.getCode() != Authentification) {
            return;
        }

        user = authService.getUser(request.getLogin(), request.getPassword());
        if (user != null) {
            server.subscribe(this);
            sendMsg("/authok " + user.getNickname());
        } else {
            sendMsg("Неверный логин / пароль");
        }

    }

    private void disconnect() {
        server.unsubscribe(this);
        System.out.println("Client disconnected " + socket.getRemoteSocketAddress());
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return user.getNickname();
    }

    private void sendMsgTo(String destination, String message) {
        if (user != null) {
            server.sendMsgTo(this, destination, message);
        }
    }
}
