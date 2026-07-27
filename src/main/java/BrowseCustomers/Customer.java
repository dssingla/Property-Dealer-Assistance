package BrowseCustomers;

import java.sql.Date;

public class Customer {

    private String mobile;
    private String name;
    private String address;
    private String city;
    private String email;
    private String category;
    private Date date;
    private byte[] picture;

    public Customer(String mobile, String name, String address,
                    String city, String email,
                    String category, Date date, byte[] picture) {

        this.mobile = mobile;
        this.name = name;
        this.address = address;
        this.city = city;
        this.email = email;
        this.category = category;
        this.date = date;
        this.picture = picture;
    }


    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    public byte[] getPicture(){

        return picture;

    }
}