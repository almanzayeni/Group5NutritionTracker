'''
Created on Mar 30, 2026

@author: Justin Smith
'''

from model import database
from server import constants

def handleRequest(request):
    '''
    Searches the database for food items matching the provided query and returns the appropriate response.
    
    @precondition request != None &&
                  request contains key constants.KEY_QUERY
                  
    @return a response containing the status of the request and, if successful, a list of food items matching the provided query.
    '''
    if (request == None):
        raise Exception("request is None")
    if (constants.KEY_QUERY not in request):
        raise Exception("request does not contain query")
    
    query = request[constants.KEY_QUERY]
    
    matchingFoodItems = database.searchFoodItemByDescription(query)
    
    return {constants.KEY_STATUS:constants.SUCCESS_STATUS, constants.KEY_SEARCH_RESULTS:[foodItem.toDict() for foodItem in matchingFoodItems]}