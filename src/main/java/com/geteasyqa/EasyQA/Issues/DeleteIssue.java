package com.geteasyqa.EasyQA.Issues;


import com.geteasyqa.EasyQA.API.Initialization;
import com.google.gson.JsonObject;
import okhttp3.ResponseBody;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * Created by yanagusti on 3/21/17.
 */
public class DeleteIssue extends Initialization {

    public DeleteIssue(String url) {
        super(url);
    }


    /**
     * Method for deleting an issue by the unique issue ID on Issues page within your project in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param id the unique issue ID
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     */

    public void deleteByUniqueID(String token, String auth_token, String id) throws IOException, JSONException {
        JsonObject cred = new JsonObject();
        cred.addProperty("token", token);
        cred.addProperty("auth_token", auth_token);

        Call<ResponseBody> call= easyqaUserAPI.deleteIssue(id,cred);

        Response<ResponseBody> bodyResponse = call.execute();
        String response = bodyResponse.body().string();

        if (response.startsWith("{")) {
            System.out.println("Delete Issue "+id+"=" + response);

        }else {
            System.out.println(response);
        }

    }

    /**
     * Method for deleting an issue by the issue ID in the project on Issues page in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param id the issue ID in the project
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     */

    public void deleteByProjectID(String token, String auth_token, String id) throws IOException, JSONException {
        JsonObject cred = new JsonObject();
        cred.addProperty("token", token);
        cred.addProperty("auth_token", auth_token);

        Call<ResponseBody> call= easyqaUserAPI.deleteIssueByIDInProject(id, cred);

        Response<ResponseBody> bodyResponse = call.execute();
        bodyResponse.body().string();


    }
}
