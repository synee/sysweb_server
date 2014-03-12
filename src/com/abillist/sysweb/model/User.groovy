package com.abillist.sysweb.model

import com.abillist.sysweb.service.EmailService
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.codec.digest.Md5Crypt


/**
 * Created by shao on 14-3-3.
 */
class User extends AbstractModel<User>{
    public static User dao = new User()
    long id
    private String username
    private String password

    long getId() {
        try {
            return this.getInt("id")
        }catch (Exception e){
            return 0;
        }
    }

    void setId(long id) {
        this.id = id
    }

    String getUsername() {
        return this.getStr("username")?:"Anonymous"
    }

    void setUsername(String username) {
        this.set("username", username)
        this.username = username
    }

    String getPassword() {
        return getStr("password")
    }

    void setPassword(String password) {
        this.password = encrypt(password)
    }

    public static User login(String username, String password){
        User user = dao.findFirst("SELECT * FROM user AS u WHERE u.username=? AND u.enable=true ", username)
        if (user && encrypt(password).equals(user.getPassword())){
            return user
        }else {
            return null
        }
    }

    public static User register(String email, String username, String password){
        User user = dao.findFirst("SELECT * FROM user AS u WHERE u.username=? OR email=?", username, email)
        if (user){
            return null
        }else {
            user = new User()
            String code = Base64.encodeBase64URLSafeString(encrypt("$email$password").bytes)
            user.set("username", username)
                .set("password", encrypt(password))
                .set("email", email)
                .set("code", code)
            user.save()
            EmailService.sendEmail(
                email,
                "Account Active",
                "Click <a href='http://localhost:8080/user/active?uid=${user.getId()}&code=$code'>http://localhost:8080/user/active?uid=${user.getId()}&code=$code</a> to active your account."
            )
            return user
        }
    }

    public static encrypt(String source){
        return Base64.encodeBase64String(DigestUtils.md5Hex(source.getBytes()).getBytes())
    }

    public boolean exists(){
        return this.getId() && this.getId() > 0 && this.getBoolean("enable")
    }

}
