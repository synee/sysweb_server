package com.abillist.sysweb;

import com.abillist.sysweb.controller.*;
import com.abillist.sysweb.handler.OwnPathHandler;
import com.abillist.sysweb.model.User;
import com.jfinal.config.*;
import com.jfinal.handler.Handler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ServerConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants constants) {
        constants.setDevMode(true);
        constants.setViewType(ViewType.FREE_MARKER);
    }

    @Override
    public void configRoute(Routes routes) {
        routes
            .add("/", HomeController.class)
            .add("/fs", FileSystemController.class)
            .add("/user", UserController.class);
    }

    @Override
    public void configPlugin(Plugins plugins) {
        DruidPlugin dp = new DruidPlugin("jdbc:mysql:///sysweb", "root", "root");
        plugins.add(dp);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        plugins.add(arp);
        arp.addMapping("user", User.class);
    }

    @Override
    public void configInterceptor(Interceptors interceptors) { }

    @Override
    public void configHandler(Handlers handlers) {
        handlers.add(new Handler() {
            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
                HttpSession session = request.getSession();
                if (session.getAttribute("currentUser") == null) {
                    session.setAttribute("currentUser", new User());
                }
                nextHandler.handle(target, request, response, isHandled);
            }
        }).add(new OwnPathHandler());
    }
}
