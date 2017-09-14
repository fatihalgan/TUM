package mcel.tump.gateway.handler;

public abstract class AbstractTUMRequestHandler implements TUMRequestHandler {

	private TUMRequestHandler next;
	
	public TUMRequestHandler getNext() {
        return next;
    }

    public void setNext(TUMRequestHandler handler) {
        this.next = handler;
    }
}
