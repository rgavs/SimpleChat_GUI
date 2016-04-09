package server;

import ocsf.server.ConnectionToClient;

import java.util.*;


class ChannelManager {
    private final Map<String, Set<ConnectionToClient>> channelMap;

    ChannelManager() {
        channelMap = new HashMap<>();
    }

    boolean channelExists(String channel) {
        return channelMap.containsKey(channel);
    }

    void createChannel(String channel) {
        channelMap.put(channel, new HashSet<>());
    }

    void joinChannel(String channel, ConnectionToClient client) {
        if (channelMap.containsKey(channel)) {
            channelMap.get(channel).add(client);
        } else {
            throw new NoSuchElementException("No channel " + channel);
        }
    }

    void removeFromChannel(String channel, ConnectionToClient client) {
        if (channelMap.containsKey(channel)) {
            channelMap.get(channel).remove(client);
        } else {
            throw new NoSuchElementException("No channel " + channel);
        }
    }

    boolean isEmptyChannel(String channel) {
        return channelMap.get(channel).isEmpty();
    }

    void removeChannel(String channel) {
        channelMap.remove(channel);
    }

    Set<ConnectionToClient> getChannelClients(String channel) {
        return channelMap.get(channel);
    }

    Set<String> getChannelNames() {
        return channelMap.keySet();
    }
}

