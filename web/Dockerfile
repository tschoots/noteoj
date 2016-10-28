FROM java:openjdk-8-jdk-alpine

EXPOSE 8080

WORKDIR /code
ADD ./noteoj_web /code
ADD ./templates /code/templates

ENTRYPOINT [ "./noteoj_web" ]
