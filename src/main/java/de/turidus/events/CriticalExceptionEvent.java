package de.turidus.events;

import org.jetbrains.annotations.NotNull;

public class CriticalExceptionEvent {

    public final String msg;
    public final Exception exception;

    public CriticalExceptionEvent(@NotNull String msg,@NotNull Exception exception){
        this.msg = msg;
        this.exception = exception;
    }
}
