
package com.smartinventory.exception;

/**
 *
 * @author justi
 */

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}