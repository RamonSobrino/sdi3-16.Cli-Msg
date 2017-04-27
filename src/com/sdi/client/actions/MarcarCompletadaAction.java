package com.sdi.client.actions;



import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import alb.util.console.Console;
import alb.util.menu.Action;

import com.sdi.client.util.Jndi;

public class MarcarCompletadaAction implements Action {

	private static final String JMS_CONNECTION_FACTORY =
			"jms/RemoteConnectionFactory";
	private static final String NOTANEITOR_QUEUE = "jms/queue/ClientQueue";

	private Connection con;
	private Session session;
	private MessageProducer sender;
	private MessageConsumer consumer;

	@Override
	public void execute() throws Exception {
		try{
			Long id = Console.readLong("Id de la tarea");


			String user = Console.readString("Usuario");
			String password = Console.readString("password");

			initialize();

			MapMessage msg = createMessage(user, password, id);
			Destination queue = this.session.createTemporaryQueue();
			msg.setJMSReplyTo( queue);

			sender.send(msg);

			consumer = session.createConsumer(queue);

			Message msgRecibido = consumer.receive();

			if(procesarMensaje(msgRecibido))
			{
				Console.println("Nueva tarea creada");
			}
			else
			{
				Console.println("Nueva tarea No creada");
			}

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Boolean procesarMensaje(Message msgRecibido) {
		// TODO Auto-generated method stub
		return null;
	}

	private MapMessage createMessage(String user, String password, Long id) throws JMSException {
		MapMessage msg = session.createMapMessage();
		msg.setString("command", "FinalizarTarea");
		msg.setString("user", user);
		msg.setString("password", password);
		msg.setString("id", ""+id);


		return msg;		
	}

	private void initialize() throws JMSException {
		ConnectionFactory factory =
				(ConnectionFactory) Jndi.find( JMS_CONNECTION_FACTORY );
		Destination queue = (Destination) Jndi.find( NOTANEITOR_QUEUE );
		con = factory.createConnection();
		session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		sender = session.createProducer(queue);
		con.start();
	}
}
