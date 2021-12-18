package com.bob.bobapp.listener;

public interface OnItemDeleteListener
{
    void onItemDeleteListener(String id,int position,String name);
    void onSipItemDeleteListener(String id,int position,String name);
    void onRedeemItemDeleteListener(String id,int position,String Amount);
    void onSwitchItemDeleteListener(String id,int position,String Amount);
    void onSwPItemDeleteListener(String id,int position,String Amount);
    void onSTpItemDeleteListener(String id,int position,String Amount);

}
