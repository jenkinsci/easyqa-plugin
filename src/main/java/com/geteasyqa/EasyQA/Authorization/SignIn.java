package com.geteasyqa.EasyQA.Authorization;


import com.geteasyqa.EasyQA.API.GetResponse;
import com.geteasyqa.EasyQA.API.Initialization;
import com.google.gson.JsonObject;
import okhttp3.ResponseBody;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yanagusti on 3/16/17.
 */
public class SignIn extends Initialization {
    public String auth_token_value;



    public SignIn(String url) {
        super(url);
    }

    /**
     * Method for logging into EasyQA and generate auth_token
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param email your registered email in EasyQA
     * @param password your registered password in EasyQA
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return Map with auth_token and name of user
     */


    public Map<String, String> signIn(String token, String email, String password) throws IOException, JSONException {

        JsonObject cred = new JsonObject();
        cred.addProperty("password", password);
        cred.addProperty("email", email);
        JsonObject parent=new JsonObject();
        parent.addProperty("token", token);
        parent.add("user", cred);

        Call<ResponseBody> call= easyqaUserAPI.signIn(parent);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        if (result.startsWith("{")) {
            Map<String, String> members = new GetResponse().getMapStringFromJSON(result, "auth_token", "name");
            auth_token_value = members.get("auth_token");
            return members;

        }else {
            System.out.println(result);
            return null;
        }

    }

    /**
     * Method for getting the auth_token of logged user
     * @return String auth_token of logged user
     */

    public String getAuth_token_value(){
        return auth_token_value;
    }



}
