FROM gradle:5.5.1-jdk8
COPY . /home/ep-rest
WORKDIR /home/ep-rest
RUN gradle build
CMD ["./start.sh"]