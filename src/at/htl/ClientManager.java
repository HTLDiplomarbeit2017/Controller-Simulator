package at.htl;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClientManager implements ClientReciever, InputStateChangeListener {
	private ClientNetworking network;
	private String server;
	private ScheduledExecutorService info_sender;
	private int impulseRateNone = 166;

	private int impulseRatePressed = 16;
	private Timer timeOut;

	public enum ClientState {
		IDLE, LOGIN, PASSWORD_REQ, SERVER_FULL, LOGGED_IN, CONNECTION_LOST
	}

	public ClientState state = ClientState.IDLE;
	private List<ClientListener> listeners = new ArrayList<ClientListener>();
	public final InputState inputState;
	private ScheduledFuture<?> futureSender;
	private int timeOutDelay = 5000;
	private Runnable send_State = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			byte[] x_data = inputState.getSendableChars();
			byte[] data = { (byte) 'x', x_data[1], x_data[0] };
			network.send(data, ClientManager.this);
		}
	};

	public ClientManager() {
		inputState = new InputState();
		inputState.addInputStateChangeListener(this);
		this.timeOut = new Timer();
	}

	public void addListener(ClientListener listener) {
		this.listeners.add(listener);
		setState(state);
	}

	public void login(String server, String name) {
		this.server = server;

		network = new ClientNetworking(server);
		network.send("login " + name, this);
		setState(ClientState.LOGIN);
		resetTimer();
	}

	@Override
	public void recievedString(String s) {
		resetTimer();
		// TODO Auto-generated method stub
		if (s.charAt(0) == ASCII_Control.ACK.code) {
			return;
		}
		switch (s) {
		case "pass req":
			setState(ClientState.PASSWORD_REQ);
			for (ClientListener clientListener : listeners) {
				clientListener.passwordRequired(this);
			}
			break;
		case "login successful":
			setState(ClientState.LOGGED_IN);
			startActivity();
			break;
		case "server full":
			setState(ClientState.SERVER_FULL);
			break;
		default:
			break;
		}
	}

	private void setState(ClientState state) {
		this.state = state;
		for (ClientListener clientListener : listeners) {
			clientListener.stateChanged(this, state);
		}
	}

	private void resetTimer() {
		timeOut.cancel();
		timeOut = new Timer();
		timeOut.schedule(new TimerTask() {

			@Override
			public void run() {

				setState(ClientState.CONNECTION_LOST);
				// TODO Auto-generated method stub
				if (futureSender != null) {
					futureSender.cancel(false);
				}
				if(info_sender!=null){
					info_sender.shutdown();
					info_sender = null;
				}
				for (ClientListener clientListener : listeners) {
					if (clientListener != null)
						clientListener.timedOut(ClientManager.this);
				}

			}
		}, timeOutDelay);
	}

	private void startActivity() {
		info_sender = Executors.newScheduledThreadPool(60);

		futureSender = info_sender.scheduleAtFixedRate(send_State, 0,
				inputState.isAnyDown() ? impulseRatePressed : impulseRateNone, TimeUnit.MILLISECONDS);
	}

	@Override
	public void inputChanged(InputState now) {
		// TODO Auto-generated method stub
		if (futureSender != null) {
			futureSender.cancel(false);
		}
		if (info_sender != null) {
			futureSender = info_sender.scheduleAtFixedRate(send_State, 0,
					inputState.isAnyDown() ? impulseRatePressed : impulseRateNone, TimeUnit.MILLISECONDS);
		}
	}
}
