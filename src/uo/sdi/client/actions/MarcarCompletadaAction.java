package uo.sdi.client.actions;



import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import uo.sdi.client.util.Jndi;
import uo.sdi.client.util.MsgConfig;
import alb.util.console.Console;
import alb.util.menu.Action;

public class MarcarCompletadaAction implements Action {

	private Connection con;
	private Session session;
	private MessageProducer sender;
	private MessageConsumer consumer;
	private MsgConfig mconf;
	
	@Override
	public void execute() throws Exception {
		try{
			Long id = Console.readLong("Id de la tarea");

			this.mconf = MsgConfig.getInstance();

			String user = mconf.getUser();
			String password = mconf.getPassword();

			initialize();

			MapMessage msg = createMessage(user, password, id);
			Destination queue = this.session.createTemporaryQueue();
			msg.setJMSReplyTo( queue);

			sender.send(msg);

			consumer = session.createConsumer(queue);

			Message msgRecibido = consumer.receive();

			if(procesarMensaje(msgRecibido))
			{
				Console.println("Tarea marcada como finalizada");
			}
			else
			{
				Console.println("Tarea NO marcada como finalizada");
			}
			
			close();

		} catch (JMSException e) {
			close();
			e.printStackTrace();
		}
	}

	private Boolean procesarMensaje(Message msgRecibido) throws JMSException {
		if (!messageOfExpectedType(msgRecibido)) { 
			System.out.println("Not of expected type " + msgRecibido);
			return false;
		}

		TextMessage msg= (TextMessage) msgRecibido;

		if(msg.getText().contains("Error")){
			return false;
		}

		return true;
	}

	private MapMessage createMessage(String user, String password, Long id) 
			throws JMSException {
		MapMessage msg = session.createMapMessage();
		msg.setString("command", "FinalizarTarea");
		msg.setString("user", user);
		msg.setString("password", password);
		msg.setString("id", ""+id);


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

	private boolean messageOfExpectedType(Message m) {
		return m instanceof TextMessage;
	}
	
	private void close() throws JMSException
	{
		this.consumer.close();
		this.sender.close();
		this.session.close();
		this.con.close();
	}
}
