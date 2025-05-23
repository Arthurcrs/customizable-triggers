package core.trigger;

public interface ITrigger {
    /** Attach any event handlers you need to the Forge bus. */
    void register();

    /** Unregister (e.g. on resource reload) to avoid memory leaks. */
    void unregister();
}