package hello.ncpspring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.ncpspring.key.NcpKey;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class CaptchaService {
    public String issueImageCaptcha() {
        ObjectMapper objectMapper = new ObjectMapper();
        String clientId = NcpKey.clientId;//애플리케이션 클라이언트 아이디값";
        String clientSecret = NcpKey.clientSecret;//애플리케이션 클라이언트 시크릿값";
        String result = null;
        try {
            String code = "0"; // 키 발급시 0,  캡차 이미지 비교시 1로 세팅
            String apiURL = "https://naveropenapi.apigw.ntruss.com/captcha/v1/nkey?code=" + code;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 오류 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            JsonNode jsonNode = objectMapper.readTree(response.toString());
            result = jsonNode.get("key").asText();
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public boolean verifyImageCaptcha(String key, String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        String clientId = NcpKey.clientId;//애플리케이션 클라이언트 아이디값";
        String clientSecret = NcpKey.clientSecret;//애플리케이션 클라이언트 시크릿값";
        boolean isSuccess = false;
        try {
            String code = "1"; // 키 발급시 0,  캡차 이미지 비교시 1로 세팅
            String apiURL = "https://naveropenapi.apigw.ntruss.com/captcha/v1/nkey?code=" + code +"&key="+ key + "&value="+ value;

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 오류 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            JsonNode jsonNode = objectMapper.readTree(response.toString());
            isSuccess = jsonNode.get("result").asBoolean();
        } catch (Exception e) {
            System.out.println(e);
        }
        return isSuccess;
    }
}
