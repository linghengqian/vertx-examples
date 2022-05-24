package io.vertx.example.grpc.pingpong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxPingPongServiceGrpc;
import io.vertx.example.util.Runner;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.client.GrpcClientChannel;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ClientWithStub extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(ClientWithStub.class);
  }

  @Override
  public void start() {

    // Create the channel
    GrpcClient client = GrpcClient.client(vertx);
    GrpcClientChannel channel = new GrpcClientChannel(client, SocketAddress.inetSocketAddress(8080, "localhost"));

    // Get a stub to use for interacting with the remote service
    VertxPingPongServiceGrpc.PingPongServiceVertxStub stub = VertxPingPongServiceGrpc.newVertxStub(channel);

    // Make a request
    Messages.SimpleRequest request = Messages.SimpleRequest.newBuilder().setFillUsername(true).build();

    // Call the remote service
    stub.unaryCall(request).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("My username is: " + ar.result().getUsername());
      } else {
        System.out.println("Coult not reach server " + ar.cause().getMessage());
      }
    });
  }
}
