package com.abillist.sysweb.controller

import com.abillist.sysweb.model.User
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

public class HomeController extends AbstractController{
    public void index(){
        setAttr("boots", getCurrentUser()?getCurrentUser().loadBoots():[])
        render("/index.ftl");
    }

    @ActionKey("/login")
    public void login(){
        String username = getPara("username")
        String password = getPara("password")
        User user = User.login(username, password)
        if (user){
            setCurrentUser(user)
        }
        renderJson([
            user: user
        ])
    }
    @ActionKey("/register")
    public void register(){
        String username = getPara("username")
        String password = getPara("password")
        User user = User.register(username, password)
        if (user){
            setCurrentUser(user)
        }
        renderJson([
            user: user
        ])
    }
}
