import zmq
import json

BIND_ADDR = "tcp://127.0.0.1:5555"

def run():
    context = zmq.Context.instance()
    socket = context.socket(zmq.REP)
    socket.bind(BIND_ADDR)
    
    print("Server listening on ", BIND_ADDR)
    
    while True:
        message = socket.recv_string()
        req = json.loads(message)

        action = req.get("action")
        if action == "ping":
            res = {"status": "ok", "message": "pong"}
        else:
            res = {"status": "error", "message": "unknown action"}
            
        socket.send_string(json.dumps(res))
            
if __name__ == "__main__":
    run()