import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "login")
public class login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs;

        String Driver = "com.mysql.jdbc.Driver";
        String URL = "jdbc:mysql://localhost:3306/hleper";
        String User = "3160101817";
        String Pass = "123456";
        try {
            Class.forName(Driver);
            conn = DriverManager.getConnection(URL, User, Pass);
            stmt = conn.createStatement();
            String email_address = request.getParameter("email");
            String password = request.getParameter("password");
            JSONObject user = new JSONObject();
            JSONObject result = new JSONObject();
            boolean valid = true;
            String sql = "SELECT * FROM user WHERE email_address = '"+ email_address + "'" + "and password = '" + password +"'";
            rs = stmt.executeQuery(sql);
            rs.last();
            int rows = rs.getRow();
            if(rows == 0){
                valid = false;
                result.put("user", user);
                result.put("valid", valid);
                out.println(result.toString());
            }
            else{
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String phone = rs.getString(5);
                double balance = rs.getDouble(6);
                user.put("id", id);
                user.put("name", name);
                user.put("email", email_address);
                user.put("phone", phone);
                user.put("balance", balance);
                result.put("valid", valid);
                result.put("user", user);
                out.println(result.toString());
            }
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
    }
}
