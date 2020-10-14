package server;

public interface AuthService {

    public User getUser(String login, String password);
}
