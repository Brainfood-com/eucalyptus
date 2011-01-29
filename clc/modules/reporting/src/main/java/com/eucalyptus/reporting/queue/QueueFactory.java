package com.eucalyptus.reporting.queue;


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eucalyptus.reporting.event.*;

public class QueueFactory
{
	private static Logger log = Logger.getLogger( QueueFactory.class );

	private static QueueFactory queueFactory = null;

	public static QueueFactory getInstance()
	{
		if (queueFactory == null) {
			queueFactory = new QueueFactory();
		}
		return queueFactory;
	}
	
	private static String clientUrl = "failover:(" + QueueBroker.DEFAULT_URL + ")?initialReconnectDelay=10000&maxReconnectAttempts=10";

	private Map<QueueIdentifier,QueueSenderImpl>   senders;
	private Map<QueueIdentifier,QueueReceiverImpl> receivers;
	private boolean started = false;
	
	private QueueFactory()
	{
		this.senders   = new HashMap<QueueIdentifier,QueueSenderImpl>();
		this.receivers = new HashMap<QueueIdentifier,QueueReceiverImpl>();
	}

	public enum QueueIdentifier
	{
		INSTANCE("InstanceQueue"),
		STORAGE("StorageQueue");
		
		private final String queueName;

		private QueueIdentifier(String queueName)
		{
			this.queueName = queueName;
		}

		public String getQueueName()
		{
			return this.queueName;
		}
	}

	public void startup()
	{
		if (!started) {
			started = true;
			log.info("QueueFactory started");
		} else {
			log.warn("QueueFactory started redundantly");
		}
	}
	
	public void shutdown()
	{
		if (started) {
			for (QueueIdentifier identifier : senders.keySet()) {
				senders.get(identifier).shutdown();
			}
			for (QueueIdentifier identifier : receivers.keySet()) {
				receivers.get(identifier).shutdown();
			}
			log.info("QueueFactory stopped");
		} else {
			log.warn("QueueFactory.shutdown called when not started");
		}
	}
	
	public QueueSender getSender(QueueIdentifier identifier)
	{
		if (senders.containsKey(identifier)) {
			return senders.get(identifier);
		} else {
			log.info("Client url:" + clientUrl);
			QueueSenderImpl sender = new QueueSenderImpl(clientUrl, identifier);
			sender.startup();
			senders.put(identifier, sender);
			log.info("Sender " + identifier + " started");
			return sender;
		}
	}

	public QueueReceiver getReceiver(QueueIdentifier identifier)
	{
		if (receivers.containsKey(identifier)) {
			return receivers.get(identifier);
		} else {
			log.info("Client url:" + clientUrl);
			QueueReceiverImpl receiver = new QueueReceiverImpl(clientUrl,
					identifier);
			receiver.startup();
			receivers.put(identifier, receiver);
			log.info("Receiver " + identifier + " started");
			return receiver;
		}		
	}
	
	public static void main(String[] args)
		throws Exception
	{
		QueueIdentifier identifier =
			(args[0].equalsIgnoreCase("storage"))
				? QueueIdentifier.STORAGE
				: QueueIdentifier.INSTANCE;
		boolean listener = (args[1].equalsIgnoreCase("nowait")) ? false : true;
		System.out.println("Running listener for queue " + identifier + " as " + (listener ? "listener" : "noWait"));
		QueueFactory queueFactory = QueueFactory.getInstance();
		QueueReceiver receiver = queueFactory.getReceiver(identifier);
		if (listener) {
			receiver.addEventListener(new EventListener()
			{
				@Override
				public void fireEvent(Event e)
				{
					System.out.println("Event received:" + e);
				}

			});
		} else {
			for (Event event = receiver.receiveEventNoWait();
					event != null;
					event = receiver.receiveEventNoWait())
			{
				System.out.println("Event received:" + event);				
			}

		}
	}

}
