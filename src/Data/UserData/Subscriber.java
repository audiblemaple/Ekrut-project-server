package Data.UserData;

import java.io.Serializable;

// this method will probably be used to send userdata instead of strings in the future
public class Subscriber implements Serializable {
    private String name;
    private String lastname;
    private String ID;
    private String phonenumber;
    private String email;
    private String creditcardnumber;
    private String subscribernumber;
}
