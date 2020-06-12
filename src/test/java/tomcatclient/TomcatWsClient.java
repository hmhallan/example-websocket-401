package tomcatclient;

import org.junit.Test;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class TomcatWsClient {

    @Test
    public void test_connect_on_tomcat_client_ws() throws URISyntaxException, IOException, InterruptedException, DeploymentException {

        final AtomicReference<String> message = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);

        Endpoint endpoint = new Endpoint() {
            @Override
            public void onClose(final Session session,
                                final CloseReason closeReason) {
                super.onClose(session, closeReason);
                System.out.println("onClose: " + closeReason);

            }

            @Override
            public void onError(final Session session,
                                final Throwable throwable) {
                super.onError(session, throwable);
                System.out.println("onError: " + throwable);
            }

            @Override
            public void onOpen(final Session session,
                               final EndpointConfig endpointConfig) {
                session.addMessageHandler(new Whole<String>() {
                    @Override
                    public void onMessage(final String content) {
                        message.set(content);
                        latch.countDown();
                    }
                });
            }
        };

        ClientEndpointConfig authorizationConfiguration = ClientEndpointConfig.Builder.create()
                .build();

        URI uri = new URI("ws://localhost:8080/example-websocket-401/ws/chat");

        Session session = ContainerProvider.getWebSocketContainer()
                .connectToServer(
                        endpoint,
                        authorizationConfiguration,
                        uri
                );

        session.close();
    }
}
