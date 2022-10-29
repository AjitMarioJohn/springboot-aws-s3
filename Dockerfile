FROM openjdk:17-alpine@sha256:a996cdcc040704ec6badaf5fecf1e144c096e00231a29188596c784bcf858d05
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
WORKDIR /workspace/app
MAINTAINER Ajit Mario John
RUN chown -R javauser:javauser /workspace/app
USER javauser
COPY target/*.jar springbootawss3.jar
EXPOSE 9090
CMD ["java", "-jar", "springbootawss3.jar"]