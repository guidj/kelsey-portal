package org.dsc.kelsey.data;

public interface IStreamReader {

    public Document next();

    public boolean isClosed();
}
