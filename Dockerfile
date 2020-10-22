FROM java:8
VOLUME /tmp
ADD target/minischool-0.0.1-SNAPSHOT.jar minischool.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dfile.encoding=UTF8","-Dsun.jnu.encoding=UTF8","-jar","/minischool.jar","--spring.profiles.active=dev"]