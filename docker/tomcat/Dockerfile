FROM tomcat:10-jdk17
RUN apt-get update
RUN apt-get upgrade -y
EXPOSE 8080
COPY ./target/bankingapp.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "jpda", "run"]