'''
Created on Mar 9, 2026

@author: Justin Smith
'''

class User(object):
    '''
    Models a user.
    '''


    def __init__(self, name, username, password, currentFoodLog, dietGoals):
        '''
        Create a new User with the provided information.
        
        @precondition name != None &&
                      username != None &&
                      password != None &&
                      currentFoodLog != None &&
                      dietGoals != None
                      
        @postcondition getName() == name &&
                       getUsername() == username &&
                       getPassword() == password &&
                       getCurrentFoodLog() == currentFoodLog &&
                       getDietGoals() == dietGoals
                       
        @param name name of the user
        @param username username for the user
        @param password password for the user
        @param currentFoodLog food log for the user
        @param dietGoals diet goals for the user
        '''
        if (name == None):
            raise Exception("name is None")
        if (username == None):
            raise Exception("username is None")
        if (password == None):
            raise Exception("password is None")
        if (currentFoodLog == None):
            raise Exception("food log is None")
        if (dietGoals == None):
            raise Exception("diet goals is None")
        
        self._name = name
        self._username = username
        self._password = password
        self._currentFoodLog = currentFoodLog
        self._dietGoals = dietGoals
        self._storedFoodLogs = {currentFoodLog.getDate(): currentFoodLog}
        
    def getName(self):
        '''
        Get the name of the user.
        
        @return the name of the user.
        '''
        return self._name
    
    def getUsername(self):
        '''
        Get the username of the user.
        
        @return the username of the user.
        '''
        return self._username
    
    def getPassword(self):
        '''
        Get the password of the user.
        
        @return the password of the user.
        '''
        return self._password
    
    def getCurrentFoodLog(self):
        '''
        Get the current food log of the user.
        
        @return the current food log of the user
        '''
        return self._currentFoodLog
    
    def setCurrentFoodLog(self, foodLog):
        '''
        Set the current food log of the user.
        
        @precondition foodLog != None
        
        @param foodLog the food log to set as the current food log of the user
        '''
        if (foodLog == None):
            raise Exception("food log is None")
        self._currentFoodLog = foodLog
    
    def getStoredFoodLogs(self):
        '''
        Get the stored food logs of the user.
        
        @return the stored food logs of the user
        '''
        return self._storedFoodLogs
    
    def addFoodLog(self, foodLog):
        '''
        Add a food log to the stored food logs of the user.
        
        @precondition foodLog != None
        
        @param foodLog the food log to add to the stored food logs of the user
        '''
        if (foodLog == None):
            raise Exception("food log is None")
        self._storedFoodLogs[foodLog.getDate()] = foodLog
    
    def getDietGoals(self):
        '''
        Get the diet goals of the user.
        
        @return the diet goals of the user.
        '''
        return self._dietGoals
    
    def setDietGoals(self, dietGoals):
        '''
        Set the diet goals of the user.
        
        @precondition dietGoals != None
        
        @param dietGoals the diet goals to set for the user
        '''
        if (dietGoals == None):
            raise Exception("diet goals is None")
        self._dietGoals = dietGoals
    
    def toDict(self):
        return {
            "name": self.getName(),
            "username": self.getUsername(),
            "password": self.getPassword(),
            "currentFoodLog": self.getCurrentFoodLog().toDict(),
            "dietGoals": self.getDietGoals().toDict()
        }