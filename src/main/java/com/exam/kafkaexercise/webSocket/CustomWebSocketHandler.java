package com.exam.kafkaexercise.webSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {

    HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //메시지 발송
        String msg = message.getPayload();
        System.out.println("=====================handleTextMessage :"+msg+"=====================");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //소켓 연결
        System.out.println("=====================Connection Socket Success=====================");
        sessionMap.put(session.getId(), session);
        log.info("@@@@"+sessionMap);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("=====================Disconnection Socket Success=====================");
        //소켓 종료
        sessionMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    private void sendMessage(WebSocketSession session, String payload){
        log.info("@@@@@@SendMessage");
        TextMessage message = new TextMessage(payload);
        try {
            session.sendMessage(message);
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }

    /**
     * @desc 전체 세션에 메세지 보내기.
     * @param payload
     */
    public void sendMessageToAll(String payload) {
        sessionMap.forEach((s, webSocketSession) -> this.sendMessage(webSocketSession,payload));
    }
}
