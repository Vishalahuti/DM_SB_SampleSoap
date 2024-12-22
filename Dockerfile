FROM openjdk:17-jdk-slim
EXPOSE 8082
ADD target/DM_SB_SampleSoap.jar DM_SB_SampleSoap.jar
ENTRYPOINT ["java","-jar","/DM_SB_SampleSoap.jar"]
