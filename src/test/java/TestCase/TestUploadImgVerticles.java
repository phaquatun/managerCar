/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestCase;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.multipart.MultipartForm;

/**
 *
 * @author Admin
 */
public class TestUploadImgVerticles extends AbstractVerticle {

    WebClient client;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        System.out.println("run client");
        sendRequest().result();
    }

    Future<Void> sendRequest() {
        return Future.future((e) -> {

            MultipartForm data = MultipartForm.create()
                    .attribute("changer", "test")
                    .binaryFileUpload("keyFileEls", "buttonStartShare.png", "D:\\netbean12.0\\FaceBook\\DebtForgiveness\\Image", "image/jpeg");

            client.postAbs("http://localhost:8080/upload/img")
                    .sendMultipartForm(data)
                    .onSuccess((res) -> {
                        String val = res.body().toString();
                        System.out.println(val);
                    });

        });
    }

}
