package org.jboss.lectures.client;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class TopicReceiverClient {

        public static void main(String[] args) {
                try {
                        InitialContext ctx = new InitialContext();
                        ConnectionFactory connectionFactory = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
                        Connection connection = connectionFactory.createConnection();
                        connection.start();
                        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                        Topic topic = (Topic)ctx.lookup("/topic/AuctionNotification");
                        MessageConsumer consumer = session.createConsumer(topic);
                        MapMessage message = (MapMessage)consumer.receive();
                        System.out.println("User " + message.getString("user"));
                        consumer.close();
                        session.close();
                        connection.stop();
                        connection.close();
                }
                catch (Exception e) {
                        e.printStackTrace();
                }

        }

}
