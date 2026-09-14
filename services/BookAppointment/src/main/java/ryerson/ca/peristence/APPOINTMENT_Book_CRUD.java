/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ryerson.ca.peristence;
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

import ryerson.ca.helper.AppointmentBook;
/**
 *
 * @author student
 */
public class APPOINTMENT_Book_CRUD {
    private static Connection getCon(){
        Connection con = null;
        try{
               Class.forName("com.mysql.jdbc.Driver");
        String connection=System.getenv("DB_URL");
        //String connection ="localhost:3306";
         con=DriverManager.getConnection("jdbc:mysql://"+connection+"/book_LBS?allowPublicKeyRetrieval=true&useSSL=false", "root", "student" );
        }catch(Exception e){
            System.out.println(e);
        }
        return con;
    }
    public static boolean addBook(String code, String username){
        try{
            Connection con = getCon();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.now();
            String q = "insert into APPOINTMENT_Book "+"(code, userid, date1) values "+"('"+code+"', "+"'"+username+"', "
                    +"'"+date.format(formatter)+"');";
            Statement stmt = con.createStatement();
            System.out.println(q);
            stmt.execute(q);
            con.close();
            return true;
        }catch(Exception e){System.out.println("e");
        return false;
        
        }
    }
    public static AppointmentBook getBookAppointment(String code){
        AppointmentBook appointment = null;
        try{
            Connection con = getCon();
            
            String q = "select * from APPOINTMENT_Book "+ " WHERE "+"code = '"+code+"';";
            System.out.println(q);
            
            PreparedStatement ps = con.prepareStatement(q);
            ResultSet rs  = ps.executeQuery();
            if(rs.next()){
                String date = rs.getDate("date1").toString();
                String username = rs.getString("userid").toString();
                appointment = new AppointmentBook(code, date, username);
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }
        return appointment;
        
    }
    
    public static boolean addBook(String code, String userid, String bookdate)throws ClassNotFoundException, SQLException{
        return(addBook(code, userid));
        
    }
}
