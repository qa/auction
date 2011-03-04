#!/bin/sh

SERVER_HOME=$HOME/releases/jboss-6.0.0.Final

java -Djava.ext.dirs=$SERVER_HOME/client -cp ../../../target/test-classes:../../../target/classes org.jboss.lectures.client.TopicReceiverClient
