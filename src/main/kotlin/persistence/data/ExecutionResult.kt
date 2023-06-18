package com.apu.data

import com.fasterxml.jackson.annotation.JsonInclude
import kotlinx.serialization.Serializable

@Serializable
data class ExecutionResult(
    val error: String? = null,
    val message: String? = null
)