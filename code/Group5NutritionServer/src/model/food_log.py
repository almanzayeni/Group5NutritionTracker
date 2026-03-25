'''
Created on Mar 9, 2026

@author: Justin Smith
'''
from datetime import date as dateTime

class FoodLog(object):
    '''
    Hold the log of food items consumed by a user for a given day.
    '''


    def __init__(self, date, breakfast, lunch, dinner, snacks):
        '''
        Create a new FoodLog with the provided information.
        
        @precondition date != None && date <= today &&
                      breakfast != None && !breakfast.contains(None) &&
                      lunch != None && !lunch.contains(None) &&
                      dinner != None && !dinner.contains(None) &&
                      snacks != None && !snacks.contains(None)
                      
        @postcondition getDate() == date &&
                       getBreakfast() == breakfast &&
                       getLunch() == lunch &&
                       getDinner() == dinner &&
                       getSnacks() == snacks
                       
        @param date date for the food log
        @param breakfast list of food items consumed for breakfast
        @param lunch list of food items consumed for lunch
        @param dinner list of food items consumed for dinner
        @param snacks list of food items consumed for snacks
        '''
        today = dateTime.today()
        if (date == None):
            raise Exception("date is None")
        if (date > today):
            raise Exception("date is in the future")
        if (breakfast == None):
            raise Exception("breakfast is None")
        if (None in breakfast):
            raise Exception("breakfast contains None")
        if (lunch == None):
            raise Exception("lunch is None")
        if (None in lunch):
            raise Exception("lunch contains None")
        if (dinner == None):
            raise Exception("dinner is None")
        if (None in dinner):
            raise Exception("dinner contains None")
        if (snacks == None):
            raise Exception("snacks is None")
        if (None in snacks):
            raise Exception("snacks contains None")
        
        self._date = date
        self._breakfast = breakfast
        self._lunch = lunch
        self._dinner = dinner
        self._snacks = snacks
        
    def getDate(self):
        '''
        Get the date for this food log.
        
        @return the date for this food log
        '''
        return self._date
    
    def getBreakfast(self):
        '''
        Get the list of food items consumed for breakfast.
        
        @return the list of food items consumed for breakfast
        '''
        return self._breakfast
    
    def getLunch(self):
        '''
        Get the list of food items consumed for lunch.
        
        @return the list of food items consumed for lunch
        '''
        return self._lunch
    
    def getDinner(self):
        '''
        Get the list of food items consumed for dinner.
        
        @return the list of food items consumed for dinner
        '''
        return self._dinner
    
    def getSnacks(self):
        '''
        Get the list of food items consumed for snacks.
        
        @return the list of food items consumed for snacks
        '''
        return self._snacks
    
    def toDict(self):
        return {
            "date": self.getDate().isoformat(),
            "breakfast": [food.to_dict() for food in self._breakfast],
            "lunch": [food.to_dict() for food in self._lunch],
            "dinner": [food.to_dict() for food in self._dinner],
            "snacks": [food.to_dict() for food in self._snacks]
        }