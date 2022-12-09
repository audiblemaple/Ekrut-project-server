package Data.UserData;

import java.io.Serializable;

// this method will probably be used to send userdata instead of strings in the future
public class User implements Serializable {
    private String firstname;
    private String lastname;
    private String id;
    private String phonenumber;
    private String emailaddress;
    private String creditcardnumber;
    private String subscribernumber;

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getId() {
        return id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public String getCreditcardnumber() {
        return creditcardnumber;
    }

    public String getSubscribernumber() {
        return subscribernumber;
    }
}