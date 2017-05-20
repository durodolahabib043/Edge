package com.durodola.edge.Listerners;

/**
 * Created by habib.durodola on 2017-05-19.
 */

public class MyCustomObject {
    private MyCustomObjectListener listener;

    public MyCustomObject() {
        // set null or default listener or accept as argument to constructor
        this.listener = null;
    }

    // Assign the listener implementing events interface that will receive the events
    void setCustomObjectListener(MyCustomObjectListener listener) {
        this.listener = listener;
    }

    public  interface MyCustomObjectListener {
       void onObjectReady(String title);

    }
}
