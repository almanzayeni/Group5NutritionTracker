'''
Created on Mar 30, 2026

@author: OpenAI
'''
import unittest
from unittest.mock import patch

from server import constants
from server import search_request_handler


class _FakeFoodItem(object):

    def __init__(self, serialized):
        self._serialized = serialized

    def toDict(self):
        return self._serialized


class TestSearchRequestHandler(unittest.TestCase):

    def test_rejects_none_request(self):
        with self.assertRaisesRegex(Exception, "request is None"):
            search_request_handler.handleRequest(None)

    def test_requires_query(self):
        with self.assertRaisesRegex(Exception, "request does not contain query"):
            search_request_handler.handleRequest({})

    def test_returns_serialized_food_items_from_database_search(self):
        banana = _FakeFoodItem({"description": "banana"})

        with patch(
            "server.search_request_handler.database.searchFoodItemByDescription",
            return_value=[banana],
        ) as search_food_item_by_description:
            response = search_request_handler.handleRequest(
                {constants.KEY_QUERY: "ban"}
            )

        search_food_item_by_description.assert_called_once_with("ban")
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertEqual([banana.toDict()], response["food_items"])


if __name__ == "__main__":
    unittest.main()
