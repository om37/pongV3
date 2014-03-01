package client;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import common.Global;

public class C_StartScreen extends JFrame {
	
	String[] options; 
	JComboBox<String> cmbSelect;	
	JButton btnOk;	
	
	int W = 400;
	int H = 200;
	
	/** 
	 * Start screen main. Entry point for program
	 * Shows new start screen
	 * */
	public static void main(String[] args)
	{
		new C_StartScreen().setVisible(true);
	}
	
	/*
	 * Constructor
	 * Initiates variables and sets up screen
	 * */
	public C_StartScreen()
	{
		System.out.println("Start splash screen " + Global.numOfGames);
		Container cp = getContentPane();
		cp.setLayout(null);
		setSize(W,H);
		
		options = new String[]{"Please select a game type...","I want to play!","I like to watch"};
		cmbSelect = new JComboBox<String>(options);
		cmbSelect.setBounds(20, 20, 345, 25);
		cmbSelect.setEditable(false);
		cp.add(cmbSelect);
		
		btnOk = new JButton();
		btnOk.setBounds(150, 50, 100, 25);
		btnOk.setText("Connect");
		btnOk.addActionListener(new ButtonClick());
		cp.add(btnOk);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	class ButtonClick implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(cmbSelect.getSelectedItem());
			String selection = (String)cmbSelect.getSelectedItem();
			if(selection.equals(options[1]))//Player
			{
				new C_PlayerSplashScreen().setVisible(true);
				C_StartScreen.this.setVisible(false);
			}
			else if(selection.equals(options[2]))//Observer
			{
					new C_ObserverSplashScreen().setVisible(true);
					C_StartScreen.this.setVisible(false);

			}
			//If option hasn't changed, do nothing!
		}
		
	}
}
