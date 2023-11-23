package com.example.tactile
import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener (private val viewModel:MainViewModel): WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket,response)
        // WebSocket connection opened
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        // Handle received text message
        super.onMessage(webSocket,text)
        val formattedText = text.replace("[", "").replace("]", "")
        Log.d("eduzal","${formattedText}")
        val values = formattedText.split(",").mapNotNull { it.toLongOrNull() }.toLongArray()
        viewModel.setAnimationValues(values)
    }



    private fun processMessage(message: String) {
        // Process the received message here
        // Example: Parsing the message as a LongArray
        val values = message.replace("[", "").replace("]", "")
            .split(",").map { it.toLongOrNull() ?: 0 }.toLongArray()

        // Now 'values' contains the LongArray obtained from the WebSocket message
        // You can process this data as needed, update ViewModel, pass to Fragments, etc.
        viewModel.setAnimationValues(values)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket,code,reason)
        // WebSocket connection is closing
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        // WebSocket connection is closed
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket,t,response)
        // WebSocket failure occurred
    }
}
