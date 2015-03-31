package com.drivemode.timberlorry.output;

/**
 * This class represents collection result.
 * @author KeithYokoma
 */
public class Result {
    private final boolean success;
    private final Exception exception;

    public Result(boolean success, Exception exception) {
        this.success = success;
        this.exception = exception;
    }

    /**
     * Factory method that creates {@link Result} object describing success.
     * @return the success result.
     */
    public static Result success() {
        return new Result(true, null);
    }

    /**
     * Factory method that creates {@link Result} object describing failure for some reason.
     * @param e the exception causing the dispatch error.
     * @return the failure result.
     */
    public static Result failure(Exception e) {
        return new Result(false, e);
    }

    /**
     * @return true if success, false otherwise.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return exception causing the dispatch error.
     */
    public Exception getException() {
        return exception;
    }
}
