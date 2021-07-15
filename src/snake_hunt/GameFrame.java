package snake_hunt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;

public class GameFrame extends JFrame{

	GameFrame() throws SQLException, ClassNotFoundException{
	    String us = SnakeGame.playername;
	    System.out.println(us);
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/snake","root","root");
        PreparedStatement ps=con.prepareStatement("select username from userinfo where username=?");
        ps.setString(1, us);
        ResultSet rs=ps.executeQuery();
        int high_score=0;
        if(rs.next()==false) {
        	PreparedStatement ps1=con.prepareStatement("INSERT INTO userinfo VALUES (?,?)");
    		ps1.setString(1,us);
    		ps1.setInt(2,high_score);
    		int re = ps1.executeUpdate();
        }
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

}
