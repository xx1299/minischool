package com.s1mple.minischool.web;

import com.s1mple.minischool.domain.AjxsResponse;
import com.s1mple.minischool.domain.Message;
import com.s1mple.minischool.domain.Vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@ServerEndpoint(value = "/websocket/{user_id}",encoders = {ServerEncoder.class})
public class WebSocketServer {

    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<WebSocketServer> SessionSet = new CopyOnWriteArraySet<>();

    private Session session;

    private Long user_id;

    public Long getUser_id() {
        return user_id;
    }

    public Session getSession() {
        return session;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("user_id") Long user_id) throws IOException {
        this.user_id = user_id;
        this.session = session;
        SessionSet.add(this);
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入，user_id：{}", this.user_id);
        log.info("有连接加入，当前连接数为：{}", cnt);
        SendMessage(session, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SessionSet.remove(this);
        log.info("有连接关闭，user_id：{}", this.user_id);
        int cnt = OnlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("来自客户端的消息：{}",message);
        SendMessage(session, "收到消息，消息内容："+message);
    }

    /**
     * 出现错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     * @param session  session
     * @param message  消息
     */
    private static void SendMessage(Session session, String message) throws IOException {

        session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)",message,session.getId()));

    }

    private static void SendOne(Session session, Object object) throws IOException, EncodeException {

        session.getBasicRemote().sendObject(object);

    }

    /**
     * 群发消息
     * @param message  消息
     */
    public static void BroadCastInfo(String message) throws IOException {
        for (WebSocketServer webSocketServer : SessionSet) {
            if(webSocketServer.getSession().isOpen()){
                SendMessage(webSocketServer.getSession(), message);
            }
        }
    }


    public static void SendMessage(Long reciveId,String message) throws IOException {
        WebSocketServer server = null;
        for (WebSocketServer s : SessionSet) {
            if(s.getUser_id().equals(reciveId)){
                server = s;
                break;
            }
        }
        if(server!=null){
            SendMessage(server.getSession(), message);
        } else{
            log.warn("没有找到你指定ID的会话：{}",reciveId);
        }
    }

    public static Boolean SendOne(Long reciveId, Object object) throws IOException, EncodeException {
        WebSocketServer server = null;
        for (WebSocketServer s : SessionSet) {
            System.out.println(s.getUser_id());
            if(s.getUser_id().equals(reciveId)){
                server = s;
                break;
            }
        }
        if(server!=null){
            SendOne(server.getSession(), AjxsResponse.success(object));
            log.info("消息'"+object+"'发送成功");
            return true;
        } else{
            return false;
        }
    }

}