package de.turidus.minecraft_mapmaker.events;

import org.jetbrains.annotations.NotNull;

public class NonCriticalExceptionEvent {

    public final String msg;
    public final Exception exception;

    public NonCriticalExceptionEvent(@NotNull String msg, @NotNull Exception exception){
        this.msg = msg;
        this.exception = exception;
    }
}
