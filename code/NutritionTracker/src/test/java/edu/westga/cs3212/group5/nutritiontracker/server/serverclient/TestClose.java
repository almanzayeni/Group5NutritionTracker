package edu.westga.cs3212.group5.nutritiontracker.server.serverclient;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.zeromq.ZMQ;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

public class TestClose {

	@Test
	public void testCloseSendsSerializedExitCommand() throws Exception {
		AtomicReference<String> receivedRequest = new AtomicReference<String>();
		CountDownLatch serverReady = new CountDownLatch(1);
		Thread serverThread = new Thread(() -> {
			ZMQ.Context context = ZMQ.context(1);
			ZMQ.Socket socket = context.socket(ZMQ.REP);
			socket.bind(ServerConstants.ADDRESS);
			serverReady.countDown();
			byte[] request = socket.recv(0);
			receivedRequest.set(new String(request, ZMQ.CHARSET));
			socket.send("ok".getBytes(ZMQ.CHARSET), 0);
			socket.close();
			context.term();
		});
		serverThread.start();
		assertTrue(serverReady.await(5, TimeUnit.SECONDS));

		ServerClient.close();

		serverThread.join(5000);
		assertFalse(serverThread.isAlive());
		assertEquals("\"exit\"", receivedRequest.get());
	}

	@Test
	public void testCloseHandlesExceptionThrownByObjectMapper() {
		PrintStream originalErr = System.err;
		ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
		System.setErr(new PrintStream(errorOutput));

		try (MockedConstruction<ObjectMapper> mockedMapper = Mockito.mockConstruction(ObjectMapper.class, (mock, context) -> {
			when(mock.writeValueAsString(ServerConstants.EXIT_COMMAND)).thenThrow(new RuntimeException("mapper failed"));
		})) {

			assertDoesNotThrow(() -> {
				ServerClient.close();
			});
			assertEquals(1, mockedMapper.constructed().size());
			assertTrue(errorOutput.toString().contains("mapper failed"));
		} finally {
			System.setErr(originalErr);
		}
	}

}
