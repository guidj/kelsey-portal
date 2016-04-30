package org.dsc.diseametry.data;

public interface IStreamReader {

	public Document next();
	
	public boolean isClosed();
}
