package events;

import org.jetbrains.annotations.NotNull;

public class CriticalExceptionEvent {

    public String msg;
    public Exception exception;

    public CriticalExceptionEvent(@NotNull String msg,@NotNull Exception exception){
        this.msg = msg;
        this.exception = exception;
    }
}
