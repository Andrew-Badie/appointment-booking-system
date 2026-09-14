/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ryerson.ca.searchappointment.persisitence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import ryerson.ca.searchappointment.helper.Appointment;
import ryerson.ca.searchappointment.helper.Service;
/**
 *
 * @author student
 */
public class Appointment_CRUD {
    
    private static Connection getCon(){
        Connection con=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
        String connection=System.getenv("DB_URL");
        //String connection ="localhost:3306";
         con=DriverManager.getConnection("jdbc:mysql://"+connection+"/LBS?allowPublicKeyRetrieval=true&useSSL=false", "root", "student" );
        
         
         System.out.println("Connection established.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return con;
    }
    public static Set<Appointment> searchForAppointments(String query){
        Set<Appointment> appointments = new HashSet<>();
        try{
            Connection con = getCon();
            //NATURAL JOIN APPOINTMENT_SERVICE " + "NATURAL JOIN SERVICE
            String q = "select * from APPOINTMENT  WHERE serviceName LIKE '%"+query+"%';";
            System.out.println(q);
                            PreparedStatement ps = con.prepareStatement(q);
                            ResultSet rs = ps.executeQuery();
                            while(rs.next()){
                               // been = new UserInfo();
                                String id = rs.getString("id");
                                String serviceName = rs.getString("serviceName");
                               // String firstName = rs.getString("firstname");
                                //String lastName = rs.getString("lastname");
                                
                                //Service service = new Service(firstName, lastName);
                                Appointment appointment = new Appointment(id, serviceName);
                                appointments.add(appointment);
                            }
                            con.close();
                    
                    }catch(Exception e){
                        System.out.println(e);
                    }
        System.out.println(">>>>>>>>>>>"+appointments.size());
        return appointments;
    }

    
    
    
    
    
    
}
