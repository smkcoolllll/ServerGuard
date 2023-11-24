package io.getarrays.server.enumeration;

/**
 * An enumeration representing the status of a server.
 * This enum defines two status values: SERVER_UP and SERVER_DOWN.
 * Each status value corresponds to a specific server state.
 * 
 */

public enum Status {

    // Represents a server that is up and running
    SERVER_UP("SERVER_UP"),

    // Represents a server that is not responding or down
    SERVER_DOWN("SERVER_DOWN");

    private final String status;

    /**
     * Constructor for the Status enum.
     * Each enum value is associated with a specific status string.
     *
     * @param status - The status string for the enum value.
     */
    Status(String status) {
        this.status = status;
    }

    /**
     * Gets the status string for the enum value.
     *
     * @return String - The status string for the enum value.
     */
    public String getStatus() {
        return this.status;
    }
}