package ins.marianao.sailing.fxml.services;

import cat.institutmarianao.sailing.ws.model.User;

public class ServiceAddUser extends ServiceAddBase<User> {

    private static final String PATH_ADD_USER = "add/user";

    public ServiceAddUser(User user) throws Exception {
        super(user, new String[] {
                ServiceQueryUsers.PATH_REST_USERS,
                PATH_ADD_USER});
    }
}
