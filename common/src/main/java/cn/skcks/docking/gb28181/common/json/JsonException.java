package cn.skcks.docking.gb28181.common.json;

import lombok.Builder;

public class JsonException extends Exception{
    public JsonException(String message){
        super(message);
    }
}
