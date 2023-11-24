package io.getarrays.server.service.implementation;

import io.getarrays.server.model.Server;
import io.getarrays.server.repo.ServerRepo;
import io.getarrays.server.service.ServerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.Random;

import static io.getarrays.server.enumeration.Status.SERVER_DOWN;
import static io.getarrays.server.enumeration.Status.SERVER_UP;
import static java.lang.Boolean.TRUE;
import static org.springframework.data.domain.PageRequest.of;



@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {
    private final ServerRepo serverRepo;

    /**
     * Creates a new server entity in the database.
     * This method saves the provided server object, sets the server's image URL, and returns the created server.
     *
     * @param server - The Server object to be created.
     * @return Server - The created server object with the generated image URL.
     */

    @Override
    public Server create(Server server) {
        log.info("Saving new server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepo.save(server);
    }

     /**
     * Pings a server with the provided IP address.
     * This method checks the server's reachability using the ipAddress and updates the server's status accordingly.
     *
     * @param ipAddress - The IP address of the server to ping.
     * @return Server - The updated server object with the ping result (status).
     * @throws IOException if there's an issue during the ping operation.
     */

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = serverRepo.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? SERVER_UP : SERVER_DOWN);
        serverRepo.save(server);
        return server;
    }

     /**
     * Retrieves a collection of servers up to the specified limit.
     * This method fetches a limited number of servers from the database and returns them as a collection.
     *
     * @param limit - The maximum number of servers to retrieve.
     * @return Collection<Server> - A collection of servers up to the specified limit.
     */

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return serverRepo.findAll(of(0, limit)).toList();
    }


    /**
     * Retrieves a server by its ID.
     * This method retrieves the server details based on the provided ID and returns the server object.
     *
     * @param id - The ID of the server to retrieve.
     * @return Server - The server object with the specified ID.
     */

    @Override
    public Server get(Long id) {
        log.info("Fetching server by id: {}", id);
        return serverRepo.findById(id).get();
    }


     /**
     * Updates an existing server entity in the database.
     * This method saves the provided server object and returns the updated server.
     *
     * @param server - The Server object to be updated.
     * @return Server - The updated server object.
     */

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return serverRepo.save(server);
    }


    /**
     * Deletes a server by its ID.
     * This method deletes the server based on the provided ID and returns a boolean indicating the deletion status.
     *
     * @param id - The ID of the server to delete.
     * @return Boolean - True if the server is successfully deleted, false otherwise.
     */

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server by ID: {}", id);
        serverRepo.deleteById(id);
        return TRUE;
    }


    /**
     * Generates a random server image URL.
     * This method creates a random image URL for new servers using a list of predefined image names.
     *
     * @return String - The generated server image URL.
     */

    private String setServerImageUrl() {
        String[] imageNames = {"mgsrv1.png", "mgsrv2.png", "mgsrv3.png", "mgsrv4.png", "mgsrv5.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/" + imageNames[new Random().nextInt(4)]).toUriString();
    }


    /**
     * Checks if a server is reachable at a specific IP address and port.
     * This method attempts to establish a socket connection to the server within a specified timeout period.
     *
     * @param ipAddress - The IP address of the server to check.
     * @param port - The port number to connect to on the server.
     * @param timeOut - The maximum time to wait for the server to respond (in milliseconds).
     * @return boolean - True if the server is reachable, false otherwise.
     */

    private boolean isReachable(String ipAddress, int port, int timeOut) {
        try {
            try(Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ipAddress, port), timeOut);
            }
            return true;
        }catch (IOException exception){
            return false;
        }
    }
}
