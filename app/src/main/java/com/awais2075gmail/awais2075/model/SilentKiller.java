package com.awais2075gmail.awais2075.model;

import java.io.Serializable;

public class SilentKiller implements Serializable {
    private String evacuationCode;
    private boolean isEnabled;

    public SilentKiller(String evacuationCode, boolean isEnabled) {
        this.evacuationCode = evacuationCode;
        this.isEnabled = isEnabled;
    }

    public String getEvacuationCode() {
        return evacuationCode;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }
}
