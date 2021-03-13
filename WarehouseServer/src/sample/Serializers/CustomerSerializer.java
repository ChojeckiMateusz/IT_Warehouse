package sample.Serializers;

import java.io.Serializable;

public class CustomerSerializer implements Serializable {
    private String name;

    private String surname;

    private String email;

    private String phoneNumber;

    private String address;

    public CustomerSerializer(String name, String surname, String email, String phoneNumber, String address) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }
}
