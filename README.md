mvn spring-boot:run

java -cp "customer-service/target/customer-service-1.0-SNAPSHOT.jar;java-agent/target/lib/*" -javaagent:java-agent/target/java-agent-1.0-SNAPSHOT.jar org.springframework.boot.loader.JarLauncher
