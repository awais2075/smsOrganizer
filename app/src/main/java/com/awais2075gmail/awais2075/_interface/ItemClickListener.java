package com.awais2075gmail.awais2075._interface;

/**
 * Created by Muhammad Awais Rashi on 04-Jan-18.
 */

public interface ItemClickListener {
    public void itemClicked(long smsId, long smsThreadId, String smsNumber, String smsAddress, String smsBody, String smsReadState, String smsDate, String smsType);

    void onClickListener(String id, int position);

    void onLongClickListener(String id, int position);


}
