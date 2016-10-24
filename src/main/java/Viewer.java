import org.apache.catalina.startup.Tomcat;

import java.util.Optional;

public class Viewer {
    public static final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));

    public static void main(String[] args) throws Exception {
        String contextPath = "/";
        String appBase = ".";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.valueOf(port.orElse("8080")));
        tomcat.getHost().setAppBase(appBase);
        tomcat.addWebapp(contextPath, appBase);
        tomcat.start();
        tomcat.getServer().await();
    }

}
