package net.rickiekarp.homeassistant.net.communication.vo;

/**
 * Created by sebastian on 15.11.17.
 */

public class Change {

    String subject;

    public String getText() {
        return subject;
    }

    public void setText(String subject) {
        this.subject = subject;
    }
}
