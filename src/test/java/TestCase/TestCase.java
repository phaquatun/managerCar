package TestCase;

import com.mycompany.managercar.HandlerCookie;
import io.vertx.core.json.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class TestCase {

    public static void main(String[] args) throws InterruptedException {
        long time = System.currentTimeMillis();
        HandlerCookie handleCookie = new HandlerCookie("tungpham", "locahost");
        String val = handleCookie.getCookie("xd");
        System.out.println(val);

        boolean check = handleCookie.checkCookie(val, "tungpham" , "locahost");
        
        System.out.println(check);
        System.out.println("end " + (System.currentTimeMillis() - time));
    }

    public void run() {
//    new 
    }

    private String encode(String key) {

        return Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
    }
}
