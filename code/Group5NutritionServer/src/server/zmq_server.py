'''
Created on Mar 9, 2026

@author: Emi, Justin Smith
'''
import zmq
import json
from model import database
from server import constants, login_authentication_request_handler

def log(message):
    print("SERVER::{0}".format(message))

def run(protocol, ipAddress, port):
    database.loadDefaultData()
    
    context = zmq.Context()
    socket = context.socket(zmq.REP)
    socket.bind("{0}://{1}:{2}".format(protocol, ipAddress, port))
    
    print("Server listening on {0}://{1}:{2}".format(protocol, ipAddress, port))
    
    while True:
        log("waiting for request...")
        message = socket.recv_string()
        request = json.loads(message)
        log("Received request: {0}".format(request))
        
        if(request == "exit"):
            return
        elif(constants.KEY_REQUEST_TYPE not in request):
            response = {constants.KEY_STATUS:constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE:"no request type"}
            json_response = json.dumps(response)
            socket.send_string(json_response)
        elif(request[constants.KEY_REQUEST_TYPE] == constants.AUTHENTICATE_LOGIN_REQUEST_TYPE):
            response = login_authentication_request_handler.handleRequest(request)
            json_response = json.dumps(response)
            socket.send_string(json_response)
            
if __name__ == "__main__":
    run(constants.PROTOCOL, constants.IP_ADDRESS, constants.PORT)