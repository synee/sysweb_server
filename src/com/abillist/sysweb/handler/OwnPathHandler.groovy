package com.abillist.sysweb.handler

import com.abillist.sysweb.model.User
import com.jfinal.handler.Handler
import groovy.json.JsonOutput

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by shao on 14-3-18.
 */
class OwnPathHandler extends Handler{
    @Override
    void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        def uri = request.getRequestURI()
        def paths = uri.split("/")
        def user = (User)request.getSession().getAttribute("currentUser")
        println(uri)
        println(paths)
        if (user.exists() && paths.length > 3 && paths[1] == "sys_root"){
            if (paths[2] == user.getUsername()){
                this.nextHandler.handle(target, request, response, isHandled)
            }else {
                response.setStatus(403)
                response.writer.write(JsonOutput.toJson([
                    error: true,
                    message: 403
                ]))
            }
        }else {
            this.nextHandler.handle(target, request, response, isHandled)
        }


    }
}
