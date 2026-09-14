/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import Helper.AppointmentConfirm;

/**
 *
 * @author student
 */
public class APPOINTMENT_Booking_CRUD {
    public static Connection getCon() throws ClassNotFoundException, SQLException{
       Connection con=null;
     try{
         Class.forName("com.mysql.jdbc.Driver");
        String connection=System.getenv("DB_URL");
        //String connection ="localhost:3306";
         con=DriverManager.getConnection("jdbc:mysql://"+connection+"/confirm_LBS?allowPublicKeyRetrieval=true&useSSL=false", "root", "student" );
        
         
         System.out.println("Connection established.");
     }
     catch(Exception e){ System.out.println(e);}
     return con;
     
    }
    
    public static boolean isOnBook(String code){
       boolean result;
        try{
            Connection con= getCon();
            
        
            
            String q = "select * from APPOINTMENT_Book "
                    + " WHERE code LIKE '"+code+"'"+";";

			PreparedStatement ps=con.prepareStatement(q);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){

			result=true;
                                
                                }
                        else
                            result=false;
			
			con.close();

		}catch(Exception e){return false;}
            return result;
    }
    
    
    public static Set<AppointmentConfirm>  getBookings(){
        Set<AppointmentConfirm> appointments= new HashSet<AppointmentConfirm>();
        

        try{
            Connection con= getCon();
            
        
            
            String q = "select * from APPOINTMENT_Book "
                    +";";
                        System.out.println(q);
			PreparedStatement ps=con.prepareStatement(q);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){

				String isbn=rs.getString("code");
			
                                 
                                
                                AppointmentConfirm appointment = new AppointmentConfirm(isbn,null, null);
                                appointments.add(appointment);
                                
                                }
			
			
			con.close();

		}catch(Exception e){return appointments;}
            return appointments;
    }
    
    public static void addBook(String code) throws ClassNotFoundException, SQLException{
      
        
            Connection con= getCon();
          
            String q = "insert into APPOINTMENT_Book "
                    + "(code) values "
                    + "('"+code+"');";
            Statement stmt = con.createStatement(); 
           
            stmt.execute(q);
			con.close();
                        

		 
 
        
    }
    
    public static void addBook(String code, String username, String date) throws ClassNotFoundException, SQLException{
      
        
            Connection con= getCon();
          
            String q = "insert into APPOINTMENT_Book "
                    + "(code, username, date1) values "
                    + "("+
                    "'" +code+"'"+ ","
                    +"'"+username+"'" + ","
                    +"'"+date+"'"
                    +"');";
            Statement stmt = con.createStatement(); 
           
            stmt.execute(q);
			con.close();
                        

		 
 
        
    }
    
}
