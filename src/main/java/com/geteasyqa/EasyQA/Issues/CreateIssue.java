package com.geteasyqa.EasyQA.Issues;


import com.geteasyqa.EasyQA.API.GetNotRequiredParameters;
import com.geteasyqa.EasyQA.API.Initialization;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by yanagusti on 3/16/17.
 */
public class CreateIssue extends Initialization {
    Integer id;

    public CreateIssue(String url) {
        super(url);
    }

    /**
     * Method for creating an issue without attachments on Issues page within your project in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param summary a short description of your issue
     * @param data additional parameters for an issue
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return id of the created issue
     */

    public Integer createIssue(String token, String auth_token, String summary, String... data) throws IOException, JSONException {

        Map<String, RequestBody> model= new GetNotRequiredParameters().getIntoMap("text/plain", data);

        model.put("token", RequestBody.create(textType, token));
        model.put("auth_token", RequestBody.create(textType, auth_token));
        model.put("summary", RequestBody.create(textType, summary));

        Call<ResponseBody> call= easyqaUserAPI.uploadBugReport(model);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        System.out.println(result);
        if (result.startsWith("{")) {
            JSONObject jsonObj = new JSONObject(result);
            id = jsonObj.getInt("id");
        }else {
            System.out.println(result);
        }
        return id;
    }

    /**
     * Method for creating an issue with attachments on Issues page within your project in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param auth_token your authorization token in EasyQA
     * @param summary a short description of your issue
     * @param files a list of files for attachments
     * @param data additional parameters for an issue
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return id of the created issue
     */

    public Integer createIssueWithAttachments(String token, String auth_token, String summary, ArrayList<File> files, String... data) throws IOException, JSONException {

        Map<String, RequestBody> model= new GetNotRequiredParameters().getIntoMap("text/plain", data);

        model.put("token", RequestBody.create(textType, token));
        model.put("auth_token", RequestBody.create(textType, auth_token));
        model.put("summary", RequestBody.create(textType, summary));

        for (File file : files) {
            String fileName = file.getName();
            model.put(String.format("%s\"; filename=\"%s", fileName, fileName),
                    RequestBody.create(fileName.contains(".mp4") ? videoType : imageType, file));
        }

        Call<ResponseBody> call= easyqaUserAPI.uploadBugReport(model);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        System.out.println(result);
        if (result.startsWith("{")) {
            JSONObject jsonObj = new JSONObject(result);
            id = Integer.valueOf(jsonObj.getInt("id"));
            System.out.println("Create success! Issue id=" + id);
        }else {
            System.out.println(result);
        }
        return id;
    }

    /**
     * Method for creating an issue without attachments anonymously for some Test Object on Issues page within your project in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param summary a short description of your issue
     * @param deviceSerial serial number of your device
     * @param deviceModel model name of you device
     * @param osVersion operation system version of your phone
     * @param test_object_id an id of the test object for which you create an issue
     * @param data additional parameters for an issue
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return id of the created issue
     */

    public Integer  createIssueFromDevice(String token, String summary, String deviceSerial, String deviceModel,
                                         String osVersion, String test_object_id, String... data) throws IOException, JSONException {

        Map<String, RequestBody> model= new GetNotRequiredParameters().getIntoMap("text/plain", data);

        model.put("token", RequestBody.create(textType, token));
        model.put("summary", RequestBody.create(textType, summary));
        model.put("deviceSerial", RequestBody.create(textType, deviceSerial));
        model.put("deviceModel", RequestBody.create(textType, deviceModel));
        model.put("osVersion", RequestBody.create(textType, osVersion));
        model.put("test_object_id", RequestBody.create(textType, test_object_id));


        Call<ResponseBody> call= easyqaUserAPI.uploadBugReport(model);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();
        if (result.startsWith("{")) {
            JSONObject jsonObj = new JSONObject(result);
            id = jsonObj.getInt("id");
        }else {
            System.out.println(result);
        }


        return id;
    }


    /**
     * Method for creating an issue with attachments anonymously for some Test Object on Issues page within your project in EasyQA
     * @param token of your project in EasyQA. You can generate it on Integrations page
     * @param summary a short description of your issue
     * @param deviceSerial serial number of your device
     * @param deviceModel model name of you device
     * @param osVersion operation system version of your phone
     * @param test_object_id an id of the test object for which you create an issue
     * @param files a list of files for attachments
     * @param data additional parameters for an issue
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return id of the created issue
     */

    public Integer createIssueFromDeviceWithAttachment(String token, String summary, String deviceSerial, String deviceModel,
                                             String osVersion, String test_object_id, ArrayList<File> files, String... data) throws IOException, JSONException {



        Map<String, RequestBody> model= new GetNotRequiredParameters().getIntoMap("text/plain", data);

        model.put("token", RequestBody.create(textType, token));
        model.put("summary", RequestBody.create(textType, summary));
        model.put("deviceSerial", RequestBody.create(textType, deviceSerial));
        model.put("deviceModel", RequestBody.create(textType, deviceModel));
        model.put("osVersion", RequestBody.create(textType, osVersion));
        model.put("test_object_id", RequestBody.create(textType, test_object_id));

        for (File file : files) {
            String fileName = file.getName();
            model.put(String.format("%s\"; filename=\"%s", fileName, fileName),
                    RequestBody.create(fileName.contains(".mp4") ? videoType : imageType, file));
        }


        Call<ResponseBody> call= easyqaUserAPI.uploadBugReport(model);

        Response<ResponseBody> bodyResponse = call.execute();
        String result = bodyResponse.body().string();

        JSONObject jsonObj = new JSONObject(result);
        System.out.println(result);
        id = jsonObj.getInt("id");

        return id;

    }


}
