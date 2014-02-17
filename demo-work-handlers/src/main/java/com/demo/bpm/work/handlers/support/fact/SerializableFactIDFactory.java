package com.demo.bpm.work.handlers.support.fact;

import java.io.Serializable;

public class SerializableFactIDFactory
    implements FactIDFactory,
               Serializable
{
    private Long nextFactId =0L;

    public synchronized Long getNextFactID(String key)
    {
        return ++nextFactId;
    }
}
