package sample.Serializers;

import java.io.Serializable;

public class UserSerializer implements Serializable {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    public UserSerializer(Integer id, String username, String password, String email, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


}
