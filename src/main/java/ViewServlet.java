import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import tirnak.persistence.model.Post;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(
        name = "index",
        urlPatterns = {"/"}
)
public class ViewServlet extends HttpServlet {

    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int postsToDisplay = 0;
        try {
            postsToDisplay = Integer.parseInt(req.getParameter("limit"));
        } catch (Exception ignored) {}
        handle(req, resp, postsToDisplay,
            "from Post p where p.author in (select max(p.author) from Post p) and p.parent = null");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String queryS = "";
        try {
            queryS = req.getParameter("query");
        } catch (Exception ignored) {}
        handle(req, resp, 0, queryS);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp, int postsToDisplay,  String queryS) throws ServletException, IOException {
        Session session = sessionFactory.openSession();
        System.out.println(System.currentTimeMillis() + ": after open session()");
        Query query = session.createQuery(queryS);
        if (postsToDisplay > 0 ) {
            query.setMaxResults(postsToDisplay);
        }
        List<Post> posts = query.list();
        System.out.println("there are " + posts.size() + " posts");
        req.setAttribute("posts", posts);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/vk_persisted.jsp");
        dispatcher.forward(req, resp);
        System.out.println(System.currentTimeMillis() + ": after forward and before session.close()");
        session.close();
        System.out.println(System.currentTimeMillis() + ": after session.close()");
    }

}
