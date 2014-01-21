package com.bobpaulin.sling.websocket.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;

import com.bobpaulin.sling.websocket.SlingWebsocketService;
import com.bobpaulin.sling.websocket.listeners.TestWebSocketListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

@Component(metatype=true, immediate=true)
@Service
public class SlingWebsocketServiceImpl implements SlingWebsocketService{
	
	private SocketIOServer server;
	
	@Reference
	private SlingRepository repository;
	
	public void activate(ComponentContext context) throws Exception
	{
		Configuration config = new Configuration();
	    config.setHostname("localhost");
	    config.setPort(83);
	    config.setAllowCustomRequests(true);
	    
	    server = new SocketIOServer(config);
	    
	    server.addListeners(new TestWebSocketListener(repository));
	    
	    server.start();
	    
	    System.out.println("Web Socket Server Started");

	}
	
	public void deactivate(ComponentContext context)
	{
		server.stop();
		System.out.println("Web Socket Server Stopped");
	}
	
	public void setRepository(SlingRepository repository) {
		this.repository = repository;
	}

}
