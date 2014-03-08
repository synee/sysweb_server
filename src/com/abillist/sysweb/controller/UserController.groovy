package com.abillist.sysweb.controller

/**
 * Created by shao on 14-3-5.
 */
class UserController extends AbstractController{
    public void current(){
        renderJson([
            user: getCurrentUser()
        ])
    }
}
