// websocket.js
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;

export function connectWebSocket(docId, onMessage, onConnect) {
  return new Promise((resolve) => {
    try {
      stompClient = new Client({
        webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
        debug: () => {}, // disable logs
        reconnectDelay: 5000, // auto reconnect every 5s
        onConnect: () => {
          const sub = stompClient.subscribe(`/topic/document/${docId}`, (msg) => {
            if (msg.body) {
              try {
                onMessage(JSON.parse(msg.body));
              } catch (e) {
                console.error("WebSocket parse error:", e);
              }
            }
          });

          if (onConnect) onConnect();

          resolve({
            connected: true,
            disconnect: () => {
              try { sub.unsubscribe(); } catch(e) {}
              try { stompClient.deactivate(); } catch(e) {}
            }
          });
        },
        onStompError: (frame) => {
          console.error("STOMP error:", frame.headers['message'], frame.body);
          resolve({ connected: false, disconnect: () => {} });
        }
      });

      stompClient.activate();
    } catch (e) {
      console.error("WebSocket init error:", e);
      resolve({ connected: false, disconnect: () => {} });
    }
  });
}

export function sendEditViaWS(docId, payload) {
  if (!stompClient || !stompClient.connected) return false;
  try {
    stompClient.publish({
      destination: `/app/edit/${docId}`,
      body: JSON.stringify(payload)
    });
    return true;
  } catch (e) {
    console.error("Send WS error:", e);
    return false;
  }
}
