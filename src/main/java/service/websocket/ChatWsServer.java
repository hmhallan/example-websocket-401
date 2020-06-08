package service.websocket;


import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint( value="/ws/chat" )
public class ChatWsServer {

    //all endpoint clients
    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    @OnMessage
    public void receiveMessage(String message, Session session) throws 	Exception {
        this.broadcast(message);
    }

    @OnOpen
    public void open(Session session) {
        clients.add(session);
    }

    @OnClose
    public void close(Session session, CloseReason c) {
        clients.remove(session);
    }

    private synchronized void broadcast(String mensagem) throws Exception {
        for(Session client : clients){
            client.getBasicRemote().sendText(mensagem);
        }
    }

}
