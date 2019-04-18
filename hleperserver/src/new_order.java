import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "new_order")
public class new_order extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
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
            int order_type = Integer.valueOf(request.getParameter("order_type")).intValue();
            int order_campus = Integer.valueOf(request.getParameter("order_campus")).intValue();
            String order_title = request.getParameter("order_title");
            String order_description = request.getParameter("order_description");
            String order_detail = request.getParameter("order_detail");
            int order_max_num = Integer.valueOf(request.getParameter("order_max_num")).intValue();
            double order_reward = Double.valueOf(request.getParameter("order_reward")).doubleValue();
            int order_user_id = Integer.valueOf(request.getParameter("order_user_id")).intValue();
            JSONObject user = new JSONObject();
            JSONObject result = new JSONObject();
            String sql = "SELECT * FROM order_list";
            rs = stmt.executeQuery(sql);
            rs.last();
            int order_id = rs.getRow() + 1;
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO order_list VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setInt(1,order_id);
            pstmt.setInt(2,order_type);
            pstmt.setInt(3,order_campus);
            pstmt.setString(4,order_title);
            pstmt.setString(5,order_description);
            pstmt.setString(6,order_detail);
            pstmt.setInt(7,0);
            pstmt.setInt(8, order_max_num);
            pstmt.setDouble(9, order_reward);
            pstmt.setInt(10,0);
            pstmt.setInt(11,0);
            pstmt.setInt(12,0);
            pstmt.executeUpdate();
            pstmt.close();

            PreparedStatement pstmt2 = conn.prepareStatement("INSERT INTO order_user VALUES(?, ?)");
            pstmt2.setInt(1,order_id);
            pstmt2.setInt(2,order_user_id);
            pstmt2.executeUpdate();
            pstmt2.close();

            out.println("Success");
        }catch(SQLException se){
            // 处理 JDBC 错误
            out.println("Failed");
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            out.println("Failed");
            e.printStackTrace();
        }
    }
}
