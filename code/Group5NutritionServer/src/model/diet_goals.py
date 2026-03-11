'''
Created on Mar 11, 2026

@author: Justin Smith
'''

class DietGoals(object):
    '''
    Stores the diet goals for a user.
    '''


    def __init__(self, primaryGoal, calorieGoal, proteinGoal, fatGoal, sugarGoal, sodiumGoal, carbsGoal, otherGoals):
        '''
        Create a new DietGoals with the provided information.
        
        @precondition primaryGoal != None &&
                      calorieGoal != None && calorieGoal >= 0 &&
                      proteinGoal != None && proteinGoal >= 0 &&
                      fatGoal != None && fatGoal >= 0 &&
                      sugarGoal != None && sugarGoal >= 0 &&
                      sodiumGoal != None && sodiumGoal >= 0 &&
                      carbsGoal != None && carbsGoal >= 0 &&
                      otherGoals != None && !otherGoals.contains(None)
                      
        @postcondition getPrimaryGoal() == primaryGoal &&
                        getCalorieGoal() == calorieGoal &&
                        getProteinGoal() == proteinGoal &&
                        getFatGoal() == fatGoal &&
                        getSugarGoal() == sugarGoal &&
                        getSodiumGoal() == sodiumGoal &&
                        getCarbsGoal() == carbsGoal &&
                        getOtherGoals() == otherGoals
                        
        @param primaryGoal the primary diet goal for the user
        @param calorieGoal the calorie goal for the user
        @param proteinGoal the protein goal for the user
        @param fatGoal the fat goal for the user
        @param sugarGoal the sugar goal for the user
        @param sodiumGoal the sodium goal for the user
        @param carbsGoal the carbs goal for the user
        @param otherGoals a list of other diet goals for the user
        '''
        if (primaryGoal == None):
            raise Exception("primary goal is None")
        if (calorieGoal == None):
            raise Exception("calorie goal is None")
        if (calorieGoal < 0):
            raise Exception("calorie goal is negative")
        if (proteinGoal == None):
            raise Exception("protein goal is None")
        if (proteinGoal < 0):
            raise Exception("protein goal is negative")
        if (fatGoal == None):
            raise Exception("fat goal is None")
        if (fatGoal < 0):
            raise Exception("fat goal is negative")
        if (sugarGoal == None):
            raise Exception("sugar goal is None")
        if (sugarGoal < 0):
            raise Exception("sugar goal is negative")
        if (sodiumGoal == None):
            raise Exception("sodium goal is None")
        if (sodiumGoal < 0):
            raise Exception("sodium goal is negative")
        if (carbsGoal == None):
            raise Exception("carbs goal is None")
        if (carbsGoal < 0):
            raise Exception("carbs goal is negative")
        if (otherGoals == None):
            raise Exception("other goals is None")
        if (None in otherGoals):
            raise Exception("other goals contains None")
        
        self._primaryGoal = primaryGoal
        self._calorieGoal = calorieGoal
        self._proteinGoal = proteinGoal
        self._fatGoal = fatGoal
        self._sugarGoal = sugarGoal
        self._sodiumGoal = sodiumGoal
        self._carbsGoal = carbsGoal
        self._otherGoals = otherGoals
        
    def getPrimaryGoal(self):
        '''
        Get the primary diet goal for the user.
        
        @return the primary diet goal for the user
        '''
        return self._primaryGoal
    
    def getCalorieGoal(self):
        '''
        Get the calorie goal for the user.
        
        @return the calorie goal for the user.
        '''
        return self._calorieGoal
    
    def getProteinGoal(self):
        '''
        Get the protein goal for the user.
        
        @return the protein goal for the user.
        '''
        return self._proteinGoal
    
    def getFatGoal(self):
        '''
        Get the fat goal for the user.
        
        @return the fat goal of the user.
        '''
        return self._fatGoal
    
    def getSugarGoal(self):
        '''
        Get the sugar goal for the user.
        
        @return the sugar goal for the user.
        '''
        return self._sugarGoal
    
    def getSodiumGoal(self):
        '''
        Get the sodium goal for the user.
        
        @return the sodium goal for the user.
        '''
        return self._sodiumGoal
    
    def getCarbsGoal(self):
        '''
        Get the carbs goal for the user.
        
        @return the carbs goal for the user.
        '''
        return self._carbsGoal
    
    def getOtherGoals(self):
        '''
        Get the other diet goals for the user.
        
        @return the other diet goals for the user.
        '''
        return self._otherGoals