package snake_hunt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginFrame extends JFrame implements ActionListener {
	Container container=getContentPane();
	JLabel userLabel=new JLabel("PLAYER NAME");
	JTextField userTextField=new JTextField();
	JButton loginButton=new JButton("START GAME");
	LoginFrame() {
        this.setTitle("Snake Game");
        this.setVisible(true);
        this.setBounds(10,10,370,250);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayoutManager();
        this.setLocationAndSize();
        this.addComponentsToContainer();
        this.getContentPane().setBackground(Color.cyan);
	}
	
	 public void setLayoutManager()
	 {
	       //Setting layout manager of Container to null
	       container.setLayout(null);
	 }
	 public void setLocationAndSize()
	 {
	       //Setting location and Size of each components using setBounds() method.
	    userLabel.setBounds(50,70,100,30);
	    userTextField.setBounds(150,70,150,30); 
	    loginButton.setBounds(120,120,120,30);
	 }
	
    @Override
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource()==loginButton) {
    		SnakeGame.playername=userTextField.getText();
    		
    		try {
				new GameFrame();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		this.dispose();
    	}
 
    }
    public void addComponentsToContainer()
    {
       //Adding each components to the Container
        container.add(userLabel);
        container.add(userTextField);
        container.add(loginButton);
        loginButton.addActionListener(this);
    }

}
