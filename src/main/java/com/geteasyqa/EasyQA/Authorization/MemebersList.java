package com.geteasyqa.EasyQA.Authorization;


import com.geteasyqa.EasyQA.API.GetResponse;
import com.geteasyqa.EasyQA.API.Initialization;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yanagusti on 3/17/17.
 */
public class MemebersList extends Initialization {

    public MemebersList(String url) {


        super(url);
    }

    /**
     * Method for getting the list of members of your project in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return Map with names and emails of project members
     */

    public Map <String, String> getMembersList(String token) throws IOException, JSONException {

        Call<ResponseBody> call= easyqaUserAPI.getMembersList(token);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();

        if (result.startsWith("{")) {
            JSONObject res = new JSONObject(result);
            JSONArray jsonArray = res.getJSONArray("users");
            String resul = jsonArray.toString();
            Map<String, String> members = new GetResponse().getMapString(resul, "id", "name");
            return members;

        }else {
            System.out.println(result);
            return null;
        }

    }
}
