package org.hyizhou.titaniumstation.ai.tools;

import org.springframework.ai.chat.messages.Message;

import java.util.LinkedList;
import java.util.List;

public class MessageHandleImp implements MessageHandle{
    private final List<Message> cache = new LinkedList<Message>();
    @Override
    public List<Message> append(Message message) {
        cache.add(message);
        return cache;
    }
}
