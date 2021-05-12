package dev.kscott.bonk.bukkit.event;

/**
 * Represents an Event.
 */
public interface Event {

    /**
     * {@return true if this event is running, false if not}
     */
    boolean running();

    /**
     * {@return true if this event can run, false if not}
     */
    boolean canRun();

    /**
     * {@return how long this event will run for}
     */
    int duration();

    /**
     * Runs the event.
     */
    void run();

}
