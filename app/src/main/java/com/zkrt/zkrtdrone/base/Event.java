package com.zkrt.zkrtdrone.base;

/**
 * Created by jack_xie on 16-12-22.
 */

public class Event {
    public String text = "BeanA";
    public Event() {

    }

    public Event(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "EventA{" +
                "text='" + text + '\'' +
                '}';
    }
}
