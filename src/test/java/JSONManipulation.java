import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONManipulation {
    public static void saveUsersData(String email, String password) throws IOException, ParseException {
        JSONParser parser=new JSONParser();
        JSONArray jsonArray= (JSONArray) parser.parse(new FileReader("./src/test/resources/users.json"));
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("email",email);
        jsonObject.put("password",password);
        jsonArray.add(jsonObject);

        FileWriter writer=new FileWriter("./src/test/resources/users.json");
        writer.write(jsonArray.toJSONString());
        writer.flush();
        writer.close();

    }
    public static JSONObject readJSONData() throws IOException, ParseException {
        JSONParser jsonParser=new JSONParser();
        JSONArray jsonArray= (JSONArray) jsonParser.parse(new FileReader("./src/test/resources/users.json"));
        return (JSONObject) jsonArray.get(jsonArray.size()-1);
    }
}
