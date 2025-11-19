package voca.auth;

import java.util.Objects;

public class Login {
    private String userid;
    private String salt;
    private String hashedpassword;

    public Login(String userid, String salt, String hashedpassword) {
        this.userid = userid;
        this.salt = salt;
        this.hashedpassword = hashedpassword;
    }

    public String getUserid() {
        return userid;
    }
    public String getSalt() {
        return salt;
    }
    public String getHashedpassword() {
        return hashedpassword;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public void setHashedpassword(String hashedpassword) {
        this.hashedpassword = hashedpassword;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Login login)) return false;
        return Objects.equals(userid, login.userid) && Objects.equals(salt, login.salt) && Objects.equals(hashedpassword, login.hashedpassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, salt, hashedpassword);
    }
}
