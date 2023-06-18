package com.apu.data

import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.serialization.Serializable

@Serializable
data class ExecutionResult(
    val error: String? = null,
    val message: String? = null
) {
    companion object {
        fun success(message: String) = ExecutionResult(message = message)
        fun error(error: String) = ExecutionResult(error = error)
    }
}
