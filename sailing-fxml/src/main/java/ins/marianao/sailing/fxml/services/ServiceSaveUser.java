package ins.marianao.sailing.fxml.services;

import cat.institutmarianao.sailing.ws.model.User;

public class ServiceSaveUser extends ServiceSaveBase<User> {

	private static final String PATH_SAVE_USER = "save/by/username";
	
    public ServiceSaveUser(User user) throws Exception {
        
        super(user, 
              User.class, 
              new String[] {
                  ServiceQueryUsers.PATH_REST_USERS,
                  PATH_SAVE_USER,
                  user.getUsername()
              },
              Method.POST, 
              true 
        );
    }
	
}
