package Model.User;

import com.mycompany.managercar.HandlerCookie;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.Cookie;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;

public class UserVerticle extends AbstractVerticle {

    private Router router;

    public UserVerticle(Router router) {
        this.router = router;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        router.get("/eve").handler(this::handleUserTest);
        router.get("/eve/img").handler(this::handerDoawloadImage);

        router.post("/register/user").handler(BodyHandler.create());
        router.post("/register/user").handler(this::handleRegisterUser);

        router.post("/register/upload/img").handler(BodyHandler.create().setHandleFileUploads(true));
        router.post("/register/upload/img").handler(this::handleUpImage);
    }

    void handleRegisterUser(RoutingContext ctx) {
        JsonObject jsonBody = ctx.getBodyAsJson();

        vertx.eventBus().request(ConstanceUser.registerUser, jsonBody, (msg) -> {
            ctx.response().end((String) msg.result().body());
        });
    }

    void handleUpImage(RoutingContext ctx) {
        String line = ctx.request().getParam("change");
		

        var setFileUpload = ctx.fileUploads();
        for (FileUpload fileUpload : setFileUpload) {
            var asynFile = vertx.fileSystem().open(fileUpload.uploadedFileName(), new OpenOptions());

            vertx.eventBus().request(ConstanceUser.upImage, asynFile.result(), (msg) -> {
                String res = (String) msg.result().body();
                if (res.contains("success")) {
                    ctx.request().response().end("ok " + res);
                } else {
                    ctx.request().response().end("err " + res);
                }
            });
            
        }
//        FileUpload fileUpload = ctx.fileUploads();

//        if (uploads.size() <= 3) {
//
//            List<Future> listFut = new ArrayList<>();
//            for (FileUpload fileUpload : ctx.fileUploads()) {
//
//                Future<Void> futRest = Future.future((promise) -> {
//                    var asynFile = vertx.fileSystem().open(fileUpload.uploadedFileName(), new OpenOptions())
//                            .onComplete((asynRes) -> {
//                                if (asynRes.succeeded()) {
//                                    vertx.eventBus().request(ConstanceUser.upImage, asynRes.result(), (msg) -> {
//                                        String res = (String) msg.result().body();
//                                        if (res.contains("success")) {
//                                            promise.complete();
//                                        } else {
//                                            promise.fail("err upload img");
//                                        }
//                                    });
//                                }
//
//                                if (asynRes.failed()) {
//                                    promise.fail("err upload img");
//                                }
//
//                            });
//
//                });
//                listFut.add(futRest);
//            }
//            CompositeFuture.all(listFut).onComplete((asynRes) -> {
//                if (asynRes.succeeded()) {
//                    ctx.request().response().end("success");
//                } else {
//                    asynRes.cause().printStackTrace();
//                    ctx.request().response().end(asynRes.cause().getMessage());
//                }
//            });
//
//        } else {
//            ctx.request().response().setStatusCode(300).end("some thing wrong ...");
//        }
    }

    void handerDoawloadImage(RoutingContext ctx) {
        vertx.eventBus().request(ConstanceUser.doawnImage, "", (msg) -> {
            Buffer val = vertx.fileSystem().readFile((String) msg.result().body()).result();

            ctx.request().response().end(val.toString());
        });
    }

//    Future<String> handleFileUpload(RoutingContext ctx, FileUpload fileUpload) {
//        return Future.future((promise) -> {
//            var asynFile = vertx.fileSystem().open(fileUpload.contentTransferEncoding(), new OpenOptions());
//            vertx.eventBus().request(ConstanceUser.upImage, asynFile, (msg) -> {
//                String res = (String) msg.result().body();
//            });
//
//        });
//    }
    void handleUserTest(RoutingContext ctx) {

        Session session = ctx.session();

        Integer cnt = session.get("hitcount");
        cnt = (cnt == null ? 0 : cnt) + 1;
        session.put("hitcount", cnt);

        String key = "xs";
        String value = new HandlerCookie(ctx, "tungpham", "localhost").getCookie(key);
        ctx.response().addCookie(Cookie.cookie(key, value));

        ctx.session().regenerateId();
        ctx.response().putHeader("content-type", "text/html")
                .end("<html><body><h1>Hitcount: " + cnt + "</h1></body></html>");

    }
}
