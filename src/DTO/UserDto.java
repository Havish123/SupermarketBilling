package DTO;

import Extension.Roles;

public class UserDto {
    private int userId;
    private String Name;
    private String EmailId;
    private String MobileNumber;
    private String Address;
    private String Password;
    private int RoleId;

    public UserDto(int userId, String name, String emailId, String mobileNumber, String address, int roleId,String password) {
        this.userId = userId;
        Name = name;
        EmailId = emailId;
        MobileNumber = mobileNumber;
        Address = address;
        RoleId = roleId;
        Password=password;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getRoleId() {
        return RoleId;
    }
    public String getRole() {
        return Roles.getNameByValue(this.RoleId);
    }

    public void setRoleId(int roleId) {
        RoleId = roleId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
