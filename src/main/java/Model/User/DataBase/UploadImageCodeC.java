package Model.User.DataBase;

import FormatPojo.FormatUploadFile;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.json.JsonObject;

public class UploadImageCodeC implements MessageCodec<FormatUploadFile, FormatUploadFile> {

    @Override
    public void encodeToWire(Buffer buffer, FormatUploadFile formUploadFile) {
        JsonObject jsonToEncode = new JsonObject();
        jsonToEncode.put("_id", formUploadFile.getNameUser())
                .put("fileContent", formUploadFile.getAsyncFile())
                .put("nameFile", formUploadFile.getNameFile());

        String jsonToStr = jsonToEncode.encode();

        int length = jsonToStr.getBytes().length;

        buffer.appendInt(length).appendString(jsonToStr);

    }

    @Override
    public FormatUploadFile decodeFromWire(int position, Buffer buffer) {
        int _pos = position;

        // Length of JSON
        int length = buffer.getInt(_pos);

        // Get JSON string by it`s length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos += 4, _pos += length);
        JsonObject contentJson = new JsonObject(jsonStr);

        String userName = contentJson.getString("_id");
        String nameFile = contentJson.getString("nameFile");
        AsyncFile asyncFile = (AsyncFile) contentJson.getValue("fileContent");

        return new FormatUploadFile(userName, nameFile, asyncFile);

    }

    @Override
    public FormatUploadFile transform(FormatUploadFile s) {

        return s;
    }

    @Override
    public String name() {

        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        // Always -1
        return -1;
    }

}
