package client;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import common.Global;
import common.NetMCReader;

/**
 * A "splash screen" to be shown to users who indicate they would like to observe, rather than play, a game of pong
 * Contains a NetMCReader in order to "pick up" the list of games available for watching.
 * @author om37
 *
 */
public class C_ObserverSplashScreen extends JFrame
{
	JButton btnOK;					//The ok button
	JLabel lblGameChoice;			//A label
	JComboBox<String> cmbGameList;	//Combo box to hold the game list
	NetMCReader in;					//MCReader to get the list of available games

	String games;					
	int W = 400;
	int H = 200;

	/**
	 * Constructor - initialises window and populates combo box
	 */
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

	/**
	 * Populates the screen's combo box with the list of available games and returns populated combo box
	 * @return JComboBox - the combo box filled with the game list
	 */
	private JComboBox<String> populateComboBox() 
	{
		try 
		{
			in = new NetMCReader(Global.P_GAME_LIST, Global.LIST_MCA);
			while(games == null ? games==null : games.isEmpty())//While games is null or empty
			{
				games = in.get().split(",")[0];//
				System.out.println(in.get());
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		String[] gameList = games.split("-");		
		cmbGameList = new JComboBox<String>(gameList);
		return cmbGameList;
	}

	/**
	 * Action listener for the OK button. Creates a new C_PongObserver object and
	 * starts it in a separate thread, closes this window.
	 * @author om37
	 *
	 */
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
