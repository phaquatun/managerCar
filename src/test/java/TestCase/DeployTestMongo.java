package TestCase;

import TestMongo.TestMongo;
import io.vertx.core.Vertx;

public class DeployTestMongo {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new TestMongo());
    }
}
