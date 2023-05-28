package persistence.data

import com.fasterxml.jackson.annotation.JsonInclude

data class ExecutionResult(
    @JsonInclude val error: String? = null,
    @JsonInclude val message: String? = null
)
