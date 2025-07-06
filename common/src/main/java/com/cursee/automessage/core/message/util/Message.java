package com.cursee.automessage.core.message.util;

public class Message {

    public String identifier;

    public MessageSchedule schedule;
    public MessageType type;

//    public int delay;
    public boolean repeats;
    public boolean pack_intro;

    public String link;
    public String text;

    public static Message defaultFallback() {

        Message message = new Message();

        message.identifier = "default_message";

        message.schedule = MessageSchedule.ON_FIRST_JOIN;
        message.type = MessageType.CHAT;

//        message.delay = 5;
        message.repeats = true;
        message.pack_intro = false;

        message.link = "https://www.google.com";
        message.text = "Default (with link): Welcome to AutoMessage, %player%!";

        return message;
    }
}
