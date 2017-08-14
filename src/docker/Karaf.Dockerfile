FROM java:8-jdk
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

ENV KARAF_VERSION=4.1.2

RUN mkdir -p /opt/karaf

RUN groupadd -r karaf -g 1000 && useradd -u 1000 -r -g karaf -m -d /opt/karaf -s /sbin/nologin -c "Karaf user" karaf && \
chmod 755 /opt/karaf

WORKDIR /opt/karaf

RUN wget http://apache.lauf-forum.at/karaf/${KARAF_VERSION}/apache-karaf-${KARAF_VERSION}.tar.gz; \
    tar --strip-components=1 -C /opt/karaf -xzf apache-karaf-${KARAF_VERSION}.tar.gz; \
    rm apache-karaf-${KARAF_VERSION}.tar.gz;

COPY wait-for-karaf-start.sh /opt/karaf/
RUN chmod +x wait-for-karaf-start.sh

COPY karaf-features-install.sh /opt/karaf/
RUN chmod +x karaf-features-install.sh

RUN mkdir -p /opt/karaf/builds

RUN chown -R karaf /opt/karaf


EXPOSE 1099 8101 8181 4444
USER karaf
CMD /opt/karaf/bin/karaf server
