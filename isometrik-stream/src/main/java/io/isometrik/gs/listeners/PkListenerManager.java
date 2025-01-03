package io.isometrik.gs.listeners;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.PkEventCallback;
import io.isometrik.gs.ui.pk.response.PkInviteRelatedEvent;
import io.isometrik.gs.ui.pk.response.PkStopEvent;

/**
 * The class containing methods to add or remove listeners for the PkEventCallback{@link
 * PkEventCallback } and to
 * pk invite received {@link PkInviteRelatedEvent},
 * listeners.
 *
 * @see PkInviteRelatedEvent
 */
public class PkListenerManager {

    private final List<PkEventCallback> listeners;
    private final Isometrik isometrik;

    /**
     * Instantiates a new  listener manager.
     *
     * @param isometrikInstance the isometrik instance
     * @see Isometrik
     */
    public PkListenerManager(Isometrik isometrikInstance) {
        this.listeners = new ArrayList<>();
        this.isometrik = isometrikInstance;
    }

    /**
     * Add listener.
     *
     * @param listener the pkEventCallback{@link PkEventCallback}
     *                 listener to be added
     * @see PkEventCallback
     */
    public void addListener(PkEventCallback listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Remove listener.
     *
     * @param listener the PkEventCallback{@link PkEventCallback}
     *                 listener to be removed
     * @see PkEventCallback
     */
    public void removeListener(PkEventCallback listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    /**
     * @return list of PkEventCallback{@link PkEventCallback}
     * listeners currently registered
     * @see PkEventCallback
     */
    private List<PkEventCallback> getListeners() {
        List<PkEventCallback> tempCallbackList;
        synchronized (listeners) {
            tempCallbackList = new ArrayList<>(listeners);
        }
        return tempCallbackList;
    }

    /**
     * announce a PkInviteReceived to listeners.
     *
     * @param pkInviteRelatedEvent pkInviteRelated{@link PkInviteRelatedEvent}
     *                             which will be broadcast to listeners.
     * @see PkInviteRelatedEvent
     */
    public void announce(PkInviteRelatedEvent pkInviteRelatedEvent) {
        for (PkEventCallback viewerEventCallback : getListeners()) {
            if (pkInviteRelatedEvent.getMessageType() == 4) {  // pkInvite
                Log.e("PK:", "PK invite received");
                viewerEventCallback.pkInviteReceived(this.isometrik, pkInviteRelatedEvent);
            } else if (pkInviteRelatedEvent.getMessageType() == 1) {  // pkInviteStatus
                Log.e("PK:", "PK invite status");
                viewerEventCallback.pkInviteStatus(this.isometrik, pkInviteRelatedEvent);
            } else {
            }
        }
    }

    public void announce(PkStopEvent pkStopEvent, boolean forViewer) {
        Log.e("PK:", "PK stop viewer");
        if (forViewer) {
            for (PkEventCallback viewerEventCallback : getListeners()) {
                viewerEventCallback.pkStopForViewer(this.isometrik, pkStopEvent);
            }
        } else {
            for (PkEventCallback viewerEventCallback : getListeners()) {
                viewerEventCallback.pkStopForPublisher(this.isometrik, pkStopEvent);
            }
        }

    }

}
