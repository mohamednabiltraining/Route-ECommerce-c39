package com.route.data

import com.google.gson.Gson
import com.route.data.api.model.Response
import com.route.data.api.model.auth.AuthResponse
import com.route.domain.common.AuthError
import com.route.domain.common.InternetConnectionError
import com.route.domain.common.Resource
import com.route.domain.common.ServerError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

suspend fun <T> executeApi(apiCall: suspend () -> T): T {
    try {
        val response = apiCall.invoke()
        return response
    } catch (ex: HttpException) {
        if (ex.code() in 400..600) {
            val serverResponse = ex.response()?.errorBody()?.string()
            val response = Gson().fromJson<Response<Any>>(serverResponse, Response::class.java)
            throw ServerError(
                response.message,
                response.statusMsg,
                httpEx = ex,
            )
        }
        throw ex
    } catch (ex: IOException) {
        throw InternetConnectionError(ex)
    } catch (ex: TimeoutException) {
        throw InternetConnectionError(ex)
    } catch (ex: Exception) {
        throw ex
    }
}

suspend fun <T> toFlow(getData: suspend () -> T): Flow<Resource<T>> {
    return flow {
        emit(Resource.Loading)
        val response = getData.invoke()
        emit(Resource.Success(response))
    }.flowOn(Dispatchers.IO)
        .catch { ex ->
            when (ex) {
                is AuthError -> {
                    emit(Resource.AuthFail(ex))
                }

                is ServerError -> {
                    emit(Resource.ServerFail(ex))
                }

                is InternetConnectionError -> {
                    emit(Resource.Fail(ex))
                }

                else -> emit(Resource.Fail(ex))
            }
        }
}

suspend fun <T> executeAuth(apiCall: suspend () -> T): T {
    try {
        return apiCall.invoke()
    } catch (ex: HttpException) {
        if (ex.code() == 400) {
            val serverResponse = ex.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(serverResponse, AuthResponse::class.java)
            throw AuthError(
                errorResponse.error?.toError(),
                ex,
            )
        } else if (ex.code() in 401..600) {
            val serverResponse = ex.response()?.errorBody()?.string()
            val response = Gson().fromJson<Response<Any>>(serverResponse, Response::class.java)
            throw ServerError(
                response.message,
                response.statusMsg,
                httpEx = ex,
            )
        }
        throw ex
    } catch (ex: IOException) {
        throw InternetConnectionError(ex)
    } catch (ex: TimeoutException) {
        throw InternetConnectionError(ex)
    } catch (ex: Exception) {
        throw ex
    }
}
