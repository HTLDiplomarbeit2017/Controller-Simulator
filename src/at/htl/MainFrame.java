package at.htl;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import at.htl.ClientManager.ClientState;
import at.htl.InputState.XBOX_Button;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;

public class MainFrame extends JFrame implements ClientListener {

	private JPanel contentPane;
	private JTextField textFieldIP;
	private ClientManager client;
	private JLabel lblState;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// InputState testState=new InputState();
				// //testState.xBOX_Down(XBOX_Button.A);
				//
				// testState.xBOX_Down(XBOX_Button.STICK_L);
				// byte[] chars=testState.getSendableChars();
				// System.out.println(chars[0]+"/"+chars[1]);
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 560, 392);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		textFieldIP = new JTextField();
		textFieldIP.setText("192.168.2.9");
		textFieldIP.setColumns(10);

		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client = new ClientManager();
				client.addListener(MainFrame.this);
				client.login(textFieldIP.getText(), "Player 1");
			}
		});

		JButton btnNewButton = new JButton("XBOX_A");

		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (client != null)
					client.inputState.xBOX_Down(XBOX_Button.A);

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (client != null)
					client.inputState.xBOX_Up(XBOX_Button.A);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				if (client != null)
					client.inputState.xBOX_Up(XBOX_Button.A);
			}
		});

		JButton btnXboxup = new JButton("XBOX_UP");
		btnXboxup.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (client != null)
					client.inputState.xBOX_Down(XBOX_Button.D_UP);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (client != null)
					client.inputState.xBOX_Up(XBOX_Button.D_UP);
				System.out.println("Mouse released");
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (client != null)
					client.inputState.xBOX_Up(XBOX_Button.D_UP);
			}
		});

		JPanel panelInput = new JPanel();
		panelInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
				case KeyEvent.VK_UP:
					if (client != null)
						client.inputState.xBOX_Down(XBOX_Button.D_UP);
					break;
				case KeyEvent.VK_DOWN:
					if (client != null)
						client.inputState.xBOX_Down(XBOX_Button.D_DOWN);
					break;
				case KeyEvent.VK_LEFT:
					if (client != null)
						client.inputState.xBOX_Down(XBOX_Button.D_LEFT);
					break;
				case KeyEvent.VK_RIGHT:
					if (client != null)
						client.inputState.xBOX_Down(XBOX_Button.D_RIGHT);
					break;
				case KeyEvent.VK_A:
					System.out.println("a down");
					if (client != null)
						client.inputState.xBOX_Down(XBOX_Button.A);
					break;
				default:
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					if (client != null)
						client.inputState.xBOX_Up(XBOX_Button.D_UP);
					break;
				case KeyEvent.VK_DOWN:
					if (client != null)
						client.inputState.xBOX_Up(XBOX_Button.D_DOWN);
					break;
				case KeyEvent.VK_LEFT:
					if (client != null)
						client.inputState.xBOX_Up(XBOX_Button.D_LEFT);
					break;
				case KeyEvent.VK_RIGHT:
					if (client != null)
						client.inputState.xBOX_Up(XBOX_Button.D_RIGHT);
					break;
				case KeyEvent.VK_A:
					System.out.println("a up");
					if (client != null)
						client.inputState.xBOX_Up(XBOX_Button.A);
					break;
				default:
					break;
				}
			}
		});
		panelInput.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("Mouse released");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				panelInput.requestFocus();
			}
		});
		panelInput.setBackground(Color.WHITE);

		lblState = new JLabel("");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane
						.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(textFieldIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnConnect)
										.addPreferredGap(ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
										.addComponent(lblState))
								.addComponent(btnNewButton).addComponent(btnXboxup))
						.addContainerGap())
				.addComponent(panelInput, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnConnect).addComponent(lblState))
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnNewButton).addGap(2)
						.addComponent(btnXboxup).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panelInput, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);
	}

	@Override
	public void stateChanged(ClientManager sender, ClientState newState) {
		// TODO Auto-generated method stub
		switch (newState) {
		case IDLE:
			lblState.setText("Idle");
			break;
		case LOGGED_IN:
			lblState.setText("Logged In");
			break;
		case SERVER_FULL:
			lblState.setText("Server Full");
			break;
		case CONNECTION_LOST:
			lblState.setText("Connection Lost");
			break;
		case LOGIN:
			lblState.setText("Loggin in");
			break;
		case PASSWORD_REQ:
			lblState.setText("Password required!");
			break;
		default:
			break;
		}
	}

	@Override
	public void passwordRequired(ClientManager sender) {
		// TODO Auto-generated method stub

	}

	@Override
	public void timedOut(ClientManager sender) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(this, "Server timed out!", "Error!", JOptionPane.ERROR_MESSAGE);
	}
}
