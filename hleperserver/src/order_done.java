import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "order_done")
public class order_done extends HttpServlet {
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
        JSONObject result = new JSONObject();
        try {
            Class.forName(Driver);
            conn = DriverManager.getConnection(URL, User, Pass);
            stmt = conn.createStatement();
            int order_id = Integer.valueOf(request.getParameter("order_id")).intValue();
            String sql = "SELECT * FROM order_list WHERE order_id = '" + order_id + "'";
            rs = stmt.executeQuery(sql);
            boolean valid = true;
            rs.last();
            if(rs.getRow() == 1) {
                PreparedStatement pstmt = conn.prepareStatement("UPDATE order_list SET state = ? WHERE order_id = '" + order_id + "'");
                pstmt.setInt(1,2);
                pstmt.executeUpdate();
                pstmt.close();
                out.println("Success");
            }
            else {
                out.println("Failed");
            }
        }catch(SQLException se){
            out.println("Failed");
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            out.println("Failed");
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
    }
}
