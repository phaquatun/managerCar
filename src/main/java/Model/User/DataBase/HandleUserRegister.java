package Model.User.DataBase;

import FormatPojo.FormatRes;
import Model.SetUpServer.ConstantManager;
import Model.User.ConstanceUser;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoGridFsClient;
import io.vertx.ext.web.FileUpload;
import java.util.Set;

public class HandleUserRegister {

    private MongoClient mongoClient;
    private MongoGridFsClient mongoGridFs;

    public HandleUserRegister(MongoClient mongoClient, MongoGridFsClient mongoGridFs) {
        this.mongoClient = mongoClient;
        this.mongoGridFs = mongoGridFs;
    }

    public HandleUserRegister() {
    }

    public void handleRegisterUser(Message<Object> msg) {

        var msgBody = msg.body();
        if (msgBody instanceof JsonObject) {

            var strMsg = (String) msgBody.toString().replace("z4X2lk", "_id");
            var jsonBodyClient = new JsonObject(strMsg);
            var query = new JsonObject().put("_id", jsonBodyClient.getString("_id"));

            mongoClient.findOne(ConstanceUser.Collections_User, query, null)
                    .compose((jsonFind) -> this.saveInfoUser(jsonFind, jsonBodyClient, msg)).result();

        }
    }

    /*
    *** handle evb
     */
    Future<String> saveInfoUser(JsonObject jsonFind, JsonObject jsonBodyClient, Message<Object> msg) {
        return Future.future((promise) -> {
            if (jsonFind == null) {
                msg.reply(new FormatRes(null, "tạo thành tk công", true).toString());
                mongoClient.save(ConstanceUser.Collections_User, jsonBodyClient).result();
            } else {
                msg.reply(new FormatRes("email khoản đã tồn tại", "vui lòng chọn email khác", false).toString());
            }
            promise.complete();
        });
    }

}
