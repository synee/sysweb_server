package com.abillist.sysweb.service

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.Email
import org.apache.commons.mail.HtmlEmail

/**
 * Created by shao on 14-3-13.
 */
class EmailService {



    public static String sendEmail(String to, String subject, String content){
        Email email = new HtmlEmail()
        email.setHostName("smtp.abillist.com")
        email.setAuthenticator(new DefaultAuthenticator("postmaster@abillist.com", "loveyu1314"));
        email.setFrom("postmaster@abillist.com");
        email.setSubject(subject)
        email.setHtmlMsg(content)
        email.addTo(to)
        return email.send()
    }
}
