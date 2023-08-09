package org.sample.dto;


import java.util.HashMap;
import java.util.Map;

public class Response {

    private long timestamp = System.currentTimeMillis();
    private Map<String,Object> result = new HashMap();

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
