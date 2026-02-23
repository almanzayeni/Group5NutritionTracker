package edu.westga.cs3212.group5.nutritiontracker.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.zeromq.ZMQ;

public class ServerClient {
	private static final String ADDRESS = "tcp://127.0.0.1:5555";
	private final ObjectMapper mapper = new ObjectMapper();
	
	public <TResponse> TResponse send(Object requestObj, Class<TResponse> responseClass) throws Exception {
		try (ZMQ.Context context = ZMQ.context(1);
				ZMQ.Socket socket = context.socket(ZMQ.REQ)) {
			socket.connect(ADDRESS);
			
			String requestJson = mapper.writeValueAsString(requestObj);
			socket.send(requestJson.getBytes(ZMQ.CHARSET), 0);
			
			byte[] reply = socket.recv(0);
			String responseJson = new String(reply, ZMQ.CHARSET);
			
			return mapper.readValue(responseJson, responseClass);
		}
	}
}
