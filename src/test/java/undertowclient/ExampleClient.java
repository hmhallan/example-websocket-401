package undertowclient;

import java.io.IOException;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;

public class ExampleClient extends AbstractReceiveListener {

    @Override
    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) throws IOException {
        String msg = message.getData();
        System.out.println("client rcv=" + msg);
    }

    @Override
    protected void onError(WebSocketChannel channel, Throwable error) {
        System.out.println("error=" + error.getMessage());
        super.onError(channel, error);
        error.printStackTrace();
    }
}
