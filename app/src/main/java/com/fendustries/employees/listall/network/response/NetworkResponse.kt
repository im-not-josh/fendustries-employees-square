package com.fendustries.employees.listall.network.response

/**
 * The purpose of this class is to return a common single type object from Remote Repository layer.
 * That way, consumers can easily digest errors alongside successful requests from the Remote Repository.
 * This approach avoids having to do something like throw and catch exceptions to consuming layers in
 * order to tell the consumer, what kind of error happened while making an API call.
 *
 * We could expand these classes to also return response headers as well.
 */
sealed class NetworkResponse<out Body> {
    data class Success<Body>(val body: Body) : NetworkResponse<Body>()

    data class HttpFailure(val statusCode: Int, val error: String?) : NetworkResponse<Nothing>()

    data class Failure(val throwable: Throwable, val statusCode: Int? = null) : NetworkResponse<Nothing>()
}