package com.geteasyqa.EasyQA.API;

import com.google.gson.JsonObject;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * Created by yanagusti on 3/23/17.
 */
public interface EasyQAAPI {

    @Headers("Content-Type: application/json")
    @GET(Initialization.GET_MEMBERS_LIST)
    Call<ResponseBody> getMembersList(@Query("token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.GET_USER)
    Call<ResponseBody> getUserByEmail(@Query("email") String email);

    @Headers("Content-Type: application/json")
    @GET(Initialization.GET_USER)
    Call<ResponseBody> getUserByID(@Query("id") String id);

    @Headers("Content-Type: application/json")
    @GET(Initialization.GET_ORG_MEMBERS_LIST)
    Call<ResponseBody> getOrgMembersListByOrgID(@Query("organization_id") String organization_id, @Query("auth_token") String auth_token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.GET_ORG_MEMBERS_LIST)
    Call<ResponseBody> getOrgMembersListByOrgTitle(@Query("organization_title") String organization_title, @Query("auth_token") String auth_token);

    @Headers("Content-Type: application/json")
    @POST(Initialization.POST_SIGN_IN)
    Call<ResponseBody> signIn(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @DELETE(Initialization.DELETE_SIGN_OUT)
    Call<ResponseBody> signOut(@Query("token") String token, @Query("auth_token") String auth_token);



    @Headers("Content-Type: application/json")
    @POST(Initialization.ORGANIZATION)
    Call<ResponseBody> createOrganization(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ORGANIZATION)
    Call<ResponseBody> getOrganizationsList(@Query("auth_token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ID_ORGANIZATION)
    Call<ResponseBody> getOrganization(@Path("id") String id, @Query("auth_token") String token);

    @Headers("Content-Type: application/json")
    @PUT(Initialization.ID_ORGANIZATION)
    Call<ResponseBody> updateOrganization(@Path("id") String id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.ID_ORGANIZATION, hasBody = true)
    Call<ResponseBody> deleteOrganization(@Path("id") String id, @Body JsonObject jsonObject);



    @Headers("Content-Type: application/json")
    @POST(Initialization.PROJECT)
    Call<ResponseBody> createProject(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @GET(Initialization.PROJECT)
    Call<ResponseBody> getProjectsList(@Query("auth_token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ID_PROJECTS)
    Call<ResponseBody> getProject(@Path("id") String id, @Query("auth_token") String token);

    @Headers("Content-Type: application/json")
    @PUT(Initialization.ID_PROJECTS)
    Call<ResponseBody> updateProject(@Path("id") String id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.ID_PROJECTS, hasBody = true)
    Call<ResponseBody> deleteProject(@Path("id") String id, @Body JsonObject jsonObject);



    @Headers("Content-Type: application/json")
    @POST(Initialization.ORGANIZATION_ROLE)
    Call<ResponseBody> assigneOrganizationRole(@Path("organization_id") String organization_id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.ID_ROLE, hasBody = true)
    Call<ResponseBody> deleteRole(@Path("id") String id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ORGANIZATION_ROLE)
    Call<ResponseBody> getOrganizationsRolesList(@Path("organization_id") String organization_id, @Query("auth_token") String auth_token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ORGANIZATION_ROLE)
    Call<ResponseBody> getProjectRolesList(@Path("organization_id") String organization_id, @Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ID_ROLE)
    Call<ResponseBody> getRole(@Path("id") String id, @Query("auth_token") String auth_token);

    @Headers("Content-Type: application/json")
    @PUT(Initialization.ID_ROLE)
    Call<ResponseBody> updateRole(@Path("id") String id, @Body JsonObject jsonObject);




    @Headers("Content-Type: application/json")
    @POST(Initialization.UPLOAD_CRASH)
    Call<ResponseBody> uploadCrash(@Body JsonObject jsonObject);

    @Multipart
    @POST(Initialization.UPLOAD_ISSUE)
    Call<ResponseBody> uploadBugReport(@PartMap Map<String, RequestBody> report);

    @Headers("Content-Type: application/json")
    @GET(Initialization.GET_ISSUE_LIST)
    Call<ResponseBody> getIssueList(@Query("token") String token, @Query("auth_token") String auth_token);

    @Headers("Content-Type: application/json")
    @POST(Initialization.ISSUE_INFO)
    Call<ResponseBody> getIssueWithMobileBuild(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST(Initialization.ISSUE_INFO)
    Call<ResponseBody> getIssueWithSite(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ID_ISSUE)
    Call<ResponseBody> getIssueByID(@Path("id") String id, @Query("token") String token, @Query("auth_token") String auth_token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.PROJECT_ID_ISSUE)
    Call<ResponseBody> getIssueByIDInProject(@Path("id") String id, @Query("token") String token, @Query("auth_token") String auth_token);


    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.ID_ISSUE, hasBody = true)
    Call<ResponseBody> deleteIssue(@Path("id") String id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.PROJECT_ID_ISSUE, hasBody = true)
    Call<ResponseBody> deleteIssueByIDInProject(@Path("id") String id, @Body JsonObject jsonObject);

    @Multipart
    @PUT(Initialization.ID_ISSUE)
    Call<ResponseBody> updateIssue(@Path("id") String id, @PartMap Map<String, RequestBody> report);

    @Multipart
    @PUT(Initialization.PROJECT_ID_ISSUE)
    Call<ResponseBody> updateIssueByIdInProject(@Path("id") String id, @PartMap Map<String, RequestBody> report);

    @Multipart
    @POST(Initialization.UPLOAD_ATTACHMENT)
    Call<ResponseBody> createAttachment(@Path("id") String id, @PartMap Map<String, RequestBody> attachment);

    @Multipart
    @POST(Initialization.UPLOAD_ATTACHMENT_ID_PROJECT)
    Call<ResponseBody> createAttachmentByIdInProject(@Path("id") String id, @PartMap Map<String, RequestBody> attachment);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.DELETE_ATTACHMENT, hasBody = true)
    Call<ResponseBody> deleteAttachment(@Path("id") String id, @Body JsonObject jsonObject);




    @Headers("Content-Type: application/json")
    @POST(Initialization.STATUSES)
    Call<ResponseBody> createStatus(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @GET(Initialization.STATUSES)
    Call<ResponseBody> getStatusesList(@Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ID_STATUS)
    Call<ResponseBody> getStatus(@Path("id") String id, @Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @PUT(Initialization.ID_STATUS)
    Call<ResponseBody> updateStatus(@Path("id") String id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.ID_STATUS, hasBody = true)
    Call<ResponseBody> deleteStatus(@Path("id") String id, @Body JsonObject jsonObject);


    @Headers("Content-Type: application/json")
    @POST(Initialization.TEST_PLAN)
    Call<ResponseBody> createTestPlan(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @GET(Initialization.TEST_PLAN)
    Call<ResponseBody> getTestPlansList(@Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ID_TEST_PLAN)
    Call<ResponseBody> getTestPlan(@Path("id") String id, @Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @PUT(Initialization.ID_TEST_PLAN)
    Call<ResponseBody> updateTestPlan(@Path("id") String id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.ID_TEST_PLAN, hasBody = true)
    Call<ResponseBody> deleteTestPlan(@Path("id") String id, @Body JsonObject jsonObject);



    @Headers("Content-Type: application/json")
    @POST(Initialization.TEST_CASES)
    Call<ResponseBody> createTestCase(@Path("test_module_id") String test_module_id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @GET(Initialization.MODULE_TEST_CASES)
    Call<ResponseBody> getTestCasesListOfModule(@Path("test_module_id") String test_module_id, @Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.TEST_PLAN_TEST_CASES)
    Call<ResponseBody> getTestCasesListOfTestPlan(@Path("test_plan_id") String test_plan_id, @Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ID_TEST_CASE)
    Call<ResponseBody> getTestCase(@Path("id") String id, @Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @PUT(Initialization.ID_TEST_CASE)
    Call<ResponseBody> updateTestCase(@Path("id") String id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.ID_TEST_CASE, hasBody = true)
    Call<ResponseBody> deleteTestCase(@Path("id") String id, @Body JsonObject jsonObject);



    @Headers("Content-Type: application/json")
    @POST(Initialization.TEST_MODULE)
    Call<ResponseBody> createTestModule(@Path("test_plan_id") String test_plan_id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @GET(Initialization.TEST_MODULE)
    Call<ResponseBody> getTestModulesList(@Path("test_plan_id") String test_plan_id, @Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ID_TEST_MODULE)
    Call<ResponseBody> getTestModule(@Path("id") String id, @Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @PUT(Initialization.ID_TEST_MODULE)
    Call<ResponseBody> updateTestModule(@Path("id") String id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.ID_TEST_MODULE, hasBody = true)
    Call<ResponseBody> deleteTestModule(@Path("id") String id, @Body JsonObject jsonObject);




    @Headers("Content-Type: application/json")
    @POST(Initialization.TEST_RUN)
    Call<ResponseBody> createTestRun(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @GET(Initialization.TEST_RUN)
    Call<ResponseBody> getTestRunsList(@Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ID_TEST_RUN)
    Call<ResponseBody> getTestRun(@Path("id") String id, @Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @PUT(Initialization.ID_TEST_RUN)
    Call<ResponseBody> updateTestRun(@Path("id") String id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.ID_TEST_RUN, hasBody = true)
    Call<ResponseBody> deleteTestRun(@Path("id") String id, @Body JsonObject jsonObject);






    @Headers("Content-Type: application/json")
    @GET(Initialization.TEST_RUN_RESULT)
    Call<ResponseBody> getTestRunResultsList(@Path("test_run_id") String test_run_id, @Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ID_TEST_RUN_RESULT)
    Call<ResponseBody> getTestRunResult(@Path("id") String id, @Query("auth_token") String auth_token, @Query("token") String token);

    @Headers("Content-Type: application/json")
    @PUT(Initialization.ID_TEST_RUN_RESULT)
    Call<ResponseBody> updateTestRunResult(@Path("id") String id, @Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.ID_TEST_RUN_RESULT, hasBody = true)
    Call<ResponseBody> deleteTestRunResult(@Path("id") String id, @Body JsonObject jsonObject);




    @Headers("Content-Type: application/json")
    @POST(Initialization.TEST_OBJECT)
    Call<ResponseBody> uploadLink(@Body JsonObject jsonObject);

    @Multipart
    @POST(Initialization.TEST_OBJECT)
    Call<ResponseBody> uploadBuild(@PartMap Map<String, RequestBody> attachment);

    @Headers("Content-Type: application/json")
    @GET(Initialization.TEST_OBJECT)
    Call<ResponseBody> getTestObjectLinks(@Query("token") String token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.TEST_OBJECT)
    Call<ResponseBody> getTestObjecsList(@Query("token") String token, @Query("auth_token") String auth_token);

    @Headers("Content-Type: application/json")
    @GET(Initialization.ID_TEST_OBJECT)
    Call<ResponseBody> getTestObjec(@Path("id") String id, @Query("token") String token, @Query("auth_token") String auth_token);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = Initialization.ID_TEST_OBJECT, hasBody = true)
    Call<ResponseBody> deleteTestObject(@Path("id") String id, @Body JsonObject jsonObject);
}
