package com.issac.novel.Error

open class Error(msg: String): Exception(msg)

class NetworkUnavailableError: Error("check your network")


