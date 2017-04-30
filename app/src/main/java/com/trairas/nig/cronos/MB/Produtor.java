package com.trairas.nig.cronos.MB;

/**
 * Created by nig on 25/04/17.
 */

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.trairas.nig.cronos.Util.util;


public class Produtor {

    util u = new util();

    private final static String QUEUE_NAME = "hello";
    static final String HOST = System.getProperty("amqp.host", "192.168.0.104");
    static final int PORT = Integer.getInteger("amqp.port", 5672);
    static final String EXCHANGE = System.getProperty("amqp.exchange", "systemExchange");
    static final String ENCODING = "UTF-8";
    Connection connection;




    public Produtor(String fuxico){

        try{

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(HOST);
            factory.setPort(PORT);
            factory.setUsername("nig");
            factory.setPassword("nig");
            factory.setVirtualHost("/");
            connection = factory.newConnection();

            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, fuxico.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + fuxico + "'");

            channel.close();
            connection.close();

        }catch (Exception erro){
            u.print("Erro ao instanciar publisher > \n"+erro);
        }

    }

}
