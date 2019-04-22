package events;

import org.jetbrains.annotations.NotNull;

public class NonCriticalExceptionEvent {

    public String msg;
    public Exception exception;

    public NonCriticalExceptionEvent(@NotNull String msg, @NotNull Exception exception){
        this.msg = msg;
        this.exception = exception;
    }
}
