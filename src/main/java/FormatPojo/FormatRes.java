

package FormatPojo;

import io.vertx.core.json.JsonObject;



public class FormatRes {
    
    private String valErr , des ;
    private boolean success ;

    public FormatRes(String valErr, String des, boolean success) {
        this.valErr = valErr;
        this.des = des;
        this.success = success;
    }

    @Override
    public String toString() {
        return new JsonObject().put("success", success).put("err", valErr).put("des", des).toString();
    }
    
   
    
}
