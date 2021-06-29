package snake_hunt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LoginIntoAccount {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchAlgorithmException, SQLException{
		System.out.println(loginIntoAccount());
	}
	
	public static boolean loginIntoAccount() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/snake","root","root");
        Scanner sc=new Scanner(System.in);
        int attempts=0;
        PreparedStatement ps=con.prepareStatement("select username,password from user where username=?");
        while(true) {
        	if(attempts==5) {
        		break;
        	}
        	System.out.println("Enter username :");
            String username=sc.next();
            System.out.println("Enter password :");
            String password=sc.next();
            ps.setString(1, username);
            ResultSet rs=ps.executeQuery();
            if(rs.next()) {
            	String passwordfromDB=rs.getString("password");
            	String HashedPasswordFromUser=HashPassword.hashPassword(password);
            	byte[] HashedPasswordFromUserBytes=HashedPasswordFromUser.getBytes();
            	byte[] passwordfromDBBytes=passwordfromDB.getBytes();
            	MessageDigest messageDigest 	= MessageDigest.getInstance("MD5");
            	if(messageDigest.isEqual(HashedPasswordFromUserBytes, passwordfromDBBytes)) {
            	     return true;	
            	}
            	else {
            		attempts=attempts+1;
                	System.out.println("password is incorrect you have "+(5-attempts)+" chances left");
            	}
            	
            }
            else {
            	attempts=attempts+1;
            	System.out.println("username is incorrect you have "+(5-attempts)+" chances left");
            }
            
            
        }
        
        System.out.println("All attempts are done try after some time");   
        return false;
		
	}

}
