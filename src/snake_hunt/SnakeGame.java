package snake_hunt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SnakeGame {

	public static String playername;

	public static void main(String[] args) throws SQLException, ClassNotFoundException{
		
		new LoginFrame();
		
       
	}

}
