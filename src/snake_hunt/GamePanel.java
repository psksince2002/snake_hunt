package snake_hunt;

import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE=25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static  int DELAY=150;
	final int x[]=new int[GAME_UNITS];
	final int y[]=new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
		

	public void startGame() {
		newApple();
		running=true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			try {
				draw(g);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void draw(Graphics g) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {
		if(running) {
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++)
			{
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH,i*UNIT_SIZE);
			}
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			for(int i=0;i<bodyParts;i++) {
				if(i==0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor((new Color(45,180,0)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("INK Free",Font.BOLD,50));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
	}
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {
		for(int i=bodyParts;i>0;i--) {
			x[i]=x[i-1];
			y[i]=y[i-1];
		}
		switch(direction) {
		case 'U':
			y[0]=y[0]-UNIT_SIZE;
			break;
		case 'D':
			y[0]=y[0]+UNIT_SIZE;
			break;
		case 'L':
			x[0]=x[0]-UNIT_SIZE;
			break;
		case 'R':
			x[0]=x[0]+UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if(x[0]==appleX && y[0]==appleY) {
			bodyParts++;
			applesEaten++;
			newApple();
			int c=1;
			if(applesEaten>7*c) {
				DELAY=DELAY-30;
				timer = new Timer(DELAY,this);
				timer.start();
				c=c+1;
			}
			
		}
	}
	public void checkCollisions() {
		//if head collides with body
		for(int i=bodyParts;i>0;i--) {
			if((x[0]==x[i]) && (y[0]==y[i])) {
				running=false;
			}
		}
		//if head touches borders
		
		if(x[0]<0) {
			running = false;
		}
		if(x[0]>SCREEN_WIDTH) {
			running=false;
		}
		if(y[0]<0) {
			running = false;
		}
		if(y[0]>SCREEN_HEIGHT) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
		//score
		g.setColor(Color.red);
		g.setFont(new Font("INK Free",Font.BOLD,50));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+ applesEaten))/2,g.getFont().getSize());
		//game over
		g.setColor(Color.red);
		g.setFont(new Font("INK Free",Font.BOLD,75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("GameOver"))/2, SCREEN_HEIGHT/2);
		
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/snake","root","root");
		Statement st = con.createStatement();
		LoginIntoAccount s = new LoginIntoAccount();
	    String us = s.res;
	    PreparedStatement ps=con.prepareStatement("select high_score from user where username=?");
	    ps.setString(1, us);
		ResultSet rs=ps.executeQuery();
		int bal = 0;
		while(rs.next()){
			bal = rs.getInt(1);
		}
		
		System.out.println(bal);
		if(applesEaten>bal) {
			int re = st.executeUpdate("update user set high_score = high_score +'"+applesEaten+"' where username='"+us+"'");
			System.out.println(re);
		}
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction !='R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction !='L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction !='D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction !='U') {
					direction = 'D';
				}
				break;
			}
		}

	

	}
}
