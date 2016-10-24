import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Post> posts = session.createQuery("from Post ").list();
        req.setAttribute("posts", posts);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/vk_persisted.jsp");
        dispatcher.forward(req, resp);
        System.out.println(System.currentTimeMillis() + ": after forward and before session.close()");
        session.close();
        System.out.println(System.currentTimeMillis() + ": after session.close()");
    }

}
