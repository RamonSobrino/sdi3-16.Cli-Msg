package com.sdi.client.actions;

import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import alb.util.console.Console;
import alb.util.menu.Action;

import com.sdi.client.model.Task;
import com.sdi.client.util.Jndi;

public class VistaTareasRetrasadasAction implements Action {

	private static final String JMS_CONNECTION_FACTORY =
			"jms/RemoteConnectionFactory";
	private static final String NOTANEITOR_QUEUE = "jms/queue/TaskManager";

	private Connection con;
	private Session session;
	private MessageProducer sender;
	private MessageConsumer consumer;

	@Override
	public void execute() throws Exception {
		try {
			String user = Console.readString("Usuario");
			String password = Console.readString("password");

			initialize();

			MapMessage msg = createMessage(user, password);
			Destination queue = this.session.createTemporaryQueue();
			msg.setJMSReplyTo( queue);

			sender.send(msg);

			consumer = session.createConsumer(queue);

			Message msgRecibido = consumer.receive();

			List<Task> tasks = procesarMensaje(msgRecibido);

			Console.println("Tareas");
			Console.printf("%6s %20s %20s %20s\n", "Id", "Titulo", 
					"Fecha Creacion", "Planeada");
			for (Task task : tasks) {
				Console.printf("%6d %20s %20s %20s\n", task.getId(), 
						task.getTitle(), task.getCreated(),
						task.getPlanned().toString());
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private List<Task> procesarMensaje(Message msgRecibido) 
			throws JMSException {
		if (!messageOfExpectedType(msgRecibido)) { 
			System.out.println("Not of expected type " + msgRecibido);
			return null;
		}

		ObjectMessage msg= (ObjectMessage) msgRecibido;

		Object ob = msg.getObject();
		if (!messageOfExpectedType(ob)) { 
			System.out.println("Not of expected type " + ob);
			return null;
		}

		@SuppressWarnings("unchecked")
		List<Task> lista = (List<Task>)ob;
		return lista;
	}

	private boolean messageOfExpectedType(Message msgRecibido) {
		return msgRecibido instanceof ObjectMessage;
	}

	private boolean messageOfExpectedType(Object objeto) {
		if(objeto instanceof List<?>){
			List<?> lista = (List<?>)objeto;
			if(lista.get(0) instanceof Task)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else{
			return false;
		}
	}

	private MapMessage createMessage(String user, String password) 
			throws JMSException {
		MapMessage msg = session.createMapMessage();
		msg.setString("command", "TareasRetrasadas");
		msg.setString("user", user);
		msg.setString("password", password);


		return msg;		
	}

	private void initialize() throws JMSException {
		ConnectionFactory factory =
				(ConnectionFactory) Jndi.find( JMS_CONNECTION_FACTORY );
		Destination queue = (Destination) Jndi.find( NOTANEITOR_QUEUE );
		con = factory.createConnection("sdi","password");
		session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		sender = session.createProducer(queue);
		con.start();
	}
}
