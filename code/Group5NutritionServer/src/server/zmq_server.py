import zmq

BIND_ADDR = "tcp://127.0.0.1:5555"

def run():
    context = zmq.Context.instance()
    socket = context.socket(zmq.REP)
    socket.bind(BIND_ADDR)
    
    print("Server listening on ", BIND_ADDR)
    
    while True:
        message = socket.recv_string()
        print("Recieved: ", message)
        
        if message == "PING":
            socket.send_string("PONG")
        else:
            socket.send_string("UNKNOWN")
            
if __name__ == "__main__":
    run()