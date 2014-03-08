import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.codec.digest.Md5Crypt

/**
 * Created by shao on 14-3-3.
 */

def password = "password"
println Base64.encodeBase64String(DigestUtils.md5Hex(password.getBytes()).getBytes())
println Base64.encodeBase64String(DigestUtils.md5Hex(password.getBytes()).getBytes())