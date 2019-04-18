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

@WebServlet(name = "specific")
public class specific extends HttpServlet {
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
        JSONObject order_info = new JSONObject();
        try {
            Class.forName(Driver);
            conn = DriverManager.getConnection(URL, User, Pass);
            stmt = conn.createStatement();
            int order_id = Integer.valueOf(request.getParameter("order_id")).intValue();
            String sql = "SELECT * FROM order_list WHERE order_id = '" + order_id + "'";
            rs = stmt.executeQuery(sql);
            boolean valid = true;
            rs.last();
            if(rs.getRow()==1){
                int order_type = rs.getInt(2);
                int order_max_num = rs.getInt(8);
                int order_crt_num = rs.getInt(7);
                double order_reward = rs.getDouble(9);
                String order_title = rs.getString(4);
                String order_description = rs.getString(5);
                String order_detail = rs.getString(6);
                order_info.put("order_id",order_id);
                order_info.put("type", order_type);
                order_info.put("max_num", order_max_num);
                order_info.put("current_num",order_crt_num);
                order_info.put("reward", order_reward);
                order_info.put("title", order_title);
                order_info.put("description", order_description);
                order_info.put("detail",order_detail);

                String sql2 = "SELECT * FROM order_user WHERE order_id = '" + order_id + "'";
                rs = stmt.executeQuery(sql2);
                rs.last();
                if(rs.getRow() == 1){
                    int user_id = rs.getInt(2);
                    String sql3 = "SELECT * FROM user WHERE id = '" + user_id + "'";
                    rs = stmt.executeQuery(sql3);
                    rs.last();
                    if(rs.getRow() == 1){
                        String user_name = rs.getString(2);
                        order_info.put("user_id",user_id);
                        order_info.put("user_name",user_name);
                    }
                    else{
                        valid = false;
                    }
                }
                else{
                    valid = false;
                }
            }
            else {
                valid = false;
            }
            metadata.put("valid", valid);
            result.put("metadata",metadata);
            result.put("order_info", order_info);
            out.println(result.toString());
        }catch(SQLException se){
            metadata.put("valid",false);
            result.put("metadata",metadata);
            result.put("order_info",order_info);
            out.println(result.toString());
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            metadata.put("valid",false);
            result.put("metadata",metadata);
            result.put("order_info",order_info);
            out.println(result.toString());
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
    }
}
