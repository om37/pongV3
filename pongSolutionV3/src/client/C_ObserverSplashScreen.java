package client;

import static common.Global.P_SERVER_WRITE;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import common.Global;
import common.NetMCReader;

public class C_ObserverSplashScreen extends JFrame {
	
	JButton btnOK;
	JLabel lblGameChoice;
	JComboBox<String> cmbGameList;
	NetMCReader in;
	
	String games;
	int W = 400;
	int H = 200;
	
	String ipAddress;
	
	public C_ObserverSplashScreen()
	{
		Container cp = getContentPane();
		cp.setLayout(null);
		setSize(W, H);
		
		lblGameChoice = new JLabel();
		lblGameChoice.setText("Please choose game to watch:");
		lblGameChoice.setBounds(120,20,200,25);
		cp.add(lblGameChoice);
		
		cmbGameList = populateComboBox();
		cmbGameList.setBounds(120, 50, 200, 25);
		cp.add(cmbGameList);
		
		btnOK = new JButton();
		btnOK.setBounds(150, 80, 100, 25);
		btnOK.setText("Connect");
		ButtonClick bc = new ButtonClick();
		btnOK.addActionListener(bc);
		cp.add(btnOK);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private JComboBox<String> populateComboBox() 
	{
		try 
		{
			in = new NetMCReader(P_SERVER_WRITE, Global.MCA);//Get reader
			games = in.get().split(",")[0];//
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		int amount = Integer.parseInt(games);
		String[] gamesForList = new String[amount];
		for(int i = 0; i < amount;i++)
		{
			gamesForList[i] = String.valueOf(i);
		}
		cmbGameList = new JComboBox<String>(gamesForList);
		return cmbGameList;
	}

class ButtonClick implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		//Observer stuff
		C_PongObserver o = new C_PongObserver( (String)cmbGameList.getSelectedItem() );
		C_ObserverSplashScreen.this.setVisible(false);
		o.start(); //start as a separate thread
	}
}

}
