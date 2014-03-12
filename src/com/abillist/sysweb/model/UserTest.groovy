package com.abillist.sysweb.model

import org.apache.commons.codec.binary.Base64

/**
 * Created by shao on 14-3-13.
 */
class UserTest extends GroovyTestCase {
    void setUp() {
        super.setUp()

    }

    void tearDown() {

    }

    void testEncrypt() {
        print Base64.encodeBase64URLSafeString(User.encrypt("Hello").bytes)
    }
}
