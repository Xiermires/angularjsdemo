# angularjsdemo
A little web app mixing angularjs / vert.x / JPA (H2)

This web app allows to create Event tasks (by default unassigned), which later an user can assign to himself.

Created using Maven, Eclipse Luna, Java 8.

Persistance is set to 'drop-and-create', it takes place so far the process is alive.

To use, either import into Eclipse and run src/main/java as an application, or use 'mvn package' to create the .jar && dependencies in the target/ folder and run it via 'java -jar angularjs-demo...jar'. Finally open 'localhost:8080' in your browser.

