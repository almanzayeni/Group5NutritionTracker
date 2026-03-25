package edu.westga.cs3212.group5.nutritiontracker.server.serverclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.zeromq.ZMQ;

import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestSend {

	@Test
	public void testNullRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			ServerClient.send(null);
		});
	}

	@Test
	public void testBlankRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			ServerClient.send("   ");
		});
	}

	@Test
	public void testValidRequestReturnsResponse() throws Exception {
		String request = "{\"request_type\":\"AUTH\"}";
		String expectedResponse = "{\"status\":\"1\"}";
		ZMQ.Context context = mock(ZMQ.Context.class);
		ZMQ.Socket socket = mock(ZMQ.Socket.class);

		try (MockedStatic<ZMQ> zmqMock = Mockito.mockStatic(ZMQ.class)) {
			zmqMock.when(() -> ZMQ.context(1)).thenReturn(context);
			when(context.socket(ZMQ.REQ)).thenReturn(socket);
			when(socket.recv(0)).thenReturn(expectedResponse.getBytes(ZMQ.CHARSET));

			String result = ServerClient.send(request);

			assertEquals(expectedResponse, result);
			verify(socket, times(1)).connect(ServerConstants.ADDRESS);
			verify(socket, times(1)).send(request.getBytes(ZMQ.CHARSET), 0);
			verify(socket, times(1)).recv(0);
			verify(socket, times(1)).close();
			verify(context, times(1)).term();
		}
	}

}
