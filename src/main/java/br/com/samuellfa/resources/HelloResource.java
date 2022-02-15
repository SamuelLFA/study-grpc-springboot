package br.com.samuellfa.resources;

import br.com.samuellfa.HelloReq;
import br.com.samuellfa.HelloRes;
import br.com.samuellfa.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class HelloResource extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(HelloReq request, StreamObserver<HelloRes> responseObserver) {
        var response = HelloRes.newBuilder()
                .setMessage(request.getMessage())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
