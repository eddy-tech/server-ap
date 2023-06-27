package com.tech.server.services;

import com.tech.server.models.Server;

import java.io.IOException;
import java.util.Collection;

public interface ServerService {
    Server createServer(Server server);
    Server ping (String ipAddress) throws IOException;
    Collection<Server> serverList(int limit);
    Server getServer(Long serverId);
    Server updateServer(Server server);
    Boolean deleteServer(Long serverId);
}
