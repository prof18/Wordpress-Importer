import objects.Article;
import objects.User;
import org.apache.commons.lang3.StringEscapeUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseAccess {

    private Connection conn = null;
    private Statement stm = null;
    private ResultSet resultSet = null;
    private String dbName = "";
    private String user = "";
    private String password = "";

    private final String connAddress = "jdbc:mysql://localhost/" + dbName + "?user=" + user + "&password=" + password;

    private final String userQuery = "SELECT  id, username, email, name, surname FROM users";

    private final String articleQuery = "SELECT id,title, pub_date, author, description, content, tag, categories FROM articles" +
            "WHERE users.id =";

    public ArrayList<User> getUsers() throws Exception {

        ArrayList<User> users = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(connAddress);
            stm = conn.createStatement();
            resultSet = stm.executeQuery(userQuery);

            while (resultSet.next()) {

                User user = new User();

                int id = resultSet.getInt("id");
                user.setId(id);
                String username = resultSet.getString("username");
                user.setUsername(username);
                String name = resultSet.getString("name");
                user.setName(name);
                String surname = resultSet.getString("surname");
                user.setSurname(surname);
                user.setEmail("email");
                user.setVisibleName(name + " " + surname);
                users.add(user);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

        return users;
    }

    public ArrayList<Article> getArticles(int userId) throws Exception {

        ArrayList<Article> articles = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(connAddress);
            stm = conn.createStatement();
            resultSet = stm.executeQuery(articleQuery + userId);

            while (resultSet.next()) {

                Article article = new Article();

                //id
                int id = resultSet.getInt("id");
                article.setId(id);

                //title
                String title = resultSet.getString("title");
                article.setTitle(StringEscapeUtils.unescapeHtml4(title));

                //date
                Timestamp timestamp = resultSet.getTimestamp("pub_date");
                Date pubDate = new Date(timestamp.getTime());
                article.setPubDate(pubDate);

                //description
                String description = resultSet.getString("description");
                article.setDescription(StringEscapeUtils.unescapeHtml4(description));

                //body
                String body = resultSet.getString("content");
                article.setContent(StringEscapeUtils.unescapeHtml4(body));

                //tags
                ArrayList<String> tags = new ArrayList<>();
                String[] tagsArray = resultSet.getString("tag").split(",");
                Collections.addAll(tags, tagsArray);
                article.setTag(tags);

                //categories
                ArrayList<String> categories = new ArrayList<>();
                String categoryArray = resultSet.getString("categories");
                Collections.addAll(categories, categoryArray);
                article.setCategories(categories);

                articles.add(article);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

        return articles;
    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (stm != null) {
                stm.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
