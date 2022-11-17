package backend.databaseservice;

import backend.pojos.Post;
import backend.pojos.Request;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbServices {

    private static String url = "jdbc:sqlite:microblog.db";

    public static void main(String[] args) {
    }
    public synchronized static void removeSub(String userName, String sub) {
     //DELETE FROM artists_backup
        //WHERE artistid = 1;

        String sql = "DELETE FROM subs WHERE name=? AND sub=?;";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, sub);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public synchronized static String retrieveSubs(String userName) {
        String sql = "SELECT sub FROM subs WHERE name='" + userName + "';";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.isBeforeFirst()) return null;

            List<String> list = new ArrayList();
            while(rs.next()) {
                String sub = rs.getString("sub");
                list.add(sub);
            }

            return String.join(",",list);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("problem with connecting to server. Couldn't retrive post");
            return null;
        }


    }

        public synchronized static void addSub(String username, String sub) {

        String sql = "INSERT INTO subs(name,sub) VALUES(?,?);";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, sub);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized static boolean checkPostExists(String id) {
        String sql = "SELECT content FROM posts where msgid='" + id + "';";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.isBeforeFirst()) return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("problem with connecting to server. Couldn't check pass");
            return false;
        }
        return true;
    }

    public synchronized static boolean checkPass(String username, String pass) {
         String url = "jdbc:sqlite:database.db";

        String sql = "SELECT pass FROM users where name='" + username + "';";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.isBeforeFirst()) return false;
            rs.next();

            if (!rs.getString(1).equals(pass)) return false;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("problem with connecting to server. Couldn't check pass");
            return false;
        }
        return true;
    }

    public synchronized static void addMessage(String msgid, String content, long date, String user,String author, String replyto) {
        ArrayList<String> list = extractTags(content);
        String sql = "INSERT INTO posts(msgid,content,date,user,author,replyto) VALUES(?,?,?,?,?,?);";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, msgid);
            pstmt.setString(2, content);
            pstmt.setLong(3, date);
            pstmt.setString(4, user);
            pstmt.setString(5, author);
            pstmt.setString(6, replyto);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (list.size() > 0) {

            for (String tag : list) {
                try (Connection conn2 = DriverManager.getConnection(url);
                     PreparedStatement pstmt2 = conn2.prepareStatement("INSERT INTO keywords(word,msgid) VALUES(?,?);")) {
                    pstmt2.setString(1, tag);
                    pstmt2.setString(2, msgid);
                    pstmt2.executeUpdate();//
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private synchronized static ArrayList<String> extractTags(String content) {
        ArrayList<String> list = new ArrayList<>();
        String regex = "#(\\w+)";
        Matcher matcher = Pattern.compile(regex).matcher(content);
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }

    public synchronized static String retrieveIds(Request req) {
        String sql = "SELECT posts.msgid FROM posts";
        String where = " WHERE ";
        String and = " AND ";

        if (!req.getTag().isBlank())
            sql += " INNER JOIN keywords \n" +
                    " ON keywords.msgid = posts.msgid WHERE word='" + req.getTag() + "'";
        if (!req.getAuthor().isBlank())
            sql += " " + (sql.contains("WHERE") ? and : where) + " posts.user='" + req.getAuthor() + "'";
        if (req.getSince_id() != 0L)
            sql += " " + (sql.contains("WHERE") ? and : where) + " posts.date > " + req.getSince_id();
        sql += " ORDER by posts.date DESC";
        if (req.getLimit() != 0)
            sql += " LIMIT " + req.getLimit();


        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.isBeforeFirst()) return "";//if no records

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                String msgid = rs.getString("msgid");
                sb.append(msgid).append(", ");
            }
            sb.append("\b\b");
            return sb.toString();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("problem with connecting to server. Couldn't retrive post");
            return "";
        }
    }

    public synchronized static Post retrievePost(String id) {
        String sql = "SELECT * FROM posts where msgid='" + id + "';";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.isBeforeFirst()) return null;

            rs.next();
            String msgid = rs.getString("msgid");
            String content = rs.getString("content");
            long date = rs.getLong("date");
            String user = rs.getString("user");
            String author = rs.getString("author");
            String replyto = rs.getString("replyto");
            return new Post(msgid, content, date, user,author,replyto);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("problem with connecting to server. Couldn't retrive post");
            return null;
        }

    }

}
