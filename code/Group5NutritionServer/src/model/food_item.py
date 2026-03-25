'''
Created on Mar 22, 2026

@author: Justin Smith
'''

from __future__ import annotations

from abc import ABC, abstractmethod


class FoodItem(ABC):
    @abstractmethod
    def get_description(self):
        pass

    @abstractmethod
    def get_quantity_category(self):
        pass

    @abstractmethod
    def get_portion_size(self):
        pass

    @abstractmethod
    def get_calories(self):
        pass

    @abstractmethod
    def get_protein(self):
        pass

    @abstractmethod
    def get_fat(self):
        pass

    @abstractmethod
    def get_sugar(self):
        pass

    @abstractmethod
    def get_carbohydrates(self):
        pass

    @abstractmethod
    def get_sodium(self):
        pass