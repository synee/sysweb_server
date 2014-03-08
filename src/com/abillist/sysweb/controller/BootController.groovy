package com.abillist.sysweb.controller

import com.abillist.sysweb.interceptor.SecureInterceptor
import com.abillist.sysweb.model.Boot
import com.abillist.sysweb.model.User
import com.jfinal.aop.Before
import com.jfinal.ext.interceptor.POST

/**
 * Created by shao on 14-3-4.
 */


@Before(SecureInterceptor.class)
class BootController extends AbstractController{
    public void add(){
        User user = this.<User>getSessionAttr("currentUser")
        String tag = getPara("tag")
        String path = getParaRelativePath("path")
        String attr = getPara("attr")
        String attrs = getPara("attrs")

        Boot boot = Boot.dao.findFirst("SELECT * FROM boot WHERE boot.path=? AND boot.user_id=?", path, user.getId())
        if (!boot){
            boot = new Boot()
        }
        boot.setUser(getCurrentUser())
        boot.set("tag", tag)
        boot.set("path", path)
        boot.set("attr", attr)
        boot.set("attrs", attrs)
        boot.save()
        renderJson(boot)
    }
    public void remove() {
        User user = this.<User>getSessionAttr("currentUser")
        String path = getParaRelativePath("path")
        Boot boot = Boot.dao.findFirst("SELECT * FROM boot WHERE boot.path=? AND boot.user_id=?", path, user.getId())
        def del = false
        if (boot)
            del = boot.delete()
        renderJson([
            success: del
        ])
    }
}
