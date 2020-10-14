package server;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {

    private List<User> users;

    public SimpleAuthService() {
        users = new ArrayList<>();
        users.add(new User("qwe", "qwe", "qwe"));
        users.add(new User("asd", "asd", "asd"));
        users.add(new User("zxc", "zxc", "zxc"));
        for (int i = 1; i < 10; i++) {
            users.add(new User("login" + i, "pass" + i, "nick" + i));
        }
    }

    @Override
    public User getUser(String login, String password) {
        for (User user : users) {
            if(user.getLogin().equals(login) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }
}
