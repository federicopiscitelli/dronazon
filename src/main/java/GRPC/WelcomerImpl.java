package GRPC;

import Welcome.*;
import io.grpc.stub.StreamObserver;

public class WelcomerImpl extends WelcomerGrpc.WelcomerImplBase {

    @Override
    public void welcome(WelcomeMessage request, StreamObserver<WelcomeResponse> responseObserver) {
        WelcomeResponse response = Welcome.WelcomeResponse.newBuilder().setMessage().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
