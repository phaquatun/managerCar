package Model.User.DataBase;

import FormatPojo.FormatRes;
import Model.User.ConstanceUser;
import Model.SetUpServer.ConstantManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoGridFsClient;

public class DAOUser extends AbstractVerticle {

    private MongoClient mongoClient;
    private MongoGridFsClient mongoGridFs;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        createMongoDb()
                .compose(this::createMongoGridFs)
                .compose(this::configEventBus)
                .result();

    }

    Future<Void> createMongoDb() {
        return Future.future((promise) -> {

            var query = new JsonObject().put("db_name", ConstantManager.Db_Name).put("connection_string", ConstantManager.Db_Uri);
            mongoClient = MongoClient.createShared(vertx, query).getCollections((asynResult) -> {
                if (asynResult.succeeded()) {
                    promise.complete();
                } else {
                    asynResult.cause().printStackTrace();
                    promise.fail("err create MongoDB" + asynResult.cause().getMessage());
                }
            });

        });
    }

    Future<Void> createMongoGridFs(Void unuse) {
        return Future.future((promise) -> {
            mongoClient.createGridFsBucketService(ConstanceUser.dbGridFs, (asynResult) -> {
                if (asynResult.succeeded()) {
                    mongoGridFs = asynResult.result();
                    promise.complete();
                }
                if (asynResult.failed()) {
                    asynResult.cause().printStackTrace();
                    promise.fail("err create MongoGridfs");
                }
            });

        });
    }

    Future<Void> configEventBus(Void unuse) {
        return Future.future((promise) -> {

            vertx.eventBus().consumer(ConstanceUser.registerUser, msg -> new HandleUserRegister(mongoClient, mongoGridFs).handleRegisterUser(msg));
            vertx.eventBus().consumer(ConstanceUser.upImage, msg -> new HandleUserUploadFile(mongoClient, mongoGridFs).handleUpImage(msg));
            vertx.eventBus().consumer(ConstanceUser.doawnImage, msg -> new HandleUserUploadFile(mongoClient, mongoGridFs).handleDownLoadImage(msg));

            promise.complete();
        });
    }

}
