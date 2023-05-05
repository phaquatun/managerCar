package Model.SetUpServer;

import Model.Admin.AdminVerticle;
import Model.User.DataBase.DAOUser;
import Model.User.UserVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.CookieSameSite;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ChainAuthHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import java.net.CookieHandler;
import java.util.Arrays;
import java.util.List;

public class SetUpServer extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        configRouter()
                .compose(this::createHttpServer)
                .compose(this::deployVertices)
                .onComplete(this::resultStartServer);
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {

    }

    Future<Router> configRouter() {
        return Future.future((promise) -> {
            Router router = Router.router(vertx);

            SessionStore sessionStoreV1 = LocalSessionStore.create(vertx);
            sessionStoreV1.delete("vertx-web.session").result();

            router.route().handler(SessionHandler.create(sessionStoreV1)
                    .setCookieHttpOnlyFlag(true)
                    .setSessionCookieName("vcar")
                    .setCookieMaxAge(35 * 3600 * 24).
                    setMinLength(60)
            );

            router.route().handler(BodyHandler.create());
            // cors for test 
            router.route().handler(CorsHandler.create("*")
                    .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                    .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                    .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                    .allowedHeader("Access-Control-Allow-Credentials")
                    .allowedHeader("Access-Control-Allow-Headers")
                    .allowedHeader("Content-Type"));

            promise.complete(router);
        });
    }

    Future<Router> createHttpServer(Router router) {
        return Future.future((promise) -> {
            vertx.createHttpServer().requestHandler(router).listen(8080).result();
            promise.complete(router);
        });
    }

    /*
    ** deploy all vertices project car
     */
    Future<Void> deployVertices(Router router) {

        var admin = Future.future((promise) -> promise.complete(vertx.deployVerticle(new AdminVerticle(router)).result()));

        var user = Future.future((promise) -> promise.complete(vertx.deployVerticle(new UserVerticle(router)).result()));
        var dataUser = Future.future((promise) -> promise.complete(vertx.deployVerticle(new DAOUser()).result()));

        List<Future> listFutre = Arrays.asList(admin, user);
        return CompositeFuture.all(listFutre).mapEmpty();
    }

    void resultStartServer(AsyncResult<Void> asynResult) {
        if (asynResult.succeeded()) {
            System.out.println("create server Success");
        }
        if (asynResult.failed()) {
            System.out.println("create server fail " + asynResult.cause().getMessage());
        }
    }

}
