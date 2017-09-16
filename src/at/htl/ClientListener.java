package at.htl;

import at.htl.ClientManager.ClientState;

public interface ClientListener {

	public void stateChanged(ClientManager sender, ClientState newState);
	public void passwordRequired(ClientManager sender);
	public void timedOut(ClientManager sender);
}
