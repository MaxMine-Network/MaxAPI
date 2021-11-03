package net.maxmine.api.coreclient.callback;

import net.maxmine.api.coreclient.packets.Packet;

public interface CoreCallback {

    void done(Packet packet);
}
