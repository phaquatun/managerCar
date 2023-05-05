package TestCase;

import java.io.File;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import tungphamdev.oraclesun.RestclientVer2.RestClient;

public class TestUpload {

    public static void main(String[] args) {
        String pathImage = "D:\\netbean12.0\\FaceBook\\DebtForgiveness\\Image\\buttonStartShare.png";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE).addBinaryBody("file", new File(pathImage));
        RestClient client = new RestClient();

        client.create((clientBuilder) -> clientBuilder.disableRedirectHandling())
                .POST("http://localhost:8080/register/upload/img", client.MULTIPART(builder), (request) -> {
                })
                .onSuccess((response, bodyResponse) -> {
                    System.out.println(bodyResponse.getContenty());
                })
                .onFailure((statusCode, response, bodyResponse) -> {
                    System.out.println("err " + statusCode + " - " + bodyResponse.getContenty());
                });
    }
}
