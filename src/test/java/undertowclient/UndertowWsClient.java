package undertowclient;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.client.WebSocketClient;
import io.undertow.websockets.core.WebSocketChannel;
import org.junit.Test;
import org.xnio.OptionMap;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UndertowWsClient {

    public static final int BUFFER_SIZE = Integer.getInteger("test.bufferSize", 1024 * 16 - 20);

    @Test
    public void test_connect_on_ws() throws URISyntaxException, IOException {
        Xnio xnio = Xnio.getInstance(UndertowWsClient.class.getClassLoader());
        XnioWorker worker = xnio.createWorker(OptionMap.builder().getMap());

        URI endpoint = new URI("ws://localhost:8080/example-websocket-401/ws/chat");

        DefaultByteBufferPool pool = new DefaultByteBufferPool(true, BUFFER_SIZE, 1000, 10, 100);
        WebSocketChannel channel = WebSocketClient.connectionBuilder(worker, pool, endpoint)
                                                    .connect().get();
        System.out.println("Connected to server!");
        
        channel.getReceiveSetter().set(new ExampleClient());
        channel.resumeReceives();
    }
}
