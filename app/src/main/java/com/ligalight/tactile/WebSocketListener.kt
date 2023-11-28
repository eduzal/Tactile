package com.ligalight.tactile
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
        val flags = formattedText.split(",").map { it.toBoolean() }.toBooleanArray()

        viewModel.setAnimationFlags(flags)
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
