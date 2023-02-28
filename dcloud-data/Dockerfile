FROM adoptopenjdk/openjdk8:jre8u-nightly
COPY target/dcloud-data.jar dcloud-data.jar
COPY agent /usr/local/agent
ENV SW_AGENT_NAME "dcloud-data"
ENTRYPOINT ["java","-javaagent:/usr/local/agent/skywalking-agent.jar","-jar","/dcloud-data.jar"]