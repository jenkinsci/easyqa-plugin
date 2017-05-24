package com.geteasyqa.EasyQA.Issues;

import com.geteasyqa.EasyQA.API.Initialization;
import com.google.gson.JsonObject;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanagusti on 3/31/17.
 */
public class IssueAttachments extends Initialization {


    public IssueAttachments(String url) {
        super(url);
    }


    /**
     * Method for adding attachment to the issue by the unique issue ID in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param id the unique issue ID
     * @param files a list of files for attachments
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return Integer value of attachment id
     */
    public Integer createAttachment(String token, String auth_token, String id, ArrayList<File> files) throws IOException, JSONException {

        Map<String, RequestBody> model= new HashMap();

        model.put("token", RequestBody.create(textType, token));
        model.put("auth_token", RequestBody.create(textType, auth_token));
        model.put("issue_id", RequestBody.create(textType, id));

        for (File file : files) {
            String fileName = file.getName();
            model.put(String.format("attachment\"; filename=\"%s", fileName),
                    RequestBody.create(fileName.contains(".mp4") ? videoType : imageType, file));
        }


        Call<ResponseBody> call= easyqaUserAPI.createAttachment(id, model);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        System.out.println(result);
        if (result.startsWith("{")) {
            JSONObject jsonObj = new JSONObject(result);
            Integer id_result = Integer.valueOf(jsonObj.getInt("id"));
            return id_result;
        }else {
            System.out.println(result);
            return null;
        }

    }

    /**
     * Method for adding attachment to the issue by the issue ID in the project in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param id the issue ID in the project
     * @param files a list of files for attachments
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return Integer value of attachment id
     */
    public Integer createAttachmentByIdInProject(String token, String auth_token, String id, ArrayList<File> files) throws IOException, JSONException {

        Map<String, RequestBody> model= new HashMap<>();

        model.put("token", RequestBody.create(textType, token));
        model.put("auth_token", RequestBody.create(textType, auth_token));
        model.put("issue_id", RequestBody.create(textType, id));


        for (File file : files) {
            String fileName = file.getName();
            model.put(String.format("attachment\"; filename=\"%s", fileName),
                    RequestBody.create(fileName.contains(".mp4") ? videoType : imageType, file));
        }


        Call<ResponseBody> call= easyqaUserAPI.createAttachmentByIdInProject(id, model);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        System.out.println(result);
        if (result.startsWith("{")) {
            JSONObject jsonObj = new JSONObject(result);
            Integer id_result = Integer.valueOf(jsonObj.getInt("id"));
            return id_result;
        }else {
            System.out.println(result);
            return null;
        }

    }
    /**
     * Method for deleting the attachment from the issue by the unique attachment ID in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param id the unique ID of attachment
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     */
    public void deleteAttachmentByID(String token, String auth_token, String id) throws IOException, JSONException {
        JsonObject cred = new JsonObject();
        cred.addProperty("token", token);
        cred.addProperty("auth_token", auth_token);

        Call<ResponseBody> call= easyqaUserAPI.deleteAttachment(id, cred);

        Response<ResponseBody> bodyResponse = call.execute();
        String response = bodyResponse.body().string();
        System.out.println(response);

        if (response.startsWith("{")) {
            System.out.println("Delete Issue "+id+"=" + response);

        }else {
            System.out.println(response);
        }

    }
}
