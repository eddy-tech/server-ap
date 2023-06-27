package com.tech.server.services.impl;

import com.tech.server.enums.Status;
import com.tech.server.models.Server;
import com.tech.server.repositories.ServerRepository;
import com.tech.server.services.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ServerServiceImpl implements ServerService {
    private final ServerRepository serverRepository;

    @Override
    public Server createServer(Server server) {
        log.info("Creating new server : {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepository.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server ip : {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address =  InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepository.save(server);

        return server;
    }

    @Override
    public Collection<Server> serverList(int limit) {
        log.info("Fetching all servers...");
        return serverRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server getServer(Long serverId) {
        log.info("Fetching server by id: {}", serverId);
        return serverRepository.findById(serverId).get();
    }

    @Override
    public Server updateServer(Server server) {
        log.info("Update existing server : {}", server.getName());
        return serverRepository.save(server);
    }

    @Override
    public Boolean deleteServer(Long serverId) {
        log.info("Delete server by id: {}", serverId);
         serverRepository.deleteById(serverId);
        return true;
    }

    private String setServerImageUrl() {
        String [] imageNames = {"server1.png", "server2.jpg", "server3.jpg", "server4.png"};
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/server/image/" + imageNames[new Random().nextInt(4)]).toUriString();
    }
}
