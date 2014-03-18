package com.abillist.sysweb.controller

import com.abillist.sysweb.model.User
import com.jfinal.core.Controller
import com.jfinal.kit.PathKit
import com.jfinal.render.JsonRender
import com.jfinal.render.TextRender
import groovy.json.JsonOutput
import org.apache.commons.io.FileUtils

import java.nio.file.NoSuchFileException

/**
 * Created by shao on 14-3-4.
 */
class AbstractController extends Controller {

    public static String FS_ROOT = PathKit.getWebRootPath() + "/sys_root";
    public static String USERNAME = "freeman";
    private currentUser

    protected String getMyDir() {
        File file = new File("$FS_ROOT/${getCurrentUser() != null ? getCurrentUser().username : USERNAME}");
        if (!file.exists()) {
            file.mkdirs();
            new File(file.getAbsolutePath() + "/__sys.js").createNewFile()
        }
        return file.getAbsolutePath();
    }

    protected String getParaPath() {
        try{
            return getParaPath("path");
        }catch (NoSuchFileException e){
            throw new RuntimeException(e)
        }
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

    public User getCurrentUser() {
        return this.<User> getSessionAttr("currentUser")
    }

    protected void setCurrentUser(currentUser) {
        this.setSessionAttr("currentUser", currentUser)
        this.currentUser = currentUser
    }

    public void renderError(int status, Map map) {
        renderError(status, new JsonRender(JsonOutput.toJson(map)))
    }

    public void renderErrorWithMessage(int status, String msg) {
        renderError(status, [
            error: true,
            message: msg
        ])
    }

    public void renderError400WithMessage(String msg) {
        renderErrorWithMessage(400, msg)
    }

    public void renderError404WithMessage(String msg) {
        renderErrorWithMessage(404, msg)
    }

    public void renderError500WithMessage(String msg) {
        renderErrorWithMessage(500, msg)
    }
}
