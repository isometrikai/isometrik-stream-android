package io.isometrik.gs.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.ui.pk.response.PkInviteRelatedEvent;
import io.isometrik.gs.ui.pk.response.PkStopEvent;

public abstract class PkEventCallback {

    /**
     * pkInviteReceived
     */
    public abstract void pkInviteReceived(@NotNull Isometrik isometrik,
                                          @NotNull PkInviteRelatedEvent pkInviteRelatedEvent);

    public abstract void pkInviteStatus(@NotNull Isometrik isometrik,
                                          @NotNull PkInviteRelatedEvent pkInviteRelatedEvent);

    public abstract void pkStopForViewer(@NotNull Isometrik isometrik,
                                          @NotNull PkStopEvent pkStopEvent);

    public abstract void pkStopForPublisher(@NotNull Isometrik isometrik,
                                            @NotNull PkStopEvent pkStopEvent);
}
