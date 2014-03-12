package com.abillist.sysweb.service

/**
 * Created by shao on 14-3-13.
 */
class EmailServiceTest extends GroovyTestCase {
    void testSendEmail() {
        print EmailService.sendEmail(
            "th.synee@gmail.com",
            "Sysweb Account Active",
            "<a href='#'>Hello Sysweb User</a>"
        )

        assert true
    }
}
