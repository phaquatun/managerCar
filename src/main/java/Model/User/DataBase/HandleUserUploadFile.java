package Model.User.DataBase;


import io.vertx.core.eventbus.Message;
import io.vertx.core.file.AsyncFile;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoGridFsClient;

public class HandleUserUploadFile {

    private MongoClient mongoClient;
    private MongoGridFsClient mongoGridFs;

    public HandleUserUploadFile(MongoClient mongoClient, MongoGridFsClient mongoGridFs) {
        this.mongoClient = mongoClient;
        this.mongoGridFs = mongoGridFs;
    }

    public void handleUpImage(Message<Object> msg) {

        AsyncFile asynFile = (AsyncFile) msg.body();
        System.out.println(">> check asyncFile " + (asynFile != null) + " - mongoGridFs " + (mongoGridFs != null));

        mongoGridFs.uploadByFileName(asynFile, "change.er", (asynRes) -> {
            if (asynRes.succeeded()) {
                msg.reply("success id : " + asynRes.result());
            }
            if (asynRes.failed()) {
                asynRes.cause().printStackTrace();
                msg.reply("failed " + asynRes.cause().getMessage());
            }
        });
    }

    public void handleDownLoadImage(Message<Object> msg) {

    }

}
