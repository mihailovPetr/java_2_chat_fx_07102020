package server;

import static server.MessageCode.Authentification;
import static server.MessageCode.Message;

public class Request {

    private String message;
    private String[] token;
    private MessageCode code;

    public Request(String message) {
        this.message = message.trim();
        token = message.split("\\s+");

        code = MessageCode.getCode(token[0]);
    }

    public MessageCode getCode() {
        return code;
    }

    public String getLogin(){
        if (code != Authentification || token.length<3){
            return null;
        }
        return token[1];
    }

    public String getPassword(){
        if (code != Authentification || token.length<3){
            return null;
        }
        return token[2];
    }

    public String getDestination(){
        if (code != Message || token.length<3){
            return null;
        }
        return token[1];
    }

    public String getMessage(){
        if (code != Message || token.length<3){
            return null;
        }
        return token[2];
    }
}
