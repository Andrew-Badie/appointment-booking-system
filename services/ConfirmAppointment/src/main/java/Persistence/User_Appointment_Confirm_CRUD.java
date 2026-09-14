package Persistence;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class User_Appointment_Confirm_CRUD {
    
    public static Connection getCon() throws ClassNotFoundException{
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
    
    public static Set<AppointmentConfirm> getConfirmedAppointments(String username){
        Set<AppointmentConfirm> appointments= new HashSet<AppointmentConfirm>();
        try{
            Connection con= getCon();
            String q;
            if(username.isEmpty()){
               q="select * from USER_APPOINTMENT_Confirm "
                 +";";
            }
            else
             q = "select * from USER_APPOINTMENT_Confirm "
                    + " WHERE username LIKE '"+username+"'"+";";

			PreparedStatement ps=con.prepareStatement(q);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){

				String code=rs.getString("code");
			
                                String date=rs.getDate("date1").toString();
                                
                                
                                AppointmentConfirm appointment = new AppointmentConfirm(code,date, username);
                                appointments.add(appointment);
                                
                                }
			
			con.close();

		}catch(Exception e){System.out.println(e);}
           
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>"+appointments.size());
        return appointments;
        
    }
    public static AppointmentConfirm getConfirmedAppointments(String username, String code){
        AppointmentConfirm appointment=null;
        try{
            Connection con= getCon();
            
            String q = "select * from USER_APPOINTMENT_Confirm "
                    + " WHERE username LIKE '"+username+"'"+" and "
                    + "code LIKE '"+code+"';";

			PreparedStatement ps=con.prepareStatement(q);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){

				
				
                                String date=rs.getDate("date1").toString();
                                
                                
                                 appointment = new AppointmentConfirm(code,date,username);
                                
                                
                                }
			
			con.close();

		}catch(Exception e){System.out.println(e);}
            
    
        return appointment;
        
    }
    
    
    public static boolean borrow(String username, String code){
      
        try{
            Connection con= getCon();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");
             LocalDate date = LocalDate.now();
            System.out.println(date.format(formatter));
            String q = "insert into USER_APPOINTMENT_Confirm "
                    + "(code, username, date1) values "
                    + "('"+code+"', '"+username+"', '"+date.format(formatter)+"');";
            Statement stmt = con.createStatement(); 
            stmt.execute(q);
            
			con.close();
                        return true;

		}catch(Exception e){System.out.println(e);
                return false;
                }
 
        
    }
}
