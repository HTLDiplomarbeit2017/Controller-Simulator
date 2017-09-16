package at.htl;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ClientNetworking {
	private String server;
	private int port = 25920;
	private DatagramSocket socket;
	private Executor sender;
	private int timeOut=100000;
	public ClientNetworking(String server) {
		this.server=server;
		sender=Executors.newCachedThreadPool();
		try {
			SocketAddress address=new InetSocketAddress(port+(int)(Math.random()*4000));
			socket = new DatagramSocket(address);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	
	public void send(String s,ClientReciever recv){
		send(toByte(s),recv);
	}
	public void send(byte[] buf,ClientReciever recv){
		sender.execute(()->{
			try {
				InetAddress address;
				for (byte b : buf) {
				    System.out.print(Integer.toBinaryString(b & 255 | 256).substring(1));
				}
				System.out.println();
				address = InetAddress.getByName(server);
				DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 25920);
				socket.send(packet);
				byte[] recv_buffer=new byte[128];
				DatagramPacket recv_packet=new DatagramPacket(recv_buffer, recv_buffer.length);
				recv_packet.setAddress(address);
				socket.receive(recv_packet);
				recv.recievedString(new String(recv_packet.getData(),0,recv_packet.getLength(),StandardCharsets.US_ASCII));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	private byte[] toByte(String s) {
		byte[] b_arr = s.getBytes();
		return b_arr;
	}
}
