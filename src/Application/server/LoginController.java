package Application.server;

public class LoginController {
    private mysqlController sqlcontroller;

    public LoginController() {
        this.sqlcontroller = mysqlController.getSQLInstance();
    }

    public String authenticate(String username, String password){
        String ID = sqlcontroller.checkUserExists(username, password);
        if (ID == "")
            return "";
        return ID;
    }

    public boolean checkBlocked(){
        // TODO: add check if user status is blocked.
        return false;
    }

    public boolean logOut(){
        return false;
    }
}
