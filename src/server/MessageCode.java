package server;

public enum MessageCode {
    Authentification("/Authentification"), Message("/Message"), Disconnect("/Disconnect"), Undefined("/Undefined");

    private String code;

    MessageCode(String code) {
        this.code = code;
    }

    public static MessageCode getCode(String msg){
        switch (msg) {
            case "/Authentification":
                return Authentification;
            case "/Message":
                return Message;
            case "/Disconnect":
                return Disconnect;
            default:
                return Undefined;
        }
    }


    @Override
    public String toString() {
        return code;
    }
}
