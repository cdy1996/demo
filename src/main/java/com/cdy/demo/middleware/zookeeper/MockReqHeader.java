package com.cdy.demo.middleware.zookeeper;

import org.apache.jute.*;
import org.apache.zookeeper.server.ByteBufferInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * paxos到zk  P274
 */
public class MockReqHeader implements Record {
    private long sessionId;
    private String type;

    public MockReqHeader() {
    }

    public MockReqHeader(long sessionId, String type) {
        this.sessionId = sessionId;
        this.type = type;
    }

    @Override
    public void serialize(OutputArchive archive, String tag) throws IOException {
        archive.startRecord(this, tag);
        archive.writeLong(sessionId, "sessionId");
        archive.writeString(type, "type");
        archive.endRecord(this, tag);
    }

    @Override
    public void deserialize(InputArchive archive, String tag) throws IOException {
        archive.startRecord( tag);
        sessionId = archive.readLong("sessionId");
        type = archive.readString("type");
        archive.endRecord( tag);
    }


    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryOutputArchive archive = BinaryOutputArchive.getArchive(baos);
        new MockReqHeader(123456789L, "ping").serialize(archive,"header");

        ByteBuffer wrap = ByteBuffer.wrap(baos.toByteArray());

        ByteBufferInputStream bbis = new ByteBufferInputStream(wrap);
        BinaryInputArchive inputArchive = BinaryInputArchive.getArchive(bbis);
        MockReqHeader mockReqHeader = new MockReqHeader();
        mockReqHeader.deserialize(inputArchive, "header");
        bbis.close();
        baos.close();
    }
}
