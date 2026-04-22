'''
Created on Apr 3, 2026

@author: vfilpo :)/ Justin Smith
'''

from model import database
from model.base_food import BaseFood
from model.composite_food import CompositeFood
from model.quantity_category import QuantityCategory
from server import constants

def handleRequest(request):
    if constants.KEY_FOOD_ITEM not in request:
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: "no food item"}
    
    quantity_category_str = request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_QUANTITY_CATEGORY]
    if quantity_category_str == QuantityCategory.SERVING.value:
        quantity_category_enum = QuantityCategory.SERVING
    elif quantity_category_str == QuantityCategory.QUANTITY.value:
        quantity_category_enum = QuantityCategory.QUANTITY
    elif quantity_category_str == QuantityCategory.WEIGHT.value:
        quantity_category_enum = QuantityCategory.WEIGHT
    
    if request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_TYPE] == constants.KEY_BASE_FOOD_TYPE:
        base_food = BaseFood(
            description=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_DESCRIPTION],
            quantity_category=quantity_category_enum,
            portion_size=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_PORTION_SIZE],
            calories=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_CALORIES],
            protein=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_PROTEIN],
            fat=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_FAT],
            sugar=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_SUGAR],
            carbohydrates=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_CARBS],
            sodium=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_SODIUM]
        )
        
        database.addFoodItem(base_food)
        
    elif request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_TYPE] == constants.KEY_COMPOSITE_FOOD_TYPE:
        ingredients_dict = {}
        for ingredient_description in request[constants.KEY_FOOD_ITEM][constants.KEY_INGREDIENTS]:
            if ingredient_description not in database.getFoodItems():
                return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: f"ingredient '{ingredient_description}' not found in database"}
            ingredients_dict[ingredient_description] = database.getFoodItems()[ingredient_description]
        
        composite_food = CompositeFood(
            description=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_DESCRIPTION],
            quantity_category=quantity_category_enum,
            portion_size=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_PORTION_SIZE],
            calories=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_CALORIES],
            protein=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_PROTEIN],
            fat=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_FAT],
            sugar=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_SUGAR],
            carbohydrates=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_CARBS],
            sodium=request[constants.KEY_FOOD_ITEM][constants.KEY_FOOD_SODIUM],
            ingredients= ingredients_dict
        )
        
        database.addFoodItem(composite_food)
        
    else:
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: "invalid food type"}
    
    return {constants.KEY_STATUS: constants.SUCCESS_STATUS}