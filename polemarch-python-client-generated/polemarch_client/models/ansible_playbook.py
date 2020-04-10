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


class AnsiblePlaybook(object):
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
        'playbook': 'str',
        'args': 'str',
        'become': 'bool',
        'become_method': 'str',
        'become_user': 'str',
        'check': 'bool',
        'connection': 'str',
        'diff': 'bool',
        'extra_vars': 'str',
        'flush_cache': 'bool',
        'force_handlers': 'bool',
        'forks': 'int',
        'inventory': 'str',
        'limit': 'str',
        'list_hosts': 'bool',
        'list_tags': 'bool',
        'list_tasks': 'bool',
        'module_path': 'str',
        'private_key': 'str',
        'scp_extra_args': 'str',
        'sftp_extra_args': 'str',
        'skip_tags': 'str',
        'ssh_common_args': 'str',
        'ssh_extra_args': 'str',
        'start_at_task': 'str',
        'step': 'bool',
        'syntax_check': 'bool',
        'tags': 'str',
        'timeout': 'int',
        'user': 'str',
        'vault_id': 'str',
        'vault_password_file': 'str',
        'verbose': 'int'
    }

    attribute_map = {
        'playbook': 'playbook',
        'args': 'args',
        'become': 'become',
        'become_method': 'become_method',
        'become_user': 'become_user',
        'check': 'check',
        'connection': 'connection',
        'diff': 'diff',
        'extra_vars': 'extra_vars',
        'flush_cache': 'flush_cache',
        'force_handlers': 'force_handlers',
        'forks': 'forks',
        'inventory': 'inventory',
        'limit': 'limit',
        'list_hosts': 'list_hosts',
        'list_tags': 'list_tags',
        'list_tasks': 'list_tasks',
        'module_path': 'module_path',
        'private_key': 'private_key',
        'scp_extra_args': 'scp_extra_args',
        'sftp_extra_args': 'sftp_extra_args',
        'skip_tags': 'skip_tags',
        'ssh_common_args': 'ssh_common_args',
        'ssh_extra_args': 'ssh_extra_args',
        'start_at_task': 'start_at_task',
        'step': 'step',
        'syntax_check': 'syntax_check',
        'tags': 'tags',
        'timeout': 'timeout',
        'user': 'user',
        'vault_id': 'vault_id',
        'vault_password_file': 'vault_password_file',
        'verbose': 'verbose'
    }

    def __init__(self, playbook=None, args=None, become=False, become_method=None, become_user=None, check=False, connection=None, diff=False, extra_vars=None, flush_cache=False, force_handlers=False, forks=None, inventory=None, limit=None, list_hosts=False, list_tags=False, list_tasks=False, module_path=None, private_key=None, scp_extra_args=None, sftp_extra_args=None, skip_tags=None, ssh_common_args=None, ssh_extra_args=None, start_at_task=None, step=False, syntax_check=False, tags=None, timeout=None, user=None, vault_id=None, vault_password_file=None, verbose=None):  # noqa: E501
        """AnsiblePlaybook - a model defined in Swagger"""  # noqa: E501

        self._playbook = None
        self._args = None
        self._become = None
        self._become_method = None
        self._become_user = None
        self._check = None
        self._connection = None
        self._diff = None
        self._extra_vars = None
        self._flush_cache = None
        self._force_handlers = None
        self._forks = None
        self._inventory = None
        self._limit = None
        self._list_hosts = None
        self._list_tags = None
        self._list_tasks = None
        self._module_path = None
        self._private_key = None
        self._scp_extra_args = None
        self._sftp_extra_args = None
        self._skip_tags = None
        self._ssh_common_args = None
        self._ssh_extra_args = None
        self._start_at_task = None
        self._step = None
        self._syntax_check = None
        self._tags = None
        self._timeout = None
        self._user = None
        self._vault_id = None
        self._vault_password_file = None
        self._verbose = None
        self.discriminator = None

        self.playbook = playbook
        if args is not None:
            self.args = args
        if become is not None:
            self.become = become
        if become_method is not None:
            self.become_method = become_method
        if become_user is not None:
            self.become_user = become_user
        if check is not None:
            self.check = check
        if connection is not None:
            self.connection = connection
        if diff is not None:
            self.diff = diff
        if extra_vars is not None:
            self.extra_vars = extra_vars
        if flush_cache is not None:
            self.flush_cache = flush_cache
        if force_handlers is not None:
            self.force_handlers = force_handlers
        if forks is not None:
            self.forks = forks
        if inventory is not None:
            self.inventory = inventory
        if limit is not None:
            self.limit = limit
        if list_hosts is not None:
            self.list_hosts = list_hosts
        if list_tags is not None:
            self.list_tags = list_tags
        if list_tasks is not None:
            self.list_tasks = list_tasks
        if module_path is not None:
            self.module_path = module_path
        if private_key is not None:
            self.private_key = private_key
        if scp_extra_args is not None:
            self.scp_extra_args = scp_extra_args
        if sftp_extra_args is not None:
            self.sftp_extra_args = sftp_extra_args
        if skip_tags is not None:
            self.skip_tags = skip_tags
        if ssh_common_args is not None:
            self.ssh_common_args = ssh_common_args
        if ssh_extra_args is not None:
            self.ssh_extra_args = ssh_extra_args
        if start_at_task is not None:
            self.start_at_task = start_at_task
        if step is not None:
            self.step = step
        if syntax_check is not None:
            self.syntax_check = syntax_check
        if tags is not None:
            self.tags = tags
        if timeout is not None:
            self.timeout = timeout
        if user is not None:
            self.user = user
        if vault_id is not None:
            self.vault_id = vault_id
        if vault_password_file is not None:
            self.vault_password_file = vault_password_file
        if verbose is not None:
            self.verbose = verbose

    @property
    def playbook(self):
        """Gets the playbook of this AnsiblePlaybook.  # noqa: E501


        :return: The playbook of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._playbook

    @playbook.setter
    def playbook(self, playbook):
        """Sets the playbook of this AnsiblePlaybook.


        :param playbook: The playbook of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """
        if playbook is None:
            raise ValueError("Invalid value for `playbook`, must not be `None`")  # noqa: E501

        self._playbook = playbook

    @property
    def args(self):
        """Gets the args of this AnsiblePlaybook.  # noqa: E501

        Playbook(s)  # noqa: E501

        :return: The args of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._args

    @args.setter
    def args(self, args):
        """Sets the args of this AnsiblePlaybook.

        Playbook(s)  # noqa: E501

        :param args: The args of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._args = args

    @property
    def become(self):
        """Gets the become of this AnsiblePlaybook.  # noqa: E501

        run operations with become (does not imply password prompting)  # noqa: E501

        :return: The become of this AnsiblePlaybook.  # noqa: E501
        :rtype: bool
        """
        return self._become

    @become.setter
    def become(self, become):
        """Sets the become of this AnsiblePlaybook.

        run operations with become (does not imply password prompting)  # noqa: E501

        :param become: The become of this AnsiblePlaybook.  # noqa: E501
        :type: bool
        """

        self._become = become

    @property
    def become_method(self):
        """Gets the become_method of this AnsiblePlaybook.  # noqa: E501

        privilege escalation method to use (default=sudo), use `ansible-doc -t become -l` to list valid choices.  # noqa: E501

        :return: The become_method of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._become_method

    @become_method.setter
    def become_method(self, become_method):
        """Sets the become_method of this AnsiblePlaybook.

        privilege escalation method to use (default=sudo), use `ansible-doc -t become -l` to list valid choices.  # noqa: E501

        :param become_method: The become_method of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._become_method = become_method

    @property
    def become_user(self):
        """Gets the become_user of this AnsiblePlaybook.  # noqa: E501

        run operations as this user (default=root)  # noqa: E501

        :return: The become_user of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._become_user

    @become_user.setter
    def become_user(self, become_user):
        """Sets the become_user of this AnsiblePlaybook.

        run operations as this user (default=root)  # noqa: E501

        :param become_user: The become_user of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._become_user = become_user

    @property
    def check(self):
        """Gets the check of this AnsiblePlaybook.  # noqa: E501

        don't make any changes; instead, try to predict some of the changes that may occur  # noqa: E501

        :return: The check of this AnsiblePlaybook.  # noqa: E501
        :rtype: bool
        """
        return self._check

    @check.setter
    def check(self, check):
        """Sets the check of this AnsiblePlaybook.

        don't make any changes; instead, try to predict some of the changes that may occur  # noqa: E501

        :param check: The check of this AnsiblePlaybook.  # noqa: E501
        :type: bool
        """

        self._check = check

    @property
    def connection(self):
        """Gets the connection of this AnsiblePlaybook.  # noqa: E501

        connection type to use (default=smart)  # noqa: E501

        :return: The connection of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._connection

    @connection.setter
    def connection(self, connection):
        """Sets the connection of this AnsiblePlaybook.

        connection type to use (default=smart)  # noqa: E501

        :param connection: The connection of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._connection = connection

    @property
    def diff(self):
        """Gets the diff of this AnsiblePlaybook.  # noqa: E501

        when changing (small) files and templates, show the differences in those files; works great with --check  # noqa: E501

        :return: The diff of this AnsiblePlaybook.  # noqa: E501
        :rtype: bool
        """
        return self._diff

    @diff.setter
    def diff(self, diff):
        """Sets the diff of this AnsiblePlaybook.

        when changing (small) files and templates, show the differences in those files; works great with --check  # noqa: E501

        :param diff: The diff of this AnsiblePlaybook.  # noqa: E501
        :type: bool
        """

        self._diff = diff

    @property
    def extra_vars(self):
        """Gets the extra_vars of this AnsiblePlaybook.  # noqa: E501

        set additional variables as key=value or YAML/JSON, if filename prepend with @  # noqa: E501

        :return: The extra_vars of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._extra_vars

    @extra_vars.setter
    def extra_vars(self, extra_vars):
        """Sets the extra_vars of this AnsiblePlaybook.

        set additional variables as key=value or YAML/JSON, if filename prepend with @  # noqa: E501

        :param extra_vars: The extra_vars of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._extra_vars = extra_vars

    @property
    def flush_cache(self):
        """Gets the flush_cache of this AnsiblePlaybook.  # noqa: E501

        clear the fact cache for every host in inventory  # noqa: E501

        :return: The flush_cache of this AnsiblePlaybook.  # noqa: E501
        :rtype: bool
        """
        return self._flush_cache

    @flush_cache.setter
    def flush_cache(self, flush_cache):
        """Sets the flush_cache of this AnsiblePlaybook.

        clear the fact cache for every host in inventory  # noqa: E501

        :param flush_cache: The flush_cache of this AnsiblePlaybook.  # noqa: E501
        :type: bool
        """

        self._flush_cache = flush_cache

    @property
    def force_handlers(self):
        """Gets the force_handlers of this AnsiblePlaybook.  # noqa: E501

        run handlers even if a task fails  # noqa: E501

        :return: The force_handlers of this AnsiblePlaybook.  # noqa: E501
        :rtype: bool
        """
        return self._force_handlers

    @force_handlers.setter
    def force_handlers(self, force_handlers):
        """Sets the force_handlers of this AnsiblePlaybook.

        run handlers even if a task fails  # noqa: E501

        :param force_handlers: The force_handlers of this AnsiblePlaybook.  # noqa: E501
        :type: bool
        """

        self._force_handlers = force_handlers

    @property
    def forks(self):
        """Gets the forks of this AnsiblePlaybook.  # noqa: E501

        specify number of parallel processes to use (default=5)  # noqa: E501

        :return: The forks of this AnsiblePlaybook.  # noqa: E501
        :rtype: int
        """
        return self._forks

    @forks.setter
    def forks(self, forks):
        """Sets the forks of this AnsiblePlaybook.

        specify number of parallel processes to use (default=5)  # noqa: E501

        :param forks: The forks of this AnsiblePlaybook.  # noqa: E501
        :type: int
        """

        self._forks = forks

    @property
    def inventory(self):
        """Gets the inventory of this AnsiblePlaybook.  # noqa: E501

        specify inventory host path or comma separated host list. --inventory-file is deprecated  # noqa: E501

        :return: The inventory of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._inventory

    @inventory.setter
    def inventory(self, inventory):
        """Sets the inventory of this AnsiblePlaybook.

        specify inventory host path or comma separated host list. --inventory-file is deprecated  # noqa: E501

        :param inventory: The inventory of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._inventory = inventory

    @property
    def limit(self):
        """Gets the limit of this AnsiblePlaybook.  # noqa: E501

        further limit selected hosts to an additional pattern  # noqa: E501

        :return: The limit of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._limit

    @limit.setter
    def limit(self, limit):
        """Sets the limit of this AnsiblePlaybook.

        further limit selected hosts to an additional pattern  # noqa: E501

        :param limit: The limit of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._limit = limit

    @property
    def list_hosts(self):
        """Gets the list_hosts of this AnsiblePlaybook.  # noqa: E501

        outputs a list of matching hosts; does not execute anything else  # noqa: E501

        :return: The list_hosts of this AnsiblePlaybook.  # noqa: E501
        :rtype: bool
        """
        return self._list_hosts

    @list_hosts.setter
    def list_hosts(self, list_hosts):
        """Sets the list_hosts of this AnsiblePlaybook.

        outputs a list of matching hosts; does not execute anything else  # noqa: E501

        :param list_hosts: The list_hosts of this AnsiblePlaybook.  # noqa: E501
        :type: bool
        """

        self._list_hosts = list_hosts

    @property
    def list_tags(self):
        """Gets the list_tags of this AnsiblePlaybook.  # noqa: E501

        list all available tags  # noqa: E501

        :return: The list_tags of this AnsiblePlaybook.  # noqa: E501
        :rtype: bool
        """
        return self._list_tags

    @list_tags.setter
    def list_tags(self, list_tags):
        """Sets the list_tags of this AnsiblePlaybook.

        list all available tags  # noqa: E501

        :param list_tags: The list_tags of this AnsiblePlaybook.  # noqa: E501
        :type: bool
        """

        self._list_tags = list_tags

    @property
    def list_tasks(self):
        """Gets the list_tasks of this AnsiblePlaybook.  # noqa: E501

        list all tasks that would be executed  # noqa: E501

        :return: The list_tasks of this AnsiblePlaybook.  # noqa: E501
        :rtype: bool
        """
        return self._list_tasks

    @list_tasks.setter
    def list_tasks(self, list_tasks):
        """Sets the list_tasks of this AnsiblePlaybook.

        list all tasks that would be executed  # noqa: E501

        :param list_tasks: The list_tasks of this AnsiblePlaybook.  # noqa: E501
        :type: bool
        """

        self._list_tasks = list_tasks

    @property
    def module_path(self):
        """Gets the module_path of this AnsiblePlaybook.  # noqa: E501

        prepend colon-separated path(s) to module library (default=~/.ansible/plugins/modules:/usr/share/ansible/plugins/modules)  # noqa: E501

        :return: The module_path of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._module_path

    @module_path.setter
    def module_path(self, module_path):
        """Sets the module_path of this AnsiblePlaybook.

        prepend colon-separated path(s) to module library (default=~/.ansible/plugins/modules:/usr/share/ansible/plugins/modules)  # noqa: E501

        :param module_path: The module_path of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._module_path = module_path

    @property
    def private_key(self):
        """Gets the private_key of this AnsiblePlaybook.  # noqa: E501

        use this file to authenticate the connection  # noqa: E501

        :return: The private_key of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._private_key

    @private_key.setter
    def private_key(self, private_key):
        """Sets the private_key of this AnsiblePlaybook.

        use this file to authenticate the connection  # noqa: E501

        :param private_key: The private_key of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._private_key = private_key

    @property
    def scp_extra_args(self):
        """Gets the scp_extra_args of this AnsiblePlaybook.  # noqa: E501

        specify extra arguments to pass to scp only (e.g. -l)  # noqa: E501

        :return: The scp_extra_args of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._scp_extra_args

    @scp_extra_args.setter
    def scp_extra_args(self, scp_extra_args):
        """Sets the scp_extra_args of this AnsiblePlaybook.

        specify extra arguments to pass to scp only (e.g. -l)  # noqa: E501

        :param scp_extra_args: The scp_extra_args of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._scp_extra_args = scp_extra_args

    @property
    def sftp_extra_args(self):
        """Gets the sftp_extra_args of this AnsiblePlaybook.  # noqa: E501

        specify extra arguments to pass to sftp only (e.g. -f, -l)  # noqa: E501

        :return: The sftp_extra_args of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._sftp_extra_args

    @sftp_extra_args.setter
    def sftp_extra_args(self, sftp_extra_args):
        """Sets the sftp_extra_args of this AnsiblePlaybook.

        specify extra arguments to pass to sftp only (e.g. -f, -l)  # noqa: E501

        :param sftp_extra_args: The sftp_extra_args of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._sftp_extra_args = sftp_extra_args

    @property
    def skip_tags(self):
        """Gets the skip_tags of this AnsiblePlaybook.  # noqa: E501

        only run plays and tasks whose tags do not match these values  # noqa: E501

        :return: The skip_tags of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._skip_tags

    @skip_tags.setter
    def skip_tags(self, skip_tags):
        """Sets the skip_tags of this AnsiblePlaybook.

        only run plays and tasks whose tags do not match these values  # noqa: E501

        :param skip_tags: The skip_tags of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._skip_tags = skip_tags

    @property
    def ssh_common_args(self):
        """Gets the ssh_common_args of this AnsiblePlaybook.  # noqa: E501

        specify common arguments to pass to sftp/scp/ssh (e.g. ProxyCommand)  # noqa: E501

        :return: The ssh_common_args of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._ssh_common_args

    @ssh_common_args.setter
    def ssh_common_args(self, ssh_common_args):
        """Sets the ssh_common_args of this AnsiblePlaybook.

        specify common arguments to pass to sftp/scp/ssh (e.g. ProxyCommand)  # noqa: E501

        :param ssh_common_args: The ssh_common_args of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._ssh_common_args = ssh_common_args

    @property
    def ssh_extra_args(self):
        """Gets the ssh_extra_args of this AnsiblePlaybook.  # noqa: E501

        specify extra arguments to pass to ssh only (e.g. -R)  # noqa: E501

        :return: The ssh_extra_args of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._ssh_extra_args

    @ssh_extra_args.setter
    def ssh_extra_args(self, ssh_extra_args):
        """Sets the ssh_extra_args of this AnsiblePlaybook.

        specify extra arguments to pass to ssh only (e.g. -R)  # noqa: E501

        :param ssh_extra_args: The ssh_extra_args of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._ssh_extra_args = ssh_extra_args

    @property
    def start_at_task(self):
        """Gets the start_at_task of this AnsiblePlaybook.  # noqa: E501

        start the playbook at the task matching this name  # noqa: E501

        :return: The start_at_task of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._start_at_task

    @start_at_task.setter
    def start_at_task(self, start_at_task):
        """Sets the start_at_task of this AnsiblePlaybook.

        start the playbook at the task matching this name  # noqa: E501

        :param start_at_task: The start_at_task of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._start_at_task = start_at_task

    @property
    def step(self):
        """Gets the step of this AnsiblePlaybook.  # noqa: E501

        one-step-at-a-time: confirm each task before running  # noqa: E501

        :return: The step of this AnsiblePlaybook.  # noqa: E501
        :rtype: bool
        """
        return self._step

    @step.setter
    def step(self, step):
        """Sets the step of this AnsiblePlaybook.

        one-step-at-a-time: confirm each task before running  # noqa: E501

        :param step: The step of this AnsiblePlaybook.  # noqa: E501
        :type: bool
        """

        self._step = step

    @property
    def syntax_check(self):
        """Gets the syntax_check of this AnsiblePlaybook.  # noqa: E501

        perform a syntax check on the playbook, but do not execute it  # noqa: E501

        :return: The syntax_check of this AnsiblePlaybook.  # noqa: E501
        :rtype: bool
        """
        return self._syntax_check

    @syntax_check.setter
    def syntax_check(self, syntax_check):
        """Sets the syntax_check of this AnsiblePlaybook.

        perform a syntax check on the playbook, but do not execute it  # noqa: E501

        :param syntax_check: The syntax_check of this AnsiblePlaybook.  # noqa: E501
        :type: bool
        """

        self._syntax_check = syntax_check

    @property
    def tags(self):
        """Gets the tags of this AnsiblePlaybook.  # noqa: E501

        only run plays and tasks tagged with these values  # noqa: E501

        :return: The tags of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._tags

    @tags.setter
    def tags(self, tags):
        """Sets the tags of this AnsiblePlaybook.

        only run plays and tasks tagged with these values  # noqa: E501

        :param tags: The tags of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._tags = tags

    @property
    def timeout(self):
        """Gets the timeout of this AnsiblePlaybook.  # noqa: E501

        override the connection timeout in seconds (default=10)  # noqa: E501

        :return: The timeout of this AnsiblePlaybook.  # noqa: E501
        :rtype: int
        """
        return self._timeout

    @timeout.setter
    def timeout(self, timeout):
        """Sets the timeout of this AnsiblePlaybook.

        override the connection timeout in seconds (default=10)  # noqa: E501

        :param timeout: The timeout of this AnsiblePlaybook.  # noqa: E501
        :type: int
        """

        self._timeout = timeout

    @property
    def user(self):
        """Gets the user of this AnsiblePlaybook.  # noqa: E501

        connect as this user (default=None)  # noqa: E501

        :return: The user of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._user

    @user.setter
    def user(self, user):
        """Sets the user of this AnsiblePlaybook.

        connect as this user (default=None)  # noqa: E501

        :param user: The user of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._user = user

    @property
    def vault_id(self):
        """Gets the vault_id of this AnsiblePlaybook.  # noqa: E501

        the vault identity to use  # noqa: E501

        :return: The vault_id of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._vault_id

    @vault_id.setter
    def vault_id(self, vault_id):
        """Sets the vault_id of this AnsiblePlaybook.

        the vault identity to use  # noqa: E501

        :param vault_id: The vault_id of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._vault_id = vault_id

    @property
    def vault_password_file(self):
        """Gets the vault_password_file of this AnsiblePlaybook.  # noqa: E501

        vault password file  # noqa: E501

        :return: The vault_password_file of this AnsiblePlaybook.  # noqa: E501
        :rtype: str
        """
        return self._vault_password_file

    @vault_password_file.setter
    def vault_password_file(self, vault_password_file):
        """Sets the vault_password_file of this AnsiblePlaybook.

        vault password file  # noqa: E501

        :param vault_password_file: The vault_password_file of this AnsiblePlaybook.  # noqa: E501
        :type: str
        """

        self._vault_password_file = vault_password_file

    @property
    def verbose(self):
        """Gets the verbose of this AnsiblePlaybook.  # noqa: E501

        verbose mode (-vvv for more, -vvvv to enable connection debugging)  # noqa: E501

        :return: The verbose of this AnsiblePlaybook.  # noqa: E501
        :rtype: int
        """
        return self._verbose

    @verbose.setter
    def verbose(self, verbose):
        """Sets the verbose of this AnsiblePlaybook.

        verbose mode (-vvv for more, -vvvv to enable connection debugging)  # noqa: E501

        :param verbose: The verbose of this AnsiblePlaybook.  # noqa: E501
        :type: int
        """
        if verbose is not None and verbose > 4:  # noqa: E501
            raise ValueError("Invalid value for `verbose`, must be a value less than or equal to `4`")  # noqa: E501

        self._verbose = verbose

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
        if issubclass(AnsiblePlaybook, dict):
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
        if not isinstance(other, AnsiblePlaybook):
            return False

        return self.__dict__ == other.__dict__

    def __ne__(self, other):
        """Returns true if both objects are not equal"""
        return not self == other