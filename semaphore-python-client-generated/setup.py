# coding: utf-8

"""
    SEMAPHORE

    Semaphore API  # noqa: E501

    OpenAPI spec version: 2.2.0
    
    Generated by: https://github.com/semaphore-api/semaphore-codegen.git
"""


from setuptools import setup, find_packages  # noqa: H301

NAME = "semaphore-client"
VERSION = "1.0.0"
# To install the library, run the following
#
# python setup.py install
#
# prerequisite: setuptools
# http://pypi.python.org/pypi/setuptools

REQUIRES = [
    "certifi>=2017.4.17",
    "python-dateutil>=2.1",
    "six>=1.10",
    "urllib3>=1.23"
]
    

setup(
    name=NAME,
    version=VERSION,
    description="SEMAPHORE",
    author_email="",
    url="",
    keywords=["Swagger", "SEMAPHORE"],
    install_requires=REQUIRES,
    packages=find_packages(),
    include_package_data=True,
    long_description="""\
    Semaphore API  # noqa: E501
    """
)