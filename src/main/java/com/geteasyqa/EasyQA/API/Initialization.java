package com.geteasyqa.EasyQA.API;

import okhttp3.MediaType;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yanagusti on 3/23/17.
 */
public class Initialization {

    /**
     * The list of EasyQA API methods
     */


    public static final String GET_MEMBERS_LIST = "/api/v1/projects/members_list";
    public static final String GET_ORG_MEMBERS_LIST = "/api/v1/organization/members";
    public static final String GET_USER = "/api/v1/users/show";
    public static final String POST_SIGN_IN = "/api/v1/sign_in";
    public static final String DELETE_SIGN_OUT = "/api/v1/sign_out";

    public static final String UPLOAD_CRASH = "/projects/upload_crashes";
    public static final String UPLOAD_ISSUE = "/api/v1/projects/issues/create";
    public static final String GET_ISSUE_LIST = "/api/v1/issues";
    public static final String ISSUE_INFO = "/api/v1/projects/issue_info";
    public static final String ID_ISSUE = "/api/v1/issues/{id}";
    public static final String PROJECT_ID_ISSUE = "/api/v1/issues/pid{id}";
    public static final String UPLOAD_ATTACHMENT = "/api/v1/issues/{id}/attachments";
    public static final String UPLOAD_ATTACHMENT_ID_PROJECT = "/api/v1/issues/pid{id}/attachments";
    public static final String DELETE_ATTACHMENT = "/api/v1/attachments/{id}";

    public static final String ORGANIZATION = "/api/v1/organizations";
    public static final String ID_ORGANIZATION = "/api/v1/organizations/{id}";

    public static final String PROJECT = "/api/v1/projects";
    public static final String ID_PROJECTS = "/api/v1/projects/{id}";

    public static final String ORGANIZATION_ROLE = "/api/v1/organizations/{organization_id}/roles";
    public static final String ID_ROLE = "/api/v1/roles/{id}";

    public static final String STATUSES = "/api/v1/statuses";
    public static final String ID_STATUS = "/api/v1/statuses/{id}";

    public static final String TEST_PLAN = "/api/v1/test_plans";
    public static final String ID_TEST_PLAN = "/api/v1/test_plans/{id}";

    public static final String TEST_CASES = "/api/v1/test_modules/{test_module_id}/test_cases";
    public static final String MODULE_TEST_CASES = "/api/v1/test_module/{test_module_id}/test_cases";
    public static final String TEST_PLAN_TEST_CASES = "/api/v1/test_plan/{test_plan_id}/test_cases";
    public static final String ID_TEST_CASE = "/api/v1/test_cases/{id}";

    public static final String TEST_MODULE = "/api/v1/test_plans/{test_plan_id}/test_modules";
    public static final String ID_TEST_MODULE = "/api/v1/test_modules/{id}";

    public static final String TEST_OBJECT = "/api/v1/test_objects";
    public static final String ID_TEST_OBJECT = "/api/v1/test_objects/{id}";

    public static final String TEST_RUN = "/api/v1/test_runs";
    public static final String ID_TEST_RUN = "/api/v1/test_runs/{id}";

    public static final String TEST_RUN_RESULT = "/api/v1/test_runs/{test_run_id}/test_run_results";
    public static final String ID_TEST_RUN_RESULT = "/api/v1/test_run_results/{id}";





    public EasyQAAPI easyqaUserAPI;



    public MediaType imageType = MediaType.parse("image/*");
    public MediaType videoType = MediaType.parse("video/*");
    public MediaType textType = MediaType.parse("text/plain");

    public Initialization(String url){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


       easyqaUserAPI = retrofit.create(EasyQAAPI.class);
    }




}
