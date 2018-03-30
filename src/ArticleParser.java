import objects.User;

import java.util.ArrayList;

public class ArticleParser {

    public static void main(String[] args) {

        DatabaseAccess newDa = new DatabaseAccess();
        XMLFactory xmlFactory = new XMLFactory();

        try {
            //data from the db
            ArrayList<User> newUsers = newDa.getUsers();
            for (User u : newUsers) {
                xmlFactory.createXML(u, newDa.getArticles(u.getId()), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
