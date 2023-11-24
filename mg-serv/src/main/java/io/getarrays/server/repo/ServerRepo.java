package io.getarrays.server.repo;

import io.getarrays.server.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServerRepo extends JpaRepository<Server, Long> {

     /**
     * Finds a server entity by its IP address.
     * This method retrieves the server from the database based on the provided IP address.
     *
     * @param ipAddress - The IP address of the server to find.
     * @return Server - The server entity with the specified IP address.
     */
    
    Server findByIpAddress(String ipAddress);
}
