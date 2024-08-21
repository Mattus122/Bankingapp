FROM tomcat:10-jdk17

RUN apt-get update
# will update the local package
RUN apt-get upgrade -y
#it will upgrade packages inside the image

EXPOSE 8080
#EXPOSE 18000

#ENV JPDA_ADDRESS 18000
#ENV JPDA_TRANSPORT=dt_socket
# for debugging

# RUN apt-get update -y && apt-get install tcpflow vim curl -y

# COPY ./nextgenpsd2/target/nextgenpsd2-pisp.war /usr/local/tomcat/webapps/nextgenpsd2-pisp.war
COPY ./target/bankingapp-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
# COPY ./stet/target/stet-pisp.war /usr/local/tomcat/webapps/stet-pisp.war

# make Tomcat start with JPDA (Java Platform Debugger Architecture)
CMD ["catalina.sh", "jpda", "run"]