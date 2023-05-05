
package TestMongo;

import Model.SetUpServer.ConstantManager;
import Model.User.ConstanceUser;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoGridFsClient;

public class TestMongo extends AbstractVerticle {
    
    private MongoClient mongoClient;
    
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        createMongoDb()
                .compose(this::createGridFs)
                .compose(this::uploadImage)
                .compose(this::doawloadFile)
                .result();
    }
    
    Future<MongoClient> createMongoDb() {
        return Future.future((promise) -> {
            MongoClient mongoClient;
            
            var query = new JsonObject().put("db_name", ConstantManager.Db_Name).put("connection_string", ConstantManager.Db_Uri);
            mongoClient = MongoClient.createShared(vertx, query);
            
            promise.complete(mongoClient);
        });
    }
    
    Future<MongoGridFsClient> createGridFs(MongoClient mongoClient) {
        return Future.future((e) -> {
            mongoClient.createGridFsBucketService(ConstanceUser.dbGridFs, (asynRes) -> {
                if (asynRes.succeeded()) {
                    MongoGridFsClient mongoGridFsClient = asynRes.result();
                    e.complete(mongoGridFsClient);
                } else {
                    e.fail("err create MongoGridFsClient");
                    e.complete(null);
                }
                
            });
        });
    }
    
    Future<MongoGridFsClient> doawloadFile(MongoGridFsClient mongoGridFs) {
        return Future.future((promoise) -> {
            mongoGridFs.downloadFile("test.er", (asyncResult) -> {
                if(asyncResult.succeeded()){
                    long lent = asyncResult.result();
                    System.out.println(lent);
                }
                if(asyncResult.failed()){
                    asyncResult.cause().printStackTrace();
                }
            });
            
//            mongoGridFs.downloadFileByID("test.er", "changer" ,(asyncResult) -> {
//                if(asyncResult.succeeded()){
//                    long lent = asyncResult.result();
//                    System.out.println(lent);
//                }
//                if(asyncResult.failed()){
//                    asyncResult.cause().printStackTrace();
//                }
//            });
            
            promoise.complete(mongoGridFs);
        });
    }
    
    Future<MongoGridFsClient> uploadImage(MongoGridFsClient mongoGridFs) {
        return Future.future((promise) -> {
             futFile().compose(aysnFile -> futUpload(aysnFile, mongoGridFs)).result();

//            mongoGridFs.uploadByFileName(asynFile, "test.er").onComplete((asynRes) -> {
//                if (asynRes.succeeded()) {
//                    System.out.println("success");
//                }
//                if (asynRes.failed()) {
//                    asynRes.cause().printStackTrace();
//                }
//            });
//            mongoGridFs.uploadFile("D:\\netbean12.0\\FaceBook\\DebtForgiveness\\Image\\buttonStartShare.png", res -> {
//                        if (res.succeeded()) {
//                            String id = res.result();
//                            //The ID of the stored object in Grid FS
//                        } else {
//                            res.cause().printStackTrace();
//                        }
//                    });
            promise.complete(mongoGridFs);
        });
        
    }
    
    Future<AsyncFile> futFile() {
        return vertx.fileSystem().open("D:\\netbean12.0\\FaceBook\\DebtForgiveness\\Image\\buttonStartShare.png", new OpenOptions());
    }
    
    Future<String> futUpload (AsyncFile asyncFile,MongoGridFsClient mongoGridFs){
        return mongoGridFs.uploadByFileName(asyncFile, "checker.er");
    }
    
}
