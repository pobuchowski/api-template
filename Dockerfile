FROM java:8

WORKDIR /deployments
CMD mkdir ssl 
COPY ssl/truststore.p12 ssl/truststore.p12
COPY target/api-template.jar /deployments/app.jar

CMD java -jar app.jar
