package fr.eni.tp.auctionapp.bo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class User implements UserDetails {

    private Integer userId;
    private String username;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private String street;
    private String zipCode;
    private String city;
    private String password;
    private Integer credit;
    private boolean isAdmin;


    private Collection<SimpleGrantedAuthority> authorities;

    public User() {}

    public User(String username, String lastName, String firstName, String email, String phone, String street, String zipCode, String city, String password, Integer credit, boolean isAdmin) {
        this.username = username;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.password = password;
        this.credit = credit;
        this.isAdmin = isAdmin;
    }

    public User(Integer userId, String username, String lastName, String firstName, String email, String phone, String street, String zipCode, String city, String password, Integer credit, boolean isAdmin) {
        this(username, lastName, firstName, email, phone, street, zipCode, city, password, credit, isAdmin);
        this.userId = userId;
    }

    public void addRole(String role) {
        authorities.add(
                new SimpleGrantedAuthority(role)
        );
    }

    public Integer getId() {
        return userId;
    }

    public void setId(Integer id) {
        this.userId = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("userId=").append(userId);
        sb.append(", username='").append(username).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", street='").append(street).append('\'');
        sb.append(", zipCode='").append(zipCode).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", credit=").append(credit);
        sb.append(", isAdmin=").append(isAdmin);
        sb.append('}');
        return sb.toString();
    }
}