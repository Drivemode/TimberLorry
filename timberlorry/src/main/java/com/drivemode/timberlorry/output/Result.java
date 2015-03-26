package com.drivemode.timberlorry.output;

/**
 * @author KeithYokoma
 */
public class Result {
    private final boolean success;

    public Result(boolean success) {
        this.success = success;
    }

    public static Result success() {
        return new Result(true);
    }

    public static Result failure() {
        return new Result(false);
    }

    public boolean isSuccess() {
        return success;
    }
}
