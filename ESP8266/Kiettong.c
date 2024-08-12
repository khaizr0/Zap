#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

// Thay đổi thông tin mạng Wi-Fi của bạn
const char* ssid = " Cu+H2SO4( Loang ) -->";  // Thay thế bằng SSID mạng Wi-Fi của bạn
const char* password = "12345678910";  // Thay thế bằng mật khẩu mạng Wi-Fi của bạn

// Khai báo chân điều khiển relay
const int relayPin = D1;

// Tạo đối tượng server
ESP8266WebServer server(80);

// Biến để theo dõi thời gian bật relay
unsigned long relayOnTime = 0;
const unsigned long relayDuration = 1000; // 1 giây

void setup() {
  Serial.begin(115200);
  
  // Khởi tạo chân relay
  pinMode(relayPin, OUTPUT);
  digitalWrite(relayPin, LOW); // Relay bắt đầu ở trạng thái tắt

  // Kết nối ESP8266 với mạng Wi-Fi
  WiFi.begin(ssid, password);
  
  Serial.println("Connecting to WiFi...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  
  Serial.println("\nConnected to WiFi");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());

  // Cấu hình giao diện web
  server.on("/", HTTP_GET, handleRoot);
  server.on("/relay/on", HTTP_GET, handleRelayOn);

  server.begin();
}

void loop() {
  server.handleClient();
  
  // Kiểm tra và tắt relay sau 1 giây
  if (digitalRead(relayPin) == HIGH && millis() - relayOnTime >= relayDuration) {
    digitalWrite(relayPin, LOW);
    Serial.println("Relay is OFF after 1 second");
  }
}

void handleRoot() {
  String html = "<html><body>";
  html += "<h1>Relay Control</h1>";
  html += "<a href=\"/relay/on\">Turn Relay ON</a><br>";
  html += "</body></html>";
  server.send(200, "text/html", html);
}

void handleRelayOn() {
  digitalWrite(relayPin, HIGH);
  relayOnTime = millis(); // Lưu thời gian bật relay
  server.send(200, "text/html", "Relay is ON. It will turn OFF after 1 second. <a href=\"/\">Back</a>");
}