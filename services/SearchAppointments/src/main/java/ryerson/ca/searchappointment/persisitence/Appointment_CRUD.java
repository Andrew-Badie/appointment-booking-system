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
        String sql = "SELECT * FROM APPOINTMENT WHERE serviceName LIKE ?";
        try (Connection con = getCon()) {
            if (con == null) {
                throw new IllegalStateException("Search database is unavailable");
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + query + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        appointments.add(new Appointment(rs.getString("id"), rs.getString("serviceName")));
                    }
                }
            }
        } catch (java.sql.SQLException e) {
            throw new IllegalStateException("Unable to search appointments", e);
        }
        System.out.println(">>>>>>>>>>>"+appointments.size());
        return appointments;
    }

    
    
    
    
    
    
}

