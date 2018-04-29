[![Build Status](https://travis-ci.org/valacuz/MyLocation.svg?branch=master)](https://travis-ci.org/valacuz/MyLocation)

# My Location

A practicing android app which implements Model-View-ViewModel (MVVM) pattern and written in kotlin. In addition I try to implements repository pattern in order to access the multiple data source with same rules and logic, example Android Room, Realm, REST API and memory for caching.

The data model layer exposes RxKotlin Flowable streams as a way of retrieving data and communicate with ViewModel layer.

For testing I create test case using JUnit for ViewModel and Instrumentation test for Model layer and UI Test.