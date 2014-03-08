package com.abillist.sysweb.model

/**
 * Created by shao on 14-3-3.
 */
class Share extends AbstractModel<Share>{
    public static Share dao = new Share()

    private int id
    private String name
    private String path

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getPath() {
        return path
    }

    void setPath(String path) {
        this.path = path
    }
}
