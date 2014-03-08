package com.abillist.sysweb.model

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
    private List<Boot> boots

    long getId() {
        return this.getInt("id")
    }

    void setId(long id) {
        this.id = id
    }

    String getUsername() {
        return this.getStr("username")
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

    List<Boot> loadBoots() {
        return Boot.dao.findByUser(this)
    }

    public static User login(String username, String password){
        User user = dao.findFirst("SELECT * FROM user AS u WHERE u.username=?", username)
        if (user && encrypt(password).equals(user.getPassword())){
            return user
        }else {
            return null
        }
    }

    public static User register(String username, String password){
        User user = dao.findFirst("SELECT * FROM user AS u WHERE u.username=?", username)
        if (user){
            return null
        }else {
            user = new User()
            user.set("username", username).set("password", encrypt(password))
            user.save()
            return user
        }
    }

    public static encrypt(String source){
        return Base64.encodeBase64String(DigestUtils.md5Hex(source.getBytes()).getBytes())
    }

}
