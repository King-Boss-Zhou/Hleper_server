import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "orders")
public class orders extends HttpServlet {
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
        JSONObject metadata = new JSONObject();
        JSONObject result = new JSONObject();
        JSONArray orders = new JSONArray();
        try {
            Class.forName(Driver);
            conn = DriverManager.getConnection(URL, User, Pass);
            stmt = conn.createStatement();
            int limit = Integer.valueOf(request.getParameter("limit")).intValue();
            int campus = Integer.valueOf(request.getParameter("campus")).intValue();
            String sql = "SELECT * FROM order_list WHERE campus = '" + campus + "'" + " and state <> '" + 2 + "'";
            rs = stmt.executeQuery(sql);
            boolean valid = true;
            int total = 0;
            while (rs.next()){
                JSONObject temp = new JSONObject();
                int order_id = rs.getInt(1);
                int order_type = rs.getInt(2);
                int order_max_num = rs.getInt(8);
                int order_crt_num = rs.getInt(7);
                double order_reward = rs.getDouble(9);
                String order_title = rs.getString(4);
                String order_description = rs.getString(5);
                temp.put("order_id",order_id);
                temp.put("type", order_type);
                temp.put("max_num", order_max_num);
                temp.put("crt_num",order_crt_num);
                temp.put("reward", order_reward);
                temp.put("title", order_title);
                temp.put("description", order_description);
                orders.put(temp);
                total++;
                if(total >= limit)break;
            }
            metadata.put("valid",valid);
            metadata.put("total",total);
            result.put("metadata",metadata);
            result.put("orders", orders);
            out.println(result.toString());
        }catch(SQLException se){
            metadata.put("valid",false);
            result.put("metadata",metadata);
            out.println(result.toString());
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            metadata.put("valid",false);
            result.put("metadata",metadata);
            out.println(result.toString());
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
    }
}
