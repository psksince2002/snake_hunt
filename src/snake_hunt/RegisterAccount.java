package snake_hunt;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public class RegisterAccount {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchAlgorithmException, SQLException {
		registerAccount();
	}
	
	public static boolean isValidISOCountry(String s) {
        for(String iso:Locale.getISOCountries()) {
       	 Locale l = new Locale("", iso);
            if(l.getDisplayCountry().equalsIgnoreCase(s)==true) {
           	 return true;
            }
        }
        return false;
   }
	
	private static final Set<String> ISO_COUNTRIES = new HashSet<String>
    (Arrays.asList(Locale.getISOCountries()));
	
	public static void registerAccount() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/snake","root","root");
		Scanner sc=new Scanner(System.in);
		System.out.println("enter username :");
		String username=sc.next();
		PreparedStatement ps=con.prepareStatement("select username from user where username=?");
		while(true) {
			ps.setString(1, username);
			ResultSet rs=ps.executeQuery();
			if(rs.next()==false) {
				break;
			}
			else {
				System.out.println("username already exists");
				System.out.println("enter username :");
				 username=sc.next();
			}
		}
		
		//Getting password from the user
		String hashedPassword;
		while(true) {
			System.out.println("enter password :");
			String password=sc.next();
			if(password.length()>6) {
				hashedPassword=HashPassword.hashPassword(password);
				break;
			}
			else {
				System.out.println("password length should be minimum of 6 charecters");
			}
			
		}
		
		//Getting Country name form the user
		String countryName;
		while(true) {
			System.out.println("enter the name of your country");
			countryName=sc.next();
			if(isValidISOCountry(countryName)) {
				break;
			}
			else {
				System.out.println("not a valid country name");
			}
		}
		
		long highscore=0;
		Statement st=con.createStatement();
		String sqlcreaterow="INSERT INTO user VALUES ('" + username  + "','" + hashedPassword + "','"+ highscore + "','" + countryName + "')";
		st.executeUpdate(sqlcreaterow);
		System.out.println("Account has been created");
		
	}
	

}
