package test;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
//JSOUP가 아닌 JSON import
import org.json.JSONObject;

/*

 * MQTT Publisher - MqttPublisher_API.java
 * MQTT Protocol 에서 Publisher 역할을 맡은 MqttPublisher_API.java 는 기존의 기온,습도 데이터를 공공데이터포털에서 받아오는 것이 아니라
 꽃가루 농도 데이터를 받아옴으로서 해당 데이터를 퍼블리싱 하는 역할을 수행하고 있다.
 API 호출을 통해 꽃가루 Data 값을 받아오고, 받아온 Data 를 Broker Server 에 퍼블리싱 하는 역할을 수행하고 있다.

 */

public class MqttPublisher_API {

    public static void main(String[] args) {
        //퍼블리싱을 Broker Server 의 주소와 ClientID 정의, 메세지의 지속성을 메모리에 유지하기 위한 객체 생성
        String broker = "tcp://127.0.0.1:1883";
        String clientId = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        /* 
        MqttClient 라는 객체를 생성하여 connect() 함수를 호출하고 함수에 인자값을 전달하여
        Broker Server 와 연결하는 Part.
        연결 성공시 try 구문이 실행되어 try 구문에 구현된 내용이 Console 에 출력

        MqttMessage 객체를 생성하여 데이터를 메시지로 설정하고, QoS(Quality of Service) 레벨을 2로 설정.
        publish() 메소드를 호출하여 "pollen" 주제에 메시지를 퍼블리시 하고 
        퍼블리시된 메시지를 출력.

        */
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            sampleClient.connect();
            System.out.println("Connected");

            // Call the API and get the pollen data
            String pollenData = getPollenData();
            System.out.println("Pollen Data: " + pollenData);

            // Publish the pollen data to the MQTT topic
            MqttMessage message = new MqttMessage(pollenData.getBytes());
            message.setQos(2);
            sampleClient.publish("pollen", message);
            System.out.println("Publishing message: " + pollenData);
            System.out.println("Message published");

            sampleClient.disconnect();
            System.out.println("Disconnected");
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*

    꽃가루 Data = Pollen
    getPollenData() 메소드를 호출하여 꽃가루 데이터를 가져온다.
    serviceKey 는 공공데이터포털에서 받은 개인의 고유값이며 개인이 받은 할당값을 변수에 지정하였다.
    serviceKey 는 보고서 참조.
    urlBuilder 는 공공데이터포털에서 제공한 꽃가루 농도 지수 가이드에 End Point 로 소개되어 있으며 append() 함수로 묶은 내용들은
    End Point 에 함께 데이터 값을 받아오는 값들이다.

    disconnect() 메소드를 호출하여 브로커와의 연결을 종료하고
    연결이 종료되면 "Disconnected" 메시지를 출력 하는 구문과

    Pollen 값을 받아오지 못하였을 때 N/A 로 출력하는 Error 처리 구문도 포함되어 있다.

    */

    /* 
    
    getPollenData() 
    API 호출을 위해 URL을 빌드하고 서비스 키, 페이지 번호, 행 수, 데이터 타입, 지역 번호, 시간을 설정.
    HttpURLConnection을 사용하여 GET 요청을 보낸 후 응답 코드를 확인하고, 응답에 따라 스트림을 읽음.
    응답을 문자열로 읽어 StringBuilder에 저장 후 응답 문자열을 JSON 객체로 변환하여 파싱.

    공공데이터포털을 통해 얻은 Java Code 에서 발생한 값을 받아오지 못하는 Error 로 인해 JSON 객체 import
    - JSON
    JSON 응답에서 헤더와 바디를 가져온다
    resultCode가 "00"이면 성공으로 간주하고, 꽃가루 위험 지수를 추출한다.
    추출한 데이터를 JSON 문자열로 반환한다.
    에러 발생 시 에러 메시지를 출력하고, 기본 값을 반환.

    */
    private static String getPollenData() throws Exception {
        String serviceKey = "8lHAYh%2BsxTLCSmgNmVeKF0UAExuHiWLOatYQ4QC49aCtcCRCTeP61N4NwNBFBIfcAXGYnaL7oVjaOzlHsrCRTA%3D%3D";
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV3/getOakPollenRiskIdxV3");
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=10");
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=JSON");
        urlBuilder.append("&" + URLEncoder.encode("areaNo", "UTF-8") + "=1100000000");
        urlBuilder.append("&" + URLEncoder.encode("time", "UTF-8") + "=2024061506");

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String response = sb.toString();
        System.out.println("API Response: " + response);

        try {
            // Parse JSON response and extract the pollen risk index if available
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject header = jsonResponse.getJSONObject("response").getJSONObject("header");
            String resultCode = header.getString("resultCode");
            
            if ("00".equals(resultCode)) {
                JSONObject body = jsonResponse.getJSONObject("response").getJSONObject("body");
                JSONObject items = body.getJSONObject("items");
                JSONObject item = items.getJSONArray("item").getJSONObject(0);
                double pollenRiskIndex = item.getDouble("today");

                // Return the data as a JSON string
                return new JSONObject().put("pollen", pollenRiskIndex).toString();
            } else {
                String resultMsg = header.getString("resultMsg");
                System.out.println("API Error: " + resultMsg);
                // Return default or error data
                return new JSONObject().put("pollen", "N/A").toString();
            }
        } catch (Exception e) {
            System.out.println("Error parsing JSON response");
            e.printStackTrace();
            // Return default or error data
            return new JSONObject().put("pollen", "N/A").toString();
        }
    }
}
