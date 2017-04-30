package uo.sdi.client.actions;


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
import javax.jms.TextMessage;

import uo.sdi.client.util.Jndi;
import uo.sdi.client.util.MsgConfig;
import uo.sdi.dto.Task;
import alb.util.console.Console;
import alb.util.menu.Action;

public class VistaTareasHoyAction implements Action {

	private Connection con;
	private Session session;
	private MessageProducer sender;
	private MessageConsumer consumer;
	private MsgConfig mconf;

	@Override
	public void execute() throws Exception {
		try {
			this.mconf = MsgConfig.getInstance();

			String user = mconf.getUser();
			String password = mconf.getPassword();

			initialize();

			MapMessage msg = createMessage(user, password);
			Destination queue = this.session.createTemporaryQueue();
			msg.setJMSReplyTo( queue);

			sender.send(msg);

			consumer = session.createConsumer(queue);

			Message msgRecibido = consumer.receive();

			if(msgRecibido instanceof TextMessage)
			{
				System.out.println("Se ha enviado mensaje de error");
				return;
			}
			List<Task> tasks = procesarMensaje(msgRecibido);

			
			if(tasks==null)
			{
				System.out.println("Error el mendaje no se puede procesar");
				return;
			}
			this.session.close();
			this.con.close();
			Console.println("Tareas");
			Console.printf("%6s %20s %20s %20s\n", "Id", "Titulo",
					"Fecha Creacion", "Planeada");
			for (Task task : tasks) {
				Console.printf("%6d %20s %20s %20s\n", task.getId(), 
						task.getTitle(), task.getCreated(),
						task.getPlanned().toString());
			}
			close();

		} catch (JMSException e) {
			close();
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
		
		@SuppressWarnings("unchecked")
		List<Task> lista =  (List<Task>) msg.getObject();
				
		return lista;
	}

	private boolean messageOfExpectedType(Message msgRecibido) {
		return msgRecibido instanceof ObjectMessage;
	}

	
	private MapMessage createMessage(String user, String password) 
			throws JMSException {
		MapMessage msg = session.createMapMessage();
		msg.setString("command", "TareasHoy");
		msg.setString("user", user);
		msg.setString("password", password);


		return msg;		
	}

	private void initialize() throws JMSException {
		ConnectionFactory factory =
				(ConnectionFactory) Jndi.find(
						this.mconf.getJMS_CONNECTION_FACTORY());
		Destination queue = (Destination) Jndi.find(
				this.mconf.getNOTANEITOR_QUEUE());
		con = factory.createConnection("sdi","password");
		session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		sender = session.createProducer(queue);
		con.start();
	}

	private void close() throws JMSException
	{
		this.consumer.close();
		this.sender.close();
		this.session.close();
		this.con.close();
	}
}
