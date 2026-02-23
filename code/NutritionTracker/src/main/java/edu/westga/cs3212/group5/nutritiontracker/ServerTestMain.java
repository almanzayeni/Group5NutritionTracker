package edu.westga.cs3212.group5.nutritiontracker;

import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.nutritiontracker.server.dto.PingRequest;
import edu.westga.cs3212.nutritiontracker.server.dto.PingResponse;

public class ServerTestMain {

    public static void main(String[] args) throws Exception {
        ServerClient client = new ServerClient();
        PingResponse res = client.send(new PingRequest(), PingResponse.class);

        System.out.println(res.status + " " + res.message);
    }
}