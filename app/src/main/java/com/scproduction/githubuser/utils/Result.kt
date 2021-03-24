package com.scproduction.githubuser.utils

class Result<out T>(val status: Status, val data: T) {
    companion object {
        fun <T> loading(data: T): Result<T> = Result(Status.LOADING, data)

        fun <T> success(data: T): Result<T> = Result(Status.SUCCESS, data)

        fun <T> error(data: T): Result<T> = Result(Status.ERROR, data)
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}