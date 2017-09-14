package mcel.tump.gateway.handler;

import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;

public interface TUMRequestHandler {
	public void processRequest(TUMPRequest request, TUMPResponse response) throws TUMRequestHandlerException;
    public TUMRequestHandler getNext();
    public void setNext(TUMRequestHandler handler);
}
