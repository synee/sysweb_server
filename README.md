sysweb_server
=============

Sysweb

## API

### auth

+ login
    * url: 
        + /login
    * params:
        + username
        + password
    * return:
        + user object
+ register
    * url:
        + /register
    * params:
        + username
        + password
        + repasswd
    * return
        + user object
+ fetchme
    * url:
        + /user/current
    * return
        + user object
    
### fs
##### API
--------------
- cd
    * url:
        + /fs/cd
    * params:
        + path
    * return:
        + file object
- ls
    * url:
        + /fs/ls
    * params:
        + path
    * return:
        + file list
- cp
    * url:
        + /fs/cp
    * params:
        + source
        + dest
    * return:
        + source object
        + dest object
- mv
    * url:
        + /fs/mv
    * params:
        + source
        + dest
    * return:
        + source object
        + dest object
- mkdir
    * url:
        + /fs/mkdir
    * params:
        + path
    * return:
        + file object
- touch
    * url:
        + /fs/touch
    * params:
        + path
    * return:
        + file object
- echo 
    * url:
        + /fs/echo
    * params:
        + path
        + text
    * return:
        + file object
- append
    * url:
        + /fs/append
    * params:
        + path
        + text
    * return:
        + file object
- write
    * url:
        + /fs/write
    * params:
        + path
        + text
    * return:
        + file object
- read
    * url:
        + /fs/read
    * params:
        + path
    * return:
        + file object with content text
- head
    * url:
        + /fs/head
    * params:
        + path
        + start
        + stop
    * return:
        + file object with headlines content text
- tail
    * url:
        + /fs/tail
    * params:
        + path
        + start
        + stop
    * return:
        + file object with taillines content text
            
##### API error
--------------
- bad request
    + code: 400
    + error: true
    + description: bad request, maybe some required params are not provide
- forbidden
    + code: 403
    + error: true
    + description: access forbidden, you have to login as correct account
- not exist
    + code: 404
    + error: true
    + description: what you access is not exist 
            
        
    
### boot
- addboot
- removeboot
