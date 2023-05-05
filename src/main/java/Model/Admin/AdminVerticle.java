package Model.Admin;

import Model.SetUpServer.ConstantManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class AdminVerticle extends AbstractVerticle {

    private Router router;

    public AdminVerticle(Router router) {
        this.router = router;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        router.get("/user").handler((e) -> e.response().end("hello user ..."));

        router.get("/register/admin").handler(this::handleHelloAdmin);
        router.post("/register/admin").handler(BodyHandler.create());
        router.post("/register/admin").handler(this::handleRegistAdmin);

    }

    /*
    *** eventbus is custom 
    *** end request for client 
     */
    // send request for customer
    void handleHelloAdmin(RoutingContext ctx) {
        vertx.eventBus().request("admin.hello", "", (msg) -> {
            ctx.request().response().end("hello vertx tungpham  ...");
        });
    }

    void handleRegistAdmin(RoutingContext ctx) {
        JsonObject jsonBody = ctx.getBodyAsJson();

        vertx.eventBus().request(ConstantManager.registerAdmin, jsonBody, (msg) -> {
            ctx.request().response().end((String) msg.result().body());
        });
    }

}
