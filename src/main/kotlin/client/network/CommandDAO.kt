package app.common.client.network

import app.common.client.Command
import com.apu.data.ExecutionResult

interface CommandDAO {
    suspend fun keepLogin()
    suspend fun sendCommand(command: Command, onResult: (ExecutionResult?) -> Unit)
}