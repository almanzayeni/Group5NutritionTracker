'''
Created on Mar 30, 2026

@author: OpenAI
'''
import unittest

from model.quantity_category import QuantityCategory


class TestQuantityCategory(unittest.TestCase):

    def test_enum_members_keep_expected_values(self):
        self.assertEqual("QUANTITY", QuantityCategory.QUANTITY.value)
        self.assertEqual("WEIGHT", QuantityCategory.WEIGHT.value)
        self.assertEqual("SERVING", QuantityCategory.SERVING.value)


if __name__ == "__main__":
    unittest.main()
