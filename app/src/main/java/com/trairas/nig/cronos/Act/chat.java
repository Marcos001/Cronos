package com.trairas.nig.cronos.Act;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.trairas.nig.cronos.R;
import com.trairas.nig.cronos.Util.OperArquivos;
import com.trairas.nig.cronos.Util.util;
import com.trairas.nig.cronos.MB.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class chat extends AppCompatActivity {


    util u = new util();
    OperArquivos opr = new OperArquivos();


    private String chat_ip;
    private String chat_name;
    public String ip_amiguinho="";

    Button bt_1 ;
    EditText ed_1;

    LinearLayout linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat);


        bt_1 = (Button) findViewById(R.id.chat_bt_1);
        ed_1 = (EditText) findViewById(R.id.chat_ed_1);
        linear = (LinearLayout) findViewById(R.id._linear_layout);

        u.print("<---------Iniciando Consumer->>>>>>>>>>");
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Consumidor();
            }
        }).start();

        String dados_sesao = opr.ler(chat.this, "ses.txt");

        u.print("dados = "+dados_sesao);

        String[] dados = getDados(dados_sesao);

        chat_ip = dados[0];
        chat_name = dados[1];

        u.print(" ip "+chat_ip+" nome = "+chat_name);

        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagem(ed_1.getText().toString().trim());
            }
        });


        u.print("ip = "+getIp());

        u.print("Iniciando Consumidor");



    }

    public String getIp(){

        String ipAddress = null;
        Enumeration<NetworkInterface> net = null;
        try {
            net = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        while (net.hasMoreElements()) {
            NetworkInterface element = net.nextElement();
            Enumeration<InetAddress> addresses = element.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();

                if (ip.isSiteLocalAddress()) {
                    ipAddress = ip.getHostAddress();
                }
            }
        }
        return ipAddress;
    }

    private void enviarMensagem(final String trim) {

        if(trim.length() > 0){

        new Thread(new Runnable() {
            @Override
            public void run() {
                //adicionar o ip

                new Produtor(getIp()+":"+trim+"§");
            }
        }).start();



            //enviar ao servidor

            LinearLayout.LayoutParams layt = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView tv = new TextView(chat.this);
            layt.gravity = Gravity.RIGHT;
            layt.setMargins(10,10,10,10);
            tv.setText(trim);
            tv.setPadding(10,10,10,10);
            tv.setBackgroundResource(R.color.verde);

            TextView tv_l = new TextView(chat.this);
            tv_l.setText("");


            linear.addView(tv);
            linear.addView(tv_l);

            ed_1.setText("");

        }

    }

    private String[] getDados(String dados_sesao) {

        String[] deira = new String[2];

        String tmp="";


        for(int i=0;i<dados_sesao.length();i++){


            if(dados_sesao.charAt(i) == ':'){
                deira[0] = tmp;
                tmp="";
            }

            else if(dados_sesao.charAt(i) == '.'){
                deira[1] = tmp;
                tmp="";
            }

            else if (dados_sesao.charAt(i) != ':'){
                tmp += ""+dados_sesao.charAt(i);
            }
        }

        return deira;
    }

    public class Consumidor {

        util u = new util();

        private final static String QUEUE_NAME = "enviar";
        final String HOST = System.getProperty("amqp.host", "192.168.0.104");
        final int PORT = Integer.getInteger("amqp.port", 5672);
        final String EXCHANGE = System.getProperty("amqp.exchange", "systemExchange");
        final String ENCODING = "UTF-8";
        Connection connection;


        public Consumidor(){

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
                System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                            throws IOException {

                        String message = new String(body, "UTF-8");

                        u.print("Mensagem recebida = "+message);

                        String ip = "";
                        String msg = "";
                        String tmp = "";

                        for(int i=0;i<message.length();i++){

                            if(message.charAt(i)==':'){
                                ip = tmp;
                                tmp = "";
                            }

                            else if(message.charAt(i)=='§'){
                                msg = tmp;
                                tmp = "";
                            }

                            else if(message.charAt(i)!=':'){
                                tmp += message.charAt(i);
                            }
                        }

                        u.print("IP = "+ip+"\n"+"Nome = "+msg);

                        u.print("Instanciando os textos");

                        TextView tv = new TextView(chat.this);
                        tv.setText(ip+" enviou "+msg);
                        tv.setPadding(10,10,10,10);
                        tv.setBackgroundResource(R.color.zul);

                        TextView tv_l = new TextView(chat.this);
                        tv_l.setText("");

                        u.print("Adicionando no Linear Layout");

                        linear.addView(tv);
                        linear.addView(tv_l);

                        u.print(" [x] Received '" + message + "'");
                    }
                };

                channel.basicConsume(QUEUE_NAME, true, consumer);
            }catch (Exception erro){
                u.print("Erro ao Instanciar COnsumer()");
            }

        }


    }



}
