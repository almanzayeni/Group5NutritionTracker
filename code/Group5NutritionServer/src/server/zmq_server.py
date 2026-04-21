'''
Created on Mar 9, 2026

@author: Emi, Justin Smith, Yeni Almanza
'''
import zmq
import json
from model import database
from server import constants, login_authentication_request_handler
from server import create_account_request_handler
from server import search_request_handler
from server import add_food_request_handler
from server import update_foodlog_request_handler

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
        
        if(request == constants.EXIT_COMMAND):
            log("Exiting server...")
            response = {constants.KEY_STATUS:constants.SUCCESS_STATUS, constants.KEY_SUCCESS_MESSAGE:constants.KEY_SERVER_EXIT}
            sendResponse(socket, response)
            return
        elif(constants.KEY_REQUEST_TYPE not in request):
            response = {constants.KEY_STATUS:constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE:"no request type"}
            sendResponse(socket, response)
        elif(request[constants.KEY_REQUEST_TYPE] == constants.AUTHENTICATE_LOGIN_REQUEST_TYPE):
            response = login_authentication_request_handler.handleRequest(request)
            sendResponse(socket, response) 
        elif(request[constants.KEY_REQUEST_TYPE] == constants.CREATE_ACCOUNT_REQUEST_TYPE):
            response = create_account_request_handler.handleRequest(request)
            sendResponse(socket, response)
        elif(request[constants.KEY_REQUEST_TYPE] == constants.SEARCH_REQUEST_TYPE):
            response = search_request_handler.handleRequest(request)
            sendResponse(socket, response)
        elif request[constants.KEY_REQUEST_TYPE] == constants.ADD_FOOD_REQUEST_TYPE:
            response = add_food_request_handler.handleRequest(request)
            sendResponse(socket, response)
        elif request[constants.KEY_REQUEST_TYPE] == constants.UPDATE_FOODLOG_REQUEST_TYPE:
            response = update_foodlog_request_handler.handleRequest(request)
            sendResponse(socket, response)
        else:
            response = {constants.KEY_STATUS:constants.UNSUPPORTED_OPERATION_STATUS, constants.KEY_FAILURE_MESSAGE:"unsupported request type"}
            sendResponse(socket, response)
          
def sendResponse(socket, response):
    log("Response: {0}".format(response))
    json_response = json.dumps(response)
    socket.send_string(json_response)
    
if __name__ == "__main__":
    run(constants.PROTOCOL, constants.IP_ADDRESS, constants.PORT)