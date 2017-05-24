package com.geteasyqa.EasyQA.Issues;


import com.geteasyqa.EasyQA.API.GetNotRequiredParameters;
import com.geteasyqa.EasyQA.API.GetResponse;
import com.geteasyqa.EasyQA.API.Initialization;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yanagusti on 3/31/17.
 */
public class UpdateIssue extends Initialization {
    public UpdateIssue(String url) {
        super(url);
    }

    /**
     * Method for updating the issue by the unique ID in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param id the unique ID of issue
     * @param data additional parameters for an issue
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return map with ids and summaries
     */

    public Map<String, String> updateIssue(String token, String auth_token, String id, String... data) throws IOException, JSONException {

        Map<String, RequestBody> model= new GetNotRequiredParameters().getIntoMap("text/plain", data);

        model.put("token", RequestBody.create(textType, token));
        model.put("auth_token", RequestBody.create(textType, auth_token));

        Call<ResponseBody> call= easyqaUserAPI.updateIssue(id, model);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        if (result!=null){
            Map<String, String> issue = new GetResponse().getMapStringFromJSON(result, "id", "summary");
            return issue;
        }else {
            System.out.println("Something went wrong");
            return null;
        }
    }

    /**
     * Method for updating the issue by ID in the project in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param id ID in the project
     * @param data additional parameters for an issue
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     */
    public void updateIssueByIdInProject(String token, String auth_token, String id, String... data) throws IOException, JSONException {

        Map<String, RequestBody> model= new GetNotRequiredParameters().getIntoMap("text/plain", data);

        model.put("token", RequestBody.create(textType, token));
        model.put("auth_token", RequestBody.create(textType, auth_token));

        Call<ResponseBody> call= easyqaUserAPI.updateIssueByIdInProject(id, model);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        if (result!=null){
            System.out.println(result);

            if (result.startsWith("{")) {
                JSONObject jsonObj = new JSONObject(result);
                Integer id_value = jsonObj.getInt("id");
                System.out.println("Issue is updated! id=" + id_value);

            }else {
                System.out.println(result);
            }
        }else {
            System.out.println("Something went wrong");
        }
    }

    /**
     * Method for updating status of the issue by unique ID in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param id unique issue ID in EasyQA
     * @param status_id ID of status in the project in EasyQA. For example: Submitted has ID=1, To Be Discuss=2, Reopened=3, In Progress=4, Code Review=5, QA Review=6, Closed=7
     * @param data additional parameters for an issue
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     */

    public void updateIssueStatus(String token, String auth_token, String id, String status_id, String... data) throws IOException, JSONException {

        Map<String, RequestBody> model= new GetNotRequiredParameters().getIntoMap("text/plain", data);

        model.put("token", RequestBody.create(textType, token));
        model.put("auth_token", RequestBody.create(textType, auth_token));
        model.put("status_id", RequestBody.create(textType, status_id));

        Call<ResponseBody> call= easyqaUserAPI.updateIssue(id, model);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        if (result!=null){
            System.out.println(result);

            if (result.startsWith("{")) {
                JSONObject jsonObj = new JSONObject(result);
                Integer id_value = jsonObj.getInt("id");
                System.out.println("Issue is updated! id=" + id_value);

            }else {
                System.out.println(result);
            }
        }else {
            System.out.println("Something went wrong");
        }
    }

    /**
     * Method for updating status of the issue by ID in the project in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param id ID in the project
     * @param status_id ID of status in the project in EasyQA. For example: Submitted has ID=1, To Be Discuss=2, Reopened=3, In Progress=4, Code Review=5, QA Review=6, Closed=7
     * @param data additional parameters for an issue
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     */
    public void updateIssueStatusByIdInProject(String token, String auth_token, String id, String status_id, String... data) throws IOException, JSONException {

        Map<String, RequestBody> model= new GetNotRequiredParameters().getIntoMap("text/plain", data);

        model.put("token", RequestBody.create(textType, token));
        model.put("auth_token", RequestBody.create(textType, auth_token));
        model.put("status_id", RequestBody.create(textType, status_id));

        Call<ResponseBody> call= easyqaUserAPI.updateIssueByIdInProject(id, model);

        Response<ResponseBody> bodyResponse = call.execute();
        bodyResponse.body().string();


    }
}
