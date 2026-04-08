'''
Created on Mar 30, 2026

@author: OpenAI
'''
import importlib
import json
import runpy
import sys
import types
import unittest
from unittest.mock import call
from unittest.mock import patch

from server import constants


class _FakeSocket(object):

    def __init__(self, messages):
        self._messages = list(messages)
        self.bound_address = None
        self.sent_messages = []

    def bind(self, address):
        self.bound_address = address

    def recv_string(self):
        return self._messages.pop(0)

    def send_string(self, message):
        self.sent_messages.append(message)


class _FakeContext(object):

    def __init__(self, socket):
        self._socket = socket
        self.requested_socket_type = None

    def socket(self, socket_type):
        self.requested_socket_type = socket_type
        return self._socket


class TestZmqServer(unittest.TestCase):

    def _load_module(self, fake_socket):
        fake_zmq = types.SimpleNamespace(
            Context=lambda: _FakeContext(fake_socket),
            REP="REP",
        )
        patcher = patch.dict(sys.modules, {"zmq": fake_zmq})
        patcher.start()
        self.addCleanup(patcher.stop)
        sys.modules.pop("server.zmq_server", None)
        self.addCleanup(lambda: sys.modules.pop("server.zmq_server", None))
        return importlib.import_module("server.zmq_server")

    def _run_server_with_messages(self, messages):
        fake_socket = _FakeSocket(messages)
        zmq_server = self._load_module(fake_socket)
        return zmq_server, fake_socket

    def test_log_formats_messages_with_server_prefix(self):
        zmq_server, _ = self._run_server_with_messages([json.dumps(constants.EXIT_COMMAND)])

        with patch("builtins.print") as print_mock:
            zmq_server.log("waiting")

        print_mock.assert_called_once_with("SERVER::waiting")

    def test_send_response_serializes_payload_to_json(self):
        zmq_server, _ = self._run_server_with_messages([json.dumps(constants.EXIT_COMMAND)])
        socket = _FakeSocket([])

        with patch.object(zmq_server, "log") as log_mock:
            zmq_server.sendResponse(socket, {constants.KEY_STATUS: constants.SUCCESS_STATUS})

        log_mock.assert_called_once_with("Response: {'status': 1}")
        self.assertEqual(['{"status": 1}'], socket.sent_messages)

    def test_run_returns_when_exit_command_is_received(self):
        zmq_server, socket = self._run_server_with_messages(
            [json.dumps(constants.EXIT_COMMAND)]
        )

        with patch.object(zmq_server.database, "loadDefaultData") as load_default_data, patch(
            "builtins.print"
        ):
            result = zmq_server.run("tcp", "127.0.0.1", "5555")

        self.assertIsNone(result)
        load_default_data.assert_called_once_with()
        self.assertEqual("tcp://127.0.0.1:5555", socket.bound_address)

    def test_run_returns_bad_message_when_request_type_is_missing(self):
        zmq_server, socket = self._run_server_with_messages(
            [json.dumps({}), json.dumps(constants.EXIT_COMMAND)]
        )

        with patch.object(zmq_server.database, "loadDefaultData"), patch.object(
            zmq_server, "sendResponse"
        ) as send_response, patch("builtins.print"):
            zmq_server.run("tcp", "127.0.0.1", "5555")

        self.assertEqual(
            [
                call(
                    socket,
                    {
                        constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
                        constants.KEY_FAILURE_MESSAGE: "no request type",
                    },
                ),
                call(
                    socket,
                    {
                        constants.KEY_STATUS: constants.SUCCESS_STATUS,
                        constants.KEY_SUCCESS_MESSAGE: constants.KEY_SERVER_EXIT,
                    },
                ),
            ],
            send_response.call_args_list,
        )

    def test_run_dispatches_login_requests(self):
        request = {
            constants.KEY_REQUEST_TYPE: constants.AUTHENTICATE_LOGIN_REQUEST_TYPE,
            constants.KEY_USERNAME: "johndoe",
            constants.KEY_PASSWORD: "password123",
        }
        response = {constants.KEY_STATUS: constants.SUCCESS_STATUS}
        zmq_server, socket = self._run_server_with_messages(
            [json.dumps(request), json.dumps(constants.EXIT_COMMAND)]
        )

        with patch.object(zmq_server.database, "loadDefaultData"), patch.object(
            zmq_server.login_authentication_request_handler,
            "handleRequest",
            return_value=response,
        ) as handle_request, patch.object(zmq_server, "sendResponse") as send_response, patch(
            "builtins.print"
        ):
            zmq_server.run("tcp", "127.0.0.1", "5555")

        handle_request.assert_called_once_with(request)
        self.assertEqual(
            [
                call(socket, response),
                call(
                    socket,
                    {
                        constants.KEY_STATUS: constants.SUCCESS_STATUS,
                        constants.KEY_SUCCESS_MESSAGE: constants.KEY_SERVER_EXIT,
                    },
                ),
            ],
            send_response.call_args_list,
        )

    def test_run_dispatches_create_account_requests(self):
        request = {
            constants.KEY_REQUEST_TYPE: constants.CREATE_ACCOUNT_REQUEST_TYPE,
            constants.KEY_USERNAME: "janedoe",
            constants.KEY_PASSWORD: "secret",
            constants.KEY_NAME: "Jane Doe",
        }
        response = {constants.KEY_STATUS: constants.SUCCESS_STATUS}
        zmq_server, socket = self._run_server_with_messages(
            [json.dumps(request), json.dumps(constants.EXIT_COMMAND)]
        )

        with patch.object(zmq_server.database, "loadDefaultData"), patch.object(
            zmq_server.create_account_request_handler,
            "handleRequest",
            return_value=response,
        ) as handle_request, patch.object(zmq_server, "sendResponse") as send_response, patch(
            "builtins.print"
        ):
            zmq_server.run("tcp", "127.0.0.1", "5555")

        handle_request.assert_called_once_with(request)
        self.assertEqual(
            [
                call(socket, response),
                call(
                    socket,
                    {
                        constants.KEY_STATUS: constants.SUCCESS_STATUS,
                        constants.KEY_SUCCESS_MESSAGE: constants.KEY_SERVER_EXIT,
                    },
                ),
            ],
            send_response.call_args_list,
        )

    def test_run_dispatches_search_requests(self):
        request = {
            constants.KEY_REQUEST_TYPE: constants.SEARCH_REQUEST_TYPE,
            constants.KEY_QUERY: "banana",
        }
        response = {constants.KEY_STATUS: constants.SUCCESS_STATUS, "food_items": []}
        zmq_server, socket = self._run_server_with_messages(
            [json.dumps(request), json.dumps(constants.EXIT_COMMAND)]
        )

        with patch.object(zmq_server.database, "loadDefaultData"), patch.object(
            zmq_server.search_request_handler,
            "handleRequest",
            return_value=response,
        ) as handle_request, patch.object(zmq_server, "sendResponse") as send_response, patch(
            "builtins.print"
        ):
            zmq_server.run("tcp", "127.0.0.1", "5555")

        handle_request.assert_called_once_with(request)
        self.assertEqual(
            [
                call(socket, response),
                call(
                    socket,
                    {
                        constants.KEY_STATUS: constants.SUCCESS_STATUS,
                        constants.KEY_SUCCESS_MESSAGE: constants.KEY_SERVER_EXIT,
                    },
                ),
            ],
            send_response.call_args_list,
        )

    def test_run_returns_unsupported_operation_for_unknown_request_types(self):
        request = {
            constants.KEY_REQUEST_TYPE: "DELETE_ACCOUNT",
        }
        zmq_server, socket = self._run_server_with_messages(
            [json.dumps(request), json.dumps(constants.EXIT_COMMAND)]
        )

        with patch.object(zmq_server.database, "loadDefaultData"), patch.object(
            zmq_server, "sendResponse"
        ) as send_response, patch("builtins.print"):
            zmq_server.run("tcp", "127.0.0.1", "5555")

        self.assertEqual(
            [
                call(
                    socket,
                    {
                        constants.KEY_STATUS: constants.UNSUPPORTED_OPERATION_STATUS,
                        constants.KEY_FAILURE_MESSAGE: "unsupported request type",
                    },
                ),
                call(
                    socket,
                    {
                        constants.KEY_STATUS: constants.SUCCESS_STATUS,
                        constants.KEY_SUCCESS_MESSAGE: constants.KEY_SERVER_EXIT,
                    },
                ),
            ],
            send_response.call_args_list,
        )

    def test_module_entry_point_runs_server_with_default_constants(self):
        fake_socket = _FakeSocket([json.dumps(constants.EXIT_COMMAND)])
        fake_zmq = types.SimpleNamespace(
            Context=lambda: _FakeContext(fake_socket),
            REP="REP",
        )

        with patch.dict(sys.modules, {"zmq": fake_zmq}), patch(
            "model.database.loadDefaultData"
        ) as load_default_data, patch("builtins.print"):
            runpy.run_module("server.zmq_server", run_name="__main__")

        load_default_data.assert_called_once_with()
        self.assertEqual(
            "{0}://{1}:{2}".format(
                constants.PROTOCOL,
                constants.IP_ADDRESS,
                constants.PORT,
            ),
            fake_socket.bound_address,
        )


if __name__ == "__main__":
    unittest.main()
