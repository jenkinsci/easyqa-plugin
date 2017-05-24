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
public class OrganizationMembersList extends Initialization {
    public OrganizationMembersList(String url) {
        super(url);
    }

    /**
     * Method for getting the list of members of your organization in EasyQA
     * @param organization_id an id of your organization
     * @param auth_token your authorization token in EasyQA
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return Map with names and emails of organization members
     */

    public Map<String, String> getOrgMembersListByOrgId(String organization_id, String auth_token) throws IOException, JSONException {

        Call<ResponseBody> call= easyqaUserAPI.getOrgMembersListByOrgID(organization_id, auth_token);

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
     * Method for getting the list of members of your organization in EasyQA
     * @param organization_title a title of your organization
     * @param auth_token your authorization token in EasyQA
     * @throws IOException for incorrect parsing of the server response
     * @throws JSONException if the server returns not Json object
     * @return Map with names and emails of organization members
     */

    public Map<String, String> getOrgMembersListByOrgTitle(String organization_title, String auth_token) throws IOException, JSONException {

        Call<ResponseBody> call= easyqaUserAPI.getOrgMembersListByOrgTitle(organization_title, auth_token);

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
