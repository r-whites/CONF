# coding: utf-8

"""
    Polemarch

     ### Polemarch is ansible based service for orchestration infrastructure.  * [Documentation](http://polemarch.readthedocs.io/) * [Issue Tracker](https://gitlab.com/vstconsulting/polemarch/issues) * [Source Code](https://gitlab.com/vstconsulting/polemarch)    # noqa: E501

    OpenAPI spec version: v2
    
    Generated by: https://github.com/swagger-api/swagger-codegen.git
"""


from __future__ import absolute_import

import unittest

import polemarch_client
from polemarch_client.models.inventory import Inventory  # noqa: E501
from polemarch_client.rest import ApiException


class TestInventory(unittest.TestCase):
    """Inventory unit test stubs"""

    def setUp(self):
        pass

    def tearDown(self):
        pass

    def testInventory(self):
        """Test Inventory"""
        # FIXME: construct object with mandatory attributes with example values
        # model = polemarch_client.models.inventory.Inventory()  # noqa: E501
        pass


if __name__ == '__main__':
    unittest.main()