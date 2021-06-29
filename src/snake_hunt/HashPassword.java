package snake_hunt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class HashPassword {

	public static void main(String[] args) throws NoSuchAlgorithmException {
              Scanner sc=new Scanner(System.in);
              String password=sc.next();
              System.out.print(hashPassword(password));
	}
	
	public static String hashPassword(String password) throws NoSuchAlgorithmException {
              MessageDigest messageDigest 	= MessageDigest.getInstance("MD5");
              messageDigest.update(password.getBytes());
              byte [] resultByteArray = messageDigest.digest();
              StringBuilder sb = new StringBuilder();
              for(byte b:resultByteArray ) {
            	     sb.append(String.format("%02x", b));
              }
              
              return sb.toString();   
	}
	
	

}
