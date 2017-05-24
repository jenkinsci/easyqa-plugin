package com.geteasyqa.EasyQA.Authorization;


import com.geteasyqa.EasyQA.API.Initialization;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * Created by yanagusti on 3/17/17.
 */
public class SignOut extends Initialization {

    public SignOut(String url) {
        super(url);
    }

    /**
     * Method for logged out from EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return string message about success sign out or error
     */

    public String signOut(String token, String auth_token)  throws IOException, JSONException {

        Call<ResponseBody> call= easyqaUserAPI.signOut(token, auth_token);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();


        if (result.startsWith("{")) {
            JSONObject jsonObj = new JSONObject(result);
            String message = jsonObj.getString("message");
            return message;
        }else {
            System.out.println(result);
            return result;
        }

    }
}
