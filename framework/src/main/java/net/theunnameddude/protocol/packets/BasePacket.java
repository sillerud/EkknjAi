package net.theunnameddude.protocol.packets;

import net.theunnameddude.handler.PacketHandler;
import net.theunnameddude.protocol.NetworkEngine;

import java.io.IOException;

public abstract class BasePacket {
    public abstract void read(NetworkEngine engine) throws IOException;

    public abstract void handle(PacketHandler handler) throws Exception;
}
