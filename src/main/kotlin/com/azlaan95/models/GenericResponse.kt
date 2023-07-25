package com.azlaan95.models

import kotlinx.serialization.Serializable

@Serializable
data class AppResponse<T>(val message: String?=null, val appCode: Int=200, val data: T?=null, val error: AppError?=null)

@Serializable
data class AppError(val errorMessage: String?, val errorLog: String?=null)
