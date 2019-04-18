import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(name = "login_up")
public class login_up extends HttpServlet {
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
            String sql = "SELECT email_address FROM user WHERE email_address = '"+ email_address+"'";
            rs = stmt.executeQuery(sql);
            rs.last();
            int rows = rs.getRow();
            if(rows == 0){
                String sql_count = "SELECT * FROM user";
                rs = stmt.executeQuery(sql_count);
                rs.last();
                int count = rs.getRow();
                int id = count + 1;
                String name = "USER" +  (count + 1);
                String phone = "88888888";
                double balance = 0;

                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user VALUES(?, ?, ?, ?, ?, ?)");
                pstmt.setInt(1,id);
                pstmt.setString(2,name);
                pstmt.setString(3,password);
                pstmt.setString(4,email_address);
                pstmt.setString(5,phone);
                pstmt.setDouble(6,balance);
                pstmt.executeUpdate();
                pstmt.close();

                user.put("id",id);
                user.put("name", name);
                user.put("email",email_address);
                user.put("phone",phone);
                user.put("balance",balance);
                result.put("valid",valid);
                result.put("user", user);
                out.println(result.toString());
            }
            else{
                valid = false;
                result.put("user", user);
                result.put("valid", valid);
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
