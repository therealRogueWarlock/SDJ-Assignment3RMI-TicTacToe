package shared.transferobjects;

import java.io.Serializable;

public class ServerData implements Serializable {

    private String type;
    private Object arg;
    private Object arg2;

    public ServerData(String type, Object arg) {
        this.type = type;
        this.arg = arg;
    }

    public void setArg2(Object arg2) {
        this.arg2 = arg2;
    }

    public String getType() {
        return type;
    }

    public Object getArg() {
        return arg;
    }

    public Object getArg2() {
        return arg2;
    }

}
