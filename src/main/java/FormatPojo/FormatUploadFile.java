package FormatPojo;

import io.vertx.core.file.AsyncFile;
import io.vertx.core.json.JsonObject;

public class FormatUploadFile {

    private String nameUser;
    private AsyncFile asyncFile;
    private String nameFile;

    public FormatUploadFile(String nameUser, String nameFile, AsyncFile asyncFile) {
        this.nameUser = nameUser;
        this.asyncFile = asyncFile;
        this.nameFile = nameFile;
    }

    public String getNameUser() {
        return nameUser;
    }

    public AsyncFile getAsyncFile() {
        return asyncFile;
    }

    public String getNameFile() {
        return nameFile;
    }

    
}
