<%-- 
    Document   : newjsplogin
    Created on : Mar 17, 2025, 11:32:31 AM
    Author     : student
--%>
<%@page import="java.util.Arrays"%>
<%@page import="ryerson.ca.helper.Appointment"%>
<%@page import="ryerson.ca.helper.AppointmentsXML"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        
        <style>
            * {box-sizing: border-box;}

            body {
                margin: 0;
                font-family: Arial, Helvetica, sans-serif;
            }

            .topnav {
                overflow: hidden;
                background-color: #e9e9e9;
            }

            .topnav a {
                float: left;
                display: block;
                color: black;
                text-align: center;
                padding: 14px 16px;
                text-decoration: none;
                font-size: 17px;
            }

            .topnav a:hover {
                background-color: #ddd;
                color: black;
            }

            .topnav a.active {
                background-color: #2196F3;
                color: white;
            }

            .topnav .login-container {
                float: right;
            }

            .topnav input[type=text] {
                padding: 6px;
                margin-top: 8px;
                font-size: 17px;
                border: none;
                width:120px;
            }

            .topnav .login-container button {
                float: right;
                padding: 6px 10px;
                margin-top: 8px;
                margin-right: 16px;
                background-color: #555;
                color: white;
                font-size: 17px;
                border: none;
                cursor: pointer;
            }

            .topnav .login-container button:hover {
                background-color: green;
            }

            @media screen and (max-width: 600px) {
                .topnav .login-container {
                    float: none;
                }
 .topnav a, .topnav input[type=text], .topnav .login-container button {
                    float: none;
                    display: block;
                    text-align: left;
                    width: 100%;
                    margin: 0;
                    padding: 14px;
                }
                .topnav input[type=text] {
                    border: 1px solid #ccc;  
                }
            }
       
table {
  font-family: arial, sans-serif;
  border-collapse: collapse;
  width: 60%;
}

td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 15px;
}

tr:nth-child(even) {
  background-color: #dddddd;
}

        </style>
    </head>
    <body>
        <div class=""topnav">
            Greetings <%=request.getAttribute("username")%>
            
        </div>
            <div style ="padding-left:16px">
                <h2>Search Appointment</h2> 
                <form action="FrontEnd" method="post">
                    <input type="hidden" name="pageName" value="search"/>
                    Search in service name and service provider names
                    <input type="text" name="query">
                    <input type="submit">
                    
                </form>
            </div>    
            <div style="padding-left:16px">
                <p></p>
                <form action="FrontEnd" method="post">
                    <input type="hidden" name="pageName" value="book"/>
                    <table>
                        <tr><th></th><th>ID</th><th>Service Name</th><th>Book</th></tr>
                        <% AppointmentsXML appointments = (AppointmentsXML) request.getAttribute("appointmentResults");
                        if (appointments!=null){
                            int i=0;
                            for(Appointment appointment : appointments.getAppointments()){
                                i++;%><tr><td><%=i%></td><td><%=appointment.getId()%></td><td><%=appointment.getServiceName()%></td>
                            <td><% if(appointment.booking()){%>
                                <input type ="submit" value ="Book"><% } else {System.out.println("Currently Not Avialable");}%></td>
                            <% }
}
                        
                      %>  </tr>
                            
                        
                    </table>
                </form>
            </div>
        
            
            
            
            
    </body>         
        
</html>
