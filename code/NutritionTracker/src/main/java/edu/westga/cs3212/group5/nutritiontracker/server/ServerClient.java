package edu.westga.cs3212.group5.nutritiontracker.server;

import org.zeromq.ZMQ;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class ServerClient.
 * 
 * @author Emi & Justin Smith
 * @version spring 2026
 */
public class ServerClient extends Thread {

	/**
	 * Send a request to the server and receive the response.
	 * 
	 * @precondition request != null && !request.isBlank()
	 *
	 * @param request the request to send to the server as a JSON string
	 * @return the string response from the server
	 * @throws IllegalArgumentException if request is null or blank
	 * @throws Exception the exception if there is an error sending the request or receiving the response
	 */
	public static String send(String request) throws Exception {
		if (request == null || request.isBlank()) {
			throw new IllegalArgumentException("Request cannot be null or blank");
		}
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket socket = context.socket(ZMQ.REQ);
		socket.connect(ServerConstants.ADDRESS);

		System.out.println("Client - Sending " + request);
		socket.send(request.getBytes(ZMQ.CHARSET), 0);

		byte[] reply = socket.recv(0);
		String response = new String(reply, ZMQ.CHARSET);
		System.out.println("Client - Received " + response);

		socket.close();
		context.term();

		return response;
	}

	/**
	 * Sends an exit command to the server to gracefully shut it down.
	 */
	public static void close() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String exitRequest = mapper.writeValueAsString(ServerConstants.EXIT_COMMAND);
			ServerClient.send(exitRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
