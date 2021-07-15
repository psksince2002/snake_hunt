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


import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {
	
	static final int SCREEN_WIDTH = 650;
	static final int SCREEN_HEIGHT = 650;
	static final int UNIT_SIZE=25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static  int DELAY=100;
	static boolean play = true;
	final int x[]=new int[GAME_UNITS];
	final int y[]=new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	boolean welcome = true;
	Timer timer;
	Random random;
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		//startGame();
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
		if(welcome) welcomePage(g);
		else if(running) {
			
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
	
	public void welcomePage(Graphics g) {
		//SNAKE HUNT
		g.setColor(Color.red);
		g.setFont(new Font("INK Free",Font.BOLD,75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("SNAKE HUNT", (SCREEN_WIDTH - metrics1.stringWidth("SNAKE HUNT"))/2, SCREEN_HEIGHT/2);
		
		//Difficulty level
		g.setColor(Color.cyan);
		g.setFont(new Font("INK Free",Font.BOLD,30));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Select a difficulty level", (SCREEN_WIDTH - metrics2.stringWidth("Select a difficulty level"))/2, 350);
		
		//easy
		g.setColor(Color.green);
		g.setFont(new Font("INK Free",Font.BOLD,25));
		FontMetrics metricsd1 = getFontMetrics(g.getFont());
		g.drawString("Easy (Press '1')", (SCREEN_WIDTH - metricsd1.stringWidth("Easy (Press '1')"))/2, 400);
		//medium
		g.setColor(Color.yellow);
		g.setFont(new Font("INK Free",Font.BOLD,25));
		FontMetrics metricsd2 = getFontMetrics(g.getFont());
		g.drawString("Medium (Press '2')", (SCREEN_WIDTH - metricsd2.stringWidth("Medium (Press '2')"))/2, 450);
		//hard
		g.setColor(new Color(255,140,0));
		g.setFont(new Font("INK Free",Font.BOLD,25));
		FontMetrics metricsd3 = getFontMetrics(g.getFont());
		g.drawString("Hard (Press '3')", (SCREEN_WIDTH - metricsd3.stringWidth("Hard (Press '3')"))/2, 500);
				
		
		
		//Use arrow keys
		g.setColor(Color.GRAY);
		g.setFont(new Font("INK Free",Font.ITALIC,20));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("(Use Arrow keys to move.)", (SCREEN_WIDTH - metrics3.stringWidth("(Use Arrow keys to move.)"))/2, SCREEN_HEIGHT - 50);
		
	}
	
	public void gameOver(Graphics g) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/snake","root","root");
		Statement st = con.createStatement();
		SnakeGame s = new SnakeGame();
	    String us = s.playername;
	    
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
		
		
	    PreparedStatement ps=con.prepareStatement("select high_score from userinfo where username=?");
	    ps.setString(1, us);
		ResultSet rs=ps.executeQuery();
		int bal = 0;
		while(rs.next()){
			bal = rs.getInt(1);
		}
		
		System.out.println(bal);
		if(applesEaten>bal) {
			int re = st.executeUpdate("update userinfo set high_score ='"+applesEaten+"' where username='"+us+"'");
			System.out.println(re);
		}
		
	    PreparedStatement ps1=con.prepareStatement("select high_score from userinfo where username=?");
	    ps1.setString(1, us);
		ResultSet rs1=ps1.executeQuery();
		int bal1 = 0;
		while(rs1.next()){
			bal1 = rs1.getInt(1);
		}
		
		//Your High Score is
		g.setColor(Color.red);
		g.setFont(new Font("INK Free",Font.BOLD,30));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Your High Score is : "+bal1, (SCREEN_WIDTH - metrics2.stringWidth("Your High Score is : "+ bal1))/2,SCREEN_HEIGHT - 40);
		
		//ESC TO EXIT
		g.setColor(Color.GRAY);
		g.setFont(new Font("INK Free",Font.ITALIC,20));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("Press 'ESC' to exit.", (SCREEN_WIDTH - metrics3.stringWidth("Press 'ESC' to exit."))/2, SCREEN_HEIGHT - 5);
		
		
	}
	
	public void resume() {
		play = true;
		timer.start();
	}
	
	public void pause() {
		play = false;
		timer.stop();
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
			case KeyEvent.VK_SPACE:
				if(play) pause();
				else resume();
				break;
			case KeyEvent.VK_1:
				if(welcome) {
					DELAY = 100;
					welcome = false;
					startGame();
				}
				break;
			case KeyEvent.VK_2:
				if(welcome) {
					DELAY = 75;
					welcome = false;
					startGame();
				}
				break;
			case KeyEvent.VK_3:
				if(welcome) {
					DELAY = 60;
					welcome = false;
					startGame();
				}
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			}
		}

	

	}
}
