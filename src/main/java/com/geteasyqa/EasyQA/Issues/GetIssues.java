package com.geteasyqa.EasyQA.Issues;

import com.geteasyqa.EasyQA.API.GetResponse;
import com.geteasyqa.EasyQA.API.Initialization;
import com.google.gson.JsonObject;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yanagusti on 3/24/17.
 */
public class GetIssues extends Initialization {
    public GetIssues(String url) {
        super(url);
    }

    /**
     * Method for getting all issues in the project in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return map with ids and summaries of issues
     */

    public Map<String, String> getIssueList(String token, String auth_token) throws IOException, JSONException {

        Call<ResponseBody> call= easyqaUserAPI.getIssueList(token, auth_token);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        Map <String, String> issues = new GetResponse().getMapString(result, "id", "summary");
        return issues;

    }

    /**
     * Method for getting issue info with mobile build as a test object in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param packageName name of application package
     * @param buildVersionCode code version of your build
     * @param buildVersionName version name of your build
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return JSONObject result of the server response
     */

    public JSONObject getIssueInfoWithMobileBuild(String token, String packageName, String buildVersionCode, String buildVersionName) throws IOException, JSONException {
        JsonObject cred = new JsonObject();
        cred.addProperty("token", token);
        cred.addProperty("packageName", packageName);
        cred.addProperty("buildVersionCode", buildVersionCode);
        cred.addProperty("buildVersionName", buildVersionName);

        Call<ResponseBody> call= easyqaUserAPI.getIssueWithMobileBuild(cred);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        JSONObject jsonObject = new JSONObject(result);

        return jsonObject;

    }

    /**
     * Method for getting issue info with a link of the tested website as a test object in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param link the link of tested website as a test object
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return JSONObject result of the server response
     */

    public JSONObject getIssueInfoWithSite(String token, String auth_token, String link) throws IOException, JSONException {
        JsonObject cred = new JsonObject();
        cred.addProperty("token", token);
        cred.addProperty("auth_token", auth_token);
        cred.addProperty("link", link);

        Call<ResponseBody> call= easyqaUserAPI.getIssueWithSite(cred);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        JSONObject jsonObject = new JSONObject(result);

        return jsonObject;

    }

    /**
     * Method for getting issue info by the unique issue ID in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param id the unique issue ID
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return JSONObject result of the server response
     */
    public JSONObject getIssueByUniqueID(String token, String auth_token, String id) throws IOException, JSONException {

        Call<ResponseBody> call= easyqaUserAPI.getIssueByID(id, token, auth_token);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        JSONObject jsonObject = new JSONObject(result);

        return jsonObject;

    }

    /**
     * Method for getting issue info by the issue ID in the project in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param id the issue ID in the project
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return JSONObject result of the server response
     */

    public JSONObject getIssueByProjectID(String token, String auth_token, String id) throws IOException, JSONException {

        Call<ResponseBody> call= easyqaUserAPI.getIssueByIDInProject(id, token, auth_token);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        JSONObject jsonObject = new JSONObject(result);

        return jsonObject;

    }
}
