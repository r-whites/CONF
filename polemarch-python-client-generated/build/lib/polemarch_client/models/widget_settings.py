# coding: utf-8

"""
    Polemarch

     ### Polemarch is ansible based service for orchestration infrastructure.  * [Documentation](http://polemarch.readthedocs.io/) * [Issue Tracker](https://gitlab.com/vstconsulting/polemarch/issues) * [Source Code](https://gitlab.com/vstconsulting/polemarch)    # noqa: E501

    OpenAPI spec version: v2
    
    Generated by: https://github.com/swagger-api/swagger-codegen.git
"""


import pprint
import re  # noqa: F401

import six


class WidgetSettings(object):
    """NOTE: This class is auto generated by the swagger code generator program.

    Do not edit the class manually.
    """

    """
    Attributes:
      swagger_types (dict): The key is attribute name
                            and the value is attribute type.
      attribute_map (dict): The key is attribute name
                            and the value is json key in definition.
    """
    swagger_types = {
        'pmw_users_counter': 'CounterWidgetSetting',
        'pmw_projects_counter': 'CounterWidgetSetting',
        'pmw_templates_counter': 'CounterWidgetSetting',
        'pmw_inventories_counter': 'CounterWidgetSetting',
        'pmw_groups_counter': 'CounterWidgetSetting',
        'pmw_hosts_counter': 'CounterWidgetSetting',
        'pmw_chart_widget': 'WidgetSetting'
    }

    attribute_map = {
        'pmw_users_counter': 'pmwUsersCounter',
        'pmw_projects_counter': 'pmwProjectsCounter',
        'pmw_templates_counter': 'pmwTemplatesCounter',
        'pmw_inventories_counter': 'pmwInventoriesCounter',
        'pmw_groups_counter': 'pmwGroupsCounter',
        'pmw_hosts_counter': 'pmwHostsCounter',
        'pmw_chart_widget': 'pmwChartWidget'
    }

    def __init__(self, pmw_users_counter=None, pmw_projects_counter=None, pmw_templates_counter=None, pmw_inventories_counter=None, pmw_groups_counter=None, pmw_hosts_counter=None, pmw_chart_widget=None):  # noqa: E501
        """WidgetSettings - a model defined in Swagger"""  # noqa: E501

        self._pmw_users_counter = None
        self._pmw_projects_counter = None
        self._pmw_templates_counter = None
        self._pmw_inventories_counter = None
        self._pmw_groups_counter = None
        self._pmw_hosts_counter = None
        self._pmw_chart_widget = None
        self.discriminator = None

        self.pmw_users_counter = pmw_users_counter
        self.pmw_projects_counter = pmw_projects_counter
        self.pmw_templates_counter = pmw_templates_counter
        self.pmw_inventories_counter = pmw_inventories_counter
        self.pmw_groups_counter = pmw_groups_counter
        self.pmw_hosts_counter = pmw_hosts_counter
        self.pmw_chart_widget = pmw_chart_widget

    @property
    def pmw_users_counter(self):
        """Gets the pmw_users_counter of this WidgetSettings.  # noqa: E501


        :return: The pmw_users_counter of this WidgetSettings.  # noqa: E501
        :rtype: CounterWidgetSetting
        """
        return self._pmw_users_counter

    @pmw_users_counter.setter
    def pmw_users_counter(self, pmw_users_counter):
        """Sets the pmw_users_counter of this WidgetSettings.


        :param pmw_users_counter: The pmw_users_counter of this WidgetSettings.  # noqa: E501
        :type: CounterWidgetSetting
        """
        if pmw_users_counter is None:
            raise ValueError("Invalid value for `pmw_users_counter`, must not be `None`")  # noqa: E501

        self._pmw_users_counter = pmw_users_counter

    @property
    def pmw_projects_counter(self):
        """Gets the pmw_projects_counter of this WidgetSettings.  # noqa: E501


        :return: The pmw_projects_counter of this WidgetSettings.  # noqa: E501
        :rtype: CounterWidgetSetting
        """
        return self._pmw_projects_counter

    @pmw_projects_counter.setter
    def pmw_projects_counter(self, pmw_projects_counter):
        """Sets the pmw_projects_counter of this WidgetSettings.


        :param pmw_projects_counter: The pmw_projects_counter of this WidgetSettings.  # noqa: E501
        :type: CounterWidgetSetting
        """
        if pmw_projects_counter is None:
            raise ValueError("Invalid value for `pmw_projects_counter`, must not be `None`")  # noqa: E501

        self._pmw_projects_counter = pmw_projects_counter

    @property
    def pmw_templates_counter(self):
        """Gets the pmw_templates_counter of this WidgetSettings.  # noqa: E501


        :return: The pmw_templates_counter of this WidgetSettings.  # noqa: E501
        :rtype: CounterWidgetSetting
        """
        return self._pmw_templates_counter

    @pmw_templates_counter.setter
    def pmw_templates_counter(self, pmw_templates_counter):
        """Sets the pmw_templates_counter of this WidgetSettings.


        :param pmw_templates_counter: The pmw_templates_counter of this WidgetSettings.  # noqa: E501
        :type: CounterWidgetSetting
        """
        if pmw_templates_counter is None:
            raise ValueError("Invalid value for `pmw_templates_counter`, must not be `None`")  # noqa: E501

        self._pmw_templates_counter = pmw_templates_counter

    @property
    def pmw_inventories_counter(self):
        """Gets the pmw_inventories_counter of this WidgetSettings.  # noqa: E501


        :return: The pmw_inventories_counter of this WidgetSettings.  # noqa: E501
        :rtype: CounterWidgetSetting
        """
        return self._pmw_inventories_counter

    @pmw_inventories_counter.setter
    def pmw_inventories_counter(self, pmw_inventories_counter):
        """Sets the pmw_inventories_counter of this WidgetSettings.


        :param pmw_inventories_counter: The pmw_inventories_counter of this WidgetSettings.  # noqa: E501
        :type: CounterWidgetSetting
        """
        if pmw_inventories_counter is None:
            raise ValueError("Invalid value for `pmw_inventories_counter`, must not be `None`")  # noqa: E501

        self._pmw_inventories_counter = pmw_inventories_counter

    @property
    def pmw_groups_counter(self):
        """Gets the pmw_groups_counter of this WidgetSettings.  # noqa: E501


        :return: The pmw_groups_counter of this WidgetSettings.  # noqa: E501
        :rtype: CounterWidgetSetting
        """
        return self._pmw_groups_counter

    @pmw_groups_counter.setter
    def pmw_groups_counter(self, pmw_groups_counter):
        """Sets the pmw_groups_counter of this WidgetSettings.


        :param pmw_groups_counter: The pmw_groups_counter of this WidgetSettings.  # noqa: E501
        :type: CounterWidgetSetting
        """
        if pmw_groups_counter is None:
            raise ValueError("Invalid value for `pmw_groups_counter`, must not be `None`")  # noqa: E501

        self._pmw_groups_counter = pmw_groups_counter

    @property
    def pmw_hosts_counter(self):
        """Gets the pmw_hosts_counter of this WidgetSettings.  # noqa: E501


        :return: The pmw_hosts_counter of this WidgetSettings.  # noqa: E501
        :rtype: CounterWidgetSetting
        """
        return self._pmw_hosts_counter

    @pmw_hosts_counter.setter
    def pmw_hosts_counter(self, pmw_hosts_counter):
        """Sets the pmw_hosts_counter of this WidgetSettings.


        :param pmw_hosts_counter: The pmw_hosts_counter of this WidgetSettings.  # noqa: E501
        :type: CounterWidgetSetting
        """
        if pmw_hosts_counter is None:
            raise ValueError("Invalid value for `pmw_hosts_counter`, must not be `None`")  # noqa: E501

        self._pmw_hosts_counter = pmw_hosts_counter

    @property
    def pmw_chart_widget(self):
        """Gets the pmw_chart_widget of this WidgetSettings.  # noqa: E501


        :return: The pmw_chart_widget of this WidgetSettings.  # noqa: E501
        :rtype: WidgetSetting
        """
        return self._pmw_chart_widget

    @pmw_chart_widget.setter
    def pmw_chart_widget(self, pmw_chart_widget):
        """Sets the pmw_chart_widget of this WidgetSettings.


        :param pmw_chart_widget: The pmw_chart_widget of this WidgetSettings.  # noqa: E501
        :type: WidgetSetting
        """
        if pmw_chart_widget is None:
            raise ValueError("Invalid value for `pmw_chart_widget`, must not be `None`")  # noqa: E501

        self._pmw_chart_widget = pmw_chart_widget

    def to_dict(self):
        """Returns the model properties as a dict"""
        result = {}

        for attr, _ in six.iteritems(self.swagger_types):
            value = getattr(self, attr)
            if isinstance(value, list):
                result[attr] = list(map(
                    lambda x: x.to_dict() if hasattr(x, "to_dict") else x,
                    value
                ))
            elif hasattr(value, "to_dict"):
                result[attr] = value.to_dict()
            elif isinstance(value, dict):
                result[attr] = dict(map(
                    lambda item: (item[0], item[1].to_dict())
                    if hasattr(item[1], "to_dict") else item,
                    value.items()
                ))
            else:
                result[attr] = value
        if issubclass(WidgetSettings, dict):
            for key, value in self.items():
                result[key] = value

        return result

    def to_str(self):
        """Returns the string representation of the model"""
        return pprint.pformat(self.to_dict())

    def __repr__(self):
        """For `print` and `pprint`"""
        return self.to_str()

    def __eq__(self, other):
        """Returns true if both objects are equal"""
        if not isinstance(other, WidgetSettings):
            return False

        return self.__dict__ == other.__dict__

    def __ne__(self, other):
        """Returns true if both objects are not equal"""
        return not self == other