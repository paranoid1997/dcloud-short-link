FROM adoptopenjdk/openjdk8:jre8u-nightly
COPY target/dcloud-account.jar dcloud-account.jar
COPY agent /usr/local/agent
ENV SW_AGENT_NAME "dcloud-account"
ENTRYPOINT ["java","-javaagent:/usr/local/agent/skywalking-agent.jar","-jar","/dcloud-account.jar"]