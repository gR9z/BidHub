package fr.eni.tp.auctionapp.exceptions;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private List<String> keys;

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public void addKey(String key) {
        if (keys == null) {
            keys = new ArrayList<String>();
        }
        keys.add(key);
    }

    public List<String> getKeys() {
        return keys;
    }
}
