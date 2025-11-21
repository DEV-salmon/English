package voca.auth;

import java.util.Objects;

/**
 * 로그인 기능을 위한 클래스입니다.
 * 아이디와 패스워드, salt를 필드로 가집니다
 */
public class Login {
    private String userid;
    private String salt;
    private String hashedpassword;

    /**
     * 생성자입니다
     * @param userid
     * @param salt
     * @param hashedpassword
     */
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

    /**
     * userid, salt, hashpassword가 같아야 동일한 Login객체로 판단합니다
     */
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
