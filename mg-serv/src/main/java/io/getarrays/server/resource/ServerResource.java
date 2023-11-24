package io.getarrays.server.resource;

import io.getarrays.server.model.Response;
import io.getarrays.server.model.Server;
import io.getarrays.server.service.implementation.ServerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static io.getarrays.server.enumeration.Status.SERVER_UP;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {
    private final ServerServiceImpl serverService;

     /**
     * Endpoint to get a list of servers.
     * This method retrieves the list of servers from the serverService and returns it in the response.
     * It introduces an artificial delay of 3 seconds to simulate slow server response.
     *
     * @return ResponseEntity<Response> - JSON response containing the list of servers.
     * @throws InterruptedException if the thread is interrupted during the artificial delay.
     */

    @GetMapping("/list")
    public ResponseEntity<Response> getServers() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("servers", serverService.list(30)))
                        .message("Servers retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

      /**
     * Endpoint to ping a server by its IP address.
     * This method pings the server using the ipAddress provided, and the result is returned in the response.
     *
     * @param ipAddress - The IP address of the server to ping.
     * @return ResponseEntity<Response> - JSON response containing the ping result and server details.
     * @throws IOException if there's an issue during the ping operation.
     */

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = serverService.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("server", server))
                        .message(server.getStatus() == SERVER_UP ? "Ping success" : "Ping failed")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    /**
     * Endpoint to save a new server.
     * This method saves the provided server object using the serverService and returns the created server in the response.
     *
     * @param server - The Server object to be created.
     * @return ResponseEntity<Response> - JSON response containing the created server details.
     */

    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @jakarta.validation.Valid Server server) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("server", serverService.create(server)))
                        .message("Server created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }


     /**
     * Endpoint to get details of a specific server by its ID.
     * This method retrieves the server details based on the provided ID and returns them in the response.
     *
     * @param id - The ID of the server to retrieve.
     * @return ResponseEntity<Response> - JSON response containing the server details.
     */



    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("server", serverService.get(id)))
                        .message("Server retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


    /**
     * Endpoint to delete a server by its ID.
     * This method deletes the server based on the provided ID and returns the deletion status in the response.
     *
     * @param id - The ID of the server to delete.
     * @return ResponseEntity<Response> - JSON response containing the deletion status.
     */


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("deleted", serverService.delete(id)))
                        .message("Server deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


     /**
     * Endpoint to get the image of a server by its filename.
     * This method reads the server image from a specific location and returns it as a byte array in the response.
     *
     * @param fileName - The filename of the server image to retrieve.
     * @return byte[] - The server image data as a byte array.
     * @throws IOException if there's an issue reading the server image file.
     */

    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/images/" + fileName));
    }
}
