package client;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import server.Server;

public class C_PlayerSplashScreen extends JFrame {
	
	JTextField txtIpAddress;
	JButton btnOK;
	JLabel lblIpAddress;
	
	JCheckBox chkMulti;
	int W = 400;
	int H = 200;
	
	String ipAddress;
	
	public C_PlayerSplashScreen()
	{		
		Container cp = getContentPane();
		cp.setLayout(null);
		setSize(W, H);
		
		lblIpAddress = new JLabel();
		lblIpAddress.setText("Server's IP address:");
		lblIpAddress.setBounds(143,20,200,25);
		cp.add(lblIpAddress);
		
		txtIpAddress = new JTextField();
		txtIpAddress.setBounds(150, 50, 100, 25);
		cp.add(txtIpAddress);
		
		btnOK = new JButton();
		btnOK.setBounds(150, 80, 100, 25);
		btnOK.setText("Connect");
		ButtonClick bc = new ButtonClick();
		btnOK.addActionListener(bc);
		cp.add(btnOK);
		
		chkMulti = new JCheckBox("Multicast");
		chkMulti.setBounds(150, 110, 100, 25);
		cp.add(chkMulti);

		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	class ButtonClick implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent ae)
		{
			C_PongClient c = new C_PongClient( txtIpAddress.getText(), chkMulti.isSelected() );
			C_PlayerSplashScreen.this.setVisible(false);
			c.start();
		}
	}
}