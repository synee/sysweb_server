package com.abillist.sysweb.controller

import com.abillist.sysweb.model.User
import com.jfinal.core.ActionKey

/**
 * Created by shao on 14-3-5.
 */
class UserController extends AbstractController{
    public void current(){ renderJson([ user: getCurrentUser().exists()?getCurrentUser():null ]) }

    @ActionKey("/login")
    public void login(){
        String email = getPara("email")
        String password = getPara("password")
        User user = User.login(email, password)
        if (user){
            if (!user.getBoolean("enable")){
                renderJson([
                    error: true,
                    message: "Account is not enabled, please active you account"
                ])
            }else {
                setCurrentUser(user)
            }
        }
        renderJson([ user: user ])
    }

    @ActionKey("/register")
    public void register(){
        if (currentUser.getId() > 0){
            renderJson([
                error: true,
                message: "你不能再申请了."
            ])
            return
        }
        String email = getPara("email")
        String password = getPara("password")
        User user = User.register(email, email, password)
        if (user) setCurrentUser(user)
        renderJson([user: user])
    }
    public void active(){
        User user = User.dao.findFirst("SELECT * FROM user WHERE code=?", getPara("code"))
        if (user){
            user.set("enable", true)
            user.update()
            setCurrentUser(user)
            redirect("/")
        }else {
            renderError(404)
        }
    }
}
