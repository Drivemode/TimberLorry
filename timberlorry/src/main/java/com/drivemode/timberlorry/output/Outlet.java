package com.drivemode.timberlorry.output;

/**
 * Outlet connects to a backend service that collects logs.
 * @author KeithYokoma
 */
public interface Outlet {
    /**
     * Determine which type of payload does this Outlet accepts.
     * @param payload to be checked.
     * @return true if this Outlet can send the payload, false otherwise.
     */
    boolean accept(Class<?> payload);

    /**
     * Send the payload in the current thread.
     * If an error occurred while dispatching, you should catch it and put it in the result.
     * @param payload to be sent.
     * @return {@link Result#success()} if the payload is sent successfully, {@link Result#failure(Exception)} if something went wrong on sending the payload.
     */
    Result dispatch(String payload);

    String name();
}
