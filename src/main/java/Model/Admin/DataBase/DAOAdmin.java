package Model.Admin.DataBase;

import Model.User.ConstanceUser;
import Model.SetUpServer.ConstantManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class DAOAdmin extends AbstractVerticle {

    MongoClient mongoClient;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        createMongoDB();
    }

    Future<Void> createMongoDB() {
        return Future.future((promise) -> {
            JsonObject query = new JsonObject().put("db_name", ConstantManager.Db_Name).put("connection_string", ConstanceUser.Collections_User);
            mongoClient = MongoClient.createShared(vertx, query);
            promise.complete();
        });
    }

    Future<Void> configEventBus() {
        return Future.future((promise) -> {
            vertx.eventBus().consumer(ConstantManager.registerAdmin, this::handleRegisAdmin);
            promise.complete();
        });
    }

    void handleRegisAdmin(Message<Object> msg) {

        if (msg.body() instanceof JsonObject) {
            JsonObject jsonBodyClient = (JsonObject) msg.body();
            
        } else {
            msg.fail(400, "Bad Request: somthing wrong");
        }
    }
}
