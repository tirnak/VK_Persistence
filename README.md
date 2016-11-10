# VK_Persistence
Selenium is used to save/remove images, posts etc. made by VK user
Currently it is used to save posts from user wall.

requisites:
* Google Chrome 54 && chromedriver v2.24 (others may be compatible, but not guaranteed)
* Java 8
* Maven 3

usage:
```
$ git clone ... && cd ...
$ nano/vim/gedit src/main/resources/empty.properties # write login/pass to your vk account and path to chrome driver
$ cp src/main/resources/empty.properties src/main/resources/credentials.properties 
$ nano/vim/gedit src/main/resources/hibernate.cfg.xml # edit hibernate configuration if needed. Default DB is Java-embedded HSQL.
$ mvn package
$ cd target
$ java -cp app-jar-with-dependencies.jar Saver # save text posts from your account's wall
$ java -jar app-jar-with-dependencies.jar # run embedded tomcat to see what is saved
```

Open localhost:8080 to see posts parsed. And then you can erase those posts from your wall


