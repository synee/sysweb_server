package com.abillist.sysweb;

import com.abillist.sysweb.controller.*;
import com.abillist.sysweb.model.Boot;
import com.abillist.sysweb.model.Share;
import com.abillist.sysweb.model.User;
import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;


public class ServerConfig extends JFinalConfig{
    @Override
    public void configConstant(Constants constants) {
        constants.setDevMode(true);
        constants.setViewType(ViewType.FREE_MARKER);
    }

    @Override
    public void configRoute(Routes routes) {
        routes.add("/", HomeController.class);
        routes.add("/fs", FileSystemController.class);
        routes.add("/boot", BootController.class);
        routes.add("/share", ShareController.class);
        routes.add("/user", UserController.class);
    }

    @Override
    public void configPlugin(Plugins plugins) {

        DruidPlugin dp = new DruidPlugin("jdbc:mysql:///sysweb", "root", "root");
        plugins.add(dp);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        plugins.add(arp);

        arp.addMapping("user", User.class);
        arp.addMapping("share", Share.class);
        arp.addMapping("boot", Boot.class);

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}
