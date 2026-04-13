'''
Created on Mar 30, 2026

@author: OpenAI
'''
import unittest
from unittest.mock import patch

from model.base_food import BaseFood
from model.composite_food import CompositeFood
from model.quantity_category import QuantityCategory
from server import constants
from server import search_request_handler


class TestSearchRequestHandler(unittest.TestCase):

    def _create_banana(self):
        return BaseFood(
            "banana",
            QuantityCategory.QUANTITY,
            1,
            105,
            1,
            0,
            14,
            27,
            1,
        )

    def _create_hamburger(self):
        beef_patty = BaseFood(
            "beef patty",
            QuantityCategory.WEIGHT,
            1,
            250,
            20,
            15,
            0,
            0,
            80,
        )
        pickles = BaseFood(
            "pickles",
            QuantityCategory.WEIGHT,
            1,
            10,
            0,
            0,
            2,
            3,
            300,
        )
        cheese_slice = BaseFood(
            "cheese slice",
            QuantityCategory.QUANTITY,
            1,
            70,
            5,
            6,
            0,
            1,
            150,
        )
        ketchup = BaseFood(
            "ketchup",
            QuantityCategory.WEIGHT,
            1,
            20,
            0,
            0,
            5,
            5,
            150,
        )
        hamburger_bun = BaseFood(
            "hamburger bun",
            QuantityCategory.QUANTITY,
            1,
            120,
            4,
            2,
            3,
            22,
            200,
        )
        return CompositeFood(
            "hamburger",
            QuantityCategory.SERVING,
            1,
            500,
            30,
            25,
            5,
            10,
            500,
            {
                beef_patty.get_description(): beef_patty,
                pickles.get_description(): pickles,
                cheese_slice.get_description(): cheese_slice,
                ketchup.get_description(): ketchup,
                hamburger_bun.get_description(): hamburger_bun,
            },
        )

    def test_rejects_none_request(self):
        with self.assertRaisesRegex(Exception, "request is None"):
            search_request_handler.handleRequest(None)

    def test_requires_query(self):
        with self.assertRaisesRegex(Exception, "request does not contain query"):
            search_request_handler.handleRequest({})

    def test_returns_serialized_base_food_from_database_search(self):
        banana = self._create_banana()

        with patch(
            "server.search_request_handler.database.searchFoodItemByDescription",
            return_value=[banana],
        ) as search_food_item_by_description:
            response = search_request_handler.handleRequest(
                {constants.KEY_QUERY: "ban"}
            )

        search_food_item_by_description.assert_called_once_with("ban")
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertEqual([banana.to_dict()], response[constants.KEY_SEARCH_RESULTS])

    def test_returns_serialized_composite_food_from_database_search(self):
        hamburger = self._create_hamburger()

        with patch(
            "server.search_request_handler.database.searchFoodItemByDescription",
            return_value=[hamburger],
        ) as search_food_item_by_description:
            response = search_request_handler.handleRequest(
                {constants.KEY_QUERY: "ham"}
            )

        search_food_item_by_description.assert_called_once_with("ham")
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            [hamburger.to_dict()],
            response[constants.KEY_SEARCH_RESULTS],
        )

    def test_returns_serialized_base_and_composite_food_list_from_database_search(self):
        banana = self._create_banana()
        hamburger = self._create_hamburger()

        with patch(
            "server.search_request_handler.database.searchFoodItemByDescription",
            return_value=[banana, hamburger],
        ) as search_food_item_by_description:
            response = search_request_handler.handleRequest(
                {constants.KEY_QUERY: "a"}
            )

        search_food_item_by_description.assert_called_once_with("a")
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            [banana.to_dict(), hamburger.to_dict()],
            response[constants.KEY_SEARCH_RESULTS],
        )


if __name__ == "__main__":
    unittest.main()
