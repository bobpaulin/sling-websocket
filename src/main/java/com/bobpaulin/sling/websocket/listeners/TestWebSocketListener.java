package com.bobpaulin.sling.websocket.listeners;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.jcr.api.SlingRepository;

import com.bobpaulin.sling.websocket.data.TestData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.annotation.OnMessage;

public class TestWebSocketListener {
	
	private SlingRepository repository;
	
	public TestWebSocketListener(SlingRepository repository) throws RepositoryException
	{
		this.repository = repository;
		
	}
	
	 @OnConnect
     public void onConnectHandler(SocketIOClient client) {
		 System.out.println("Web Socket Connected");
     
     }

     @OnDisconnect
     public void onDisconnectHandler(SocketIOClient client) {
    	 System.out.println("Web Socket Disconnected");
     }
     
     @OnEvent(value="message_event")
     public void onSomeEvent(TestData data) throws Exception
     {
    	 System.out.println("Event - Message: " + data.getMessage() + " Recieved from: " + data.getUser());
    	 Session session = repository.loginAdministrative(null);
    	 Node messageRoot = null;
    	 messageRoot = getMessageRoot(session);
    	 
    	 Node messageNode = messageRoot.addNode("message" + System.currentTimeMillis());
    	 
    	 messageNode.setProperty("user", data.getUser());
    	 messageNode.setProperty("message", data.getMessage());
    	 session.save();
    	 session.logout();
 
     }

	private synchronized Node getMessageRoot(Session session) throws RepositoryException {
		Node messageRoot;
		if(!session.getRootNode().hasNode("messages"))
    	 {
    		 messageRoot = session.getRootNode().addNode("messages");
    	 }
    	 else
    	 {
    		messageRoot = session.getRootNode().getNode("messages"); 
    	 }
		return messageRoot;
	}
     
     
     @OnMessage
     public void onMessage(String data)
     {
    	 System.out.println("Message: " + data);
     }
    
}
