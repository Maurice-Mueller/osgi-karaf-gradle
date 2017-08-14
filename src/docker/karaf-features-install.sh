#!/usr/bin/env bash
cd /opt/karaf/bin

CXF_VERSION="3.1.10"
JACKSON_VERSION="2.8.5"

./client feature:install webconsole
./client feature:repo-add cxf $CXF_VERSION
./client feature:install http cxf-jaxrs cxf
./client feature:repo-add mvn:org.code-house.jackson/features/$JACKSON_VERSION/xml/features
./client feature:install jackson-jaxrs-json-provider jetty
