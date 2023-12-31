package com.example.leavesmanagement.Repository;

import com.example.leavesmanagement.entity.*;
import lombok.Cleanup;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private final DataSource dataSource;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<UserData> getUsers(int page) throws Error, SQLException {
        String sql = "select * from user ORDERS LIMIT 15 OFFSET ?";
        @Cleanup Connection conn = null;
        @Cleanup PreparedStatement pstmt = null;
        ResultSet rs = null;

        conn = dataSource.getConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1,page * 15);
        rs = pstmt.executeQuery();

        List<UserData> returnValue = new ArrayList<UserData>();

        while(rs.next()) {
            UserData user = UserData.builder()
                    .name(rs.getString("name"))
                    .user_no(rs.getInt("user_no"))
                    .sign(rs.getString("sign"))
                    .department(rs.getString("department"))
                    .role(rs.getString("role"))
                    .admin_role(rs.getString("admin_role"))
                    .regist_date(rs.getString("regist_date"))
                    .before_date(rs.getString("before_date"))
                    .enter_date(rs.getString("enter_date"))
                    .isdelete(rs.getInt("isdelete"))
                    .build();

            returnValue.add(user);
        }

        return returnValue;
    }

    public User findUser(int user_no) throws Exception {
        String sql = "select * from user where user_no = ?;";
        @Cleanup Connection conn = null;
        @Cleanup PreparedStatement pstmt = null;
        ResultSet rs = null;

        conn = dataSource.getConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, user_no);
        rs = pstmt.executeQuery();

        rs.next();
        User returnValue = User.builder()
                    .user_no(rs.getInt("user_no"))
                    .id(rs.getString("id"))
                    .password(rs.getString("password"))
                    .department(rs.getString("department"))
                    .role(rs.getString("role"))
                    .name(rs.getString("name"))
                    .sign(rs.getString("sign"))
                    .admin_role(rs.getString("admin_role"))
                    .enter_date(rs.getDate("enter_date"))
                    .regist_date(rs.getDate("regist_date"))
                    .up_date(rs.getDate("up_date"))
                    .isdelete(rs.getInt("isdelete"))
                    .build();

        return returnValue;
    }

    public RepositoryMessage<User> Login(String id, String password) throws Exception{
        String sql = "select * from user where id = ?";
        @Cleanup Connection conn = null;
        @Cleanup PreparedStatement pstmt = null;
        ResultSet rs = null;

        conn = dataSource.getConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id);
        rs = pstmt.executeQuery();

        RepositoryMessage<User> returnValue = new RepositoryMessage<User>();
        if(rs.next()) {
            User user = User.builder()
                    .user_no(rs.getInt("user_no"))
                    .id(rs.getString("id"))
                    .password(rs.getString("password"))
                    .department(rs.getString("department"))
                    .role(rs.getString("role"))
                    .name(rs.getString("name"))
                    .sign(rs.getString("sign"))
                    .admin_role(rs.getString("admin_role"))
                    .enter_date(rs.getDate("enter_date"))
                    .regist_date(rs.getDate("regist_date"))
                    .up_date(rs.getDate("up_date"))
                    .isdelete(rs.getInt("isdelete"))
                    .build();

            if (passwordEncoder.matches(password, user.getPassword())) {
                returnValue.setSuccess(true);
                returnValue.setMessage("로그인 성공");
                returnValue.setObj(user);
            } else {
                returnValue.setSuccess(false);
                returnValue.setMessage("비밀번호가 일치하지 않습니다");
            }

        } else {
            returnValue.setSuccess(false);
            returnValue.setMessage("존재하지 않는 id 입니다");
        }

        return returnValue;
    }

    public void signup(User user) throws Exception {
        String sql = "INSERT INTO user (id, password, name, department, role, sign) VALUE (?, ?, ?, ?, ?, ?)";
        @Cleanup Connection conn = null;
        @Cleanup PreparedStatement pstmt = null;

        conn = dataSource.getConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, user.getId());
        pstmt.setString(2, passwordEncoder.encode(user.getPassword()));
        pstmt.setString(3, user.getName());
        pstmt.setString(4, user.getDepartment());
        pstmt.setString(5, user.getRole());
        pstmt.setString(6, user.getSign());

        pstmt.executeQuery();
    }

    public void userEdit(User user)  throws Exception {
        if(user.getSign().equals("")) {
            if(user.getPassword().equals("")){
                String sql = "UPDATE user SET name=?, department=?, role=? WHERE user_no = ?";
                @Cleanup Connection conn = null;
                @Cleanup PreparedStatement pstmt = null;

                conn = dataSource.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getDepartment());
                pstmt.setString(3, user.getRole());
                pstmt.setInt(4, user.getUser_no());

                pstmt.executeQuery();
            } else {
                String sql = "UPDATE user SET password=?, name=?, department=?, role=? WHERE user_no = ?";
                @Cleanup Connection conn = null;
                @Cleanup PreparedStatement pstmt = null;

                conn = dataSource.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, passwordEncoder.encode(user.getPassword()));
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getDepartment());
                pstmt.setString(4, user.getRole());
                pstmt.setInt(5, user.getUser_no());

                pstmt.executeQuery();
            }
        }
        else {
            if(user.getPassword().equals("")){
                String sql = "UPDATE user SET name=?, department=?, role=?, sign=? WHERE user_no = ?";
                @Cleanup Connection conn = null;
                @Cleanup PreparedStatement pstmt = null;

                conn = dataSource.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getDepartment());
                pstmt.setString(3, user.getRole());
                pstmt.setString(4, user.getSign());
                pstmt.setInt(5, user.getUser_no());

                pstmt.executeQuery();
            } else {
                String sql = "UPDATE user SET password=?, name=?, department=?, role=?, sign=? WHERE user_no = ?";
                @Cleanup Connection conn = null;
                @Cleanup PreparedStatement pstmt = null;

                conn = dataSource.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, passwordEncoder.encode(user.getPassword()));
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getDepartment());
                pstmt.setString(4, user.getRole());
                pstmt.setString(5, user.getSign());
                pstmt.setInt(6, user.getUser_no());

                pstmt.executeQuery();
            }
        }
    }

    public void setUserTimeData(int user_no, String before_date, String enter_date) throws Exception{
        String sql = "UPDATE user SET before_date = ?, enter_date = ? WHERE user_no = ?";
        @Cleanup Connection conn = null;
        @Cleanup PreparedStatement pstmt = null;

        conn = dataSource.getConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(3, user_no);
        pstmt.setString(1, before_date);
        pstmt.setString(2, enter_date);

        pstmt.executeQuery();
    }

    public UserData getUserDateData(int user_no) throws Exception {
        String sql = "SELECT * from user WHERE user_no = ?";
        @Cleanup Connection conn = null;
        @Cleanup PreparedStatement pstmt = null;
        ResultSet rs = null;

        conn = dataSource.getConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, user_no);
        rs = pstmt.executeQuery();

        UserData returnValue = new UserData();
        if(rs.next()) {
            returnValue = UserData.builder()
                    .before_date(rs.getString("before_date"))
                    .enter_date(rs.getString("enter_date"))
                    .build();

            System.out.println(returnValue);
            return returnValue;
        }

        return returnValue;
    }

    public void setUserDelete(int user_no) throws Exception {
        String sql = "UPDATE user SET isdelete=1 WHERE user_no=?";
        @Cleanup Connection conn = null;
        @Cleanup PreparedStatement pstmt = null;

        conn = dataSource.getConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, user_no);
        pstmt.executeQuery();
    }

    public void setUserRestore(int user_no) throws Exception {
        String sql = "UPDATE user SET isdelete=0 WHERE user_no=?";
        @Cleanup Connection conn = null;
        @Cleanup PreparedStatement pstmt = null;

        conn = dataSource.getConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, user_no);
        pstmt.executeQuery();
    }
}
