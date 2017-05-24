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
 * Created by yanagusti on 4/14/17.
 */
public class GetUser extends Initialization {
    public GetUser(String url) {
        super(url);
    }

    /**
     * Method for getting the user by email
     * @param email an email of the user
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return Map with name and id of the user
     */

    public Map<String, String> getUserByEmail(String email) throws IOException, JSONException {

        Call<ResponseBody> call= easyqaUserAPI.getUserByEmail(email);

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

    /**
     * Method for getting the user by id
     * @param id an id of the user
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return Map with name and email of the user
     */

    public Map<String, String> getUserByID(String id) throws IOException, JSONException {

        Call<ResponseBody> call= easyqaUserAPI.getUserByID(id);
        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();

        if (result.startsWith("{")) {
            JSONObject res = new JSONObject(result);
            JSONArray jsonArray = res.getJSONArray("users");
            String resul = jsonArray.toString();
            Map<String, String> members = new GetResponse().getMapString(resul, "email", "name");
            return members;

        }else {
            System.out.println(result);
            return null;
        }

    }
}
