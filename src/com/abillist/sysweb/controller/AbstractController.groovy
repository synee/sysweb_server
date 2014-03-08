package com.abillist.sysweb.controller

import com.abillist.sysweb.model.User
import com.jfinal.core.Controller
import com.jfinal.kit.PathKit

import java.nio.file.NoSuchFileException

/**
 * Created by shao on 14-3-4.
 */
class AbstractController extends Controller{

    public static String FS_ROOT = PathKit.getWebRootPath() + "/fs";
    public static String USERNAME = "freeman";
    private currentUser

    protected String getMyDir() {
        File file = new File("$FS_ROOT/${getCurrentUser() != null ? getCurrentUser().username : USERNAME}");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    protected String getParaPath() throws NoSuchFileException {
        return getParaPath("path");
    }

    protected String getParaPath(String pathName) throws NoSuchFileException {
        String path = getPara(pathName);
        if (path == null) {
            throw new NoSuchFileException(path);
        } else if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return getMyDir() + path;
    }

    protected String getParaRelativePath(String pathName) throws NoSuchFileException {
        def path = getParaPath(pathName)
        return path.replace("$FS_ROOT", "");
    }

    public User getCurrentUser(){
        return this.<User>getSessionAttr("currentUser")
    }

    protected void setCurrentUser(currentUser) {
        setSessionAttr("currentUser", currentUser)
        this.currentUser = currentUser
    }
}
