package com.yaago.roo.addon.shiro;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;

/**
 * Sample of a command class. The command class is registered by the Roo shell following an
 * automatic classpath scan. You can provide simple user presentation-related logic in this
 * class. You can return any objects from each method, or use the logger directly if you'd
 * like to emit messages of different severity (and therefore different colours on 
 * non-Windows systems).
 * 
 * @since 1.1
 */
@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class ShiroCommands implements CommandMarker { // All command types must implement the CommandMarker interface
    
    /**
     * Get a reference to the ShiroOperations from the underlying OSGi container
     */
    @Reference private ShiroOperations operations;

    /**
     * This method registers a command with the Roo shell. It has no command attribute.
     * 
     */
    @CliCommand(value = "shiro setup", help = "Setup Shiro addon")
    public void setup() {
        operations.setup();
    }
}