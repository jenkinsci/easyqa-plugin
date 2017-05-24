package com.geteasyqa.EasyQA;

import com.geteasyqa.EasyQA.Authorization.SignIn;
import com.geteasyqa.EasyQA.Issues.CreateIssue;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;

/**
 * Created by yanagusti on 5/24/17.
 */
public class EasyQACreateIssueOnFailure extends Builder implements SimpleBuildStep {

    private final String issueName;

    @DataBoundConstructor
    public EasyQACreateIssueOnFailure(String name) {
        this.issueName = name;
    }


    @Override
    public void perform(Run<?,?> build, FilePath workspace, Launcher launcher, TaskListener listener) throws IOException {
        // This is where you 'build' the project.
        // Since this is a dummy, we just say 'hello world' and call that a build.

        // This also shows how you can consult the global configuration of the builder
        EasyQAPluginBuilder easyQAPluginBuilder = new EasyQAPluginBuilder();
        String url = easyQAPluginBuilder.getEasyQAURL();
        String token = easyQAPluginBuilder.getEasyQAProjectToken();
        String email = easyQAPluginBuilder.getEasyQAEmail();
        String password = easyQAPluginBuilder.getEasyQAPassword();

        SignIn signIn = new SignIn(url+"1");
        signIn.signIn(token, email, password);
        String auth_token = signIn.getAuth_token_value();
        CreateIssue createIssue = new CreateIssue(url);
        if (auth_token != null){
            createIssue.createIssue(token, auth_token, "fromJenkins");
        listener.getLogger().println("Issue was sent "+issueName);
         }
        else {
            listener.getLogger().println("Sorry! " + issueName);
        }
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }


    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {




        public DescriptorImpl() {
            load();
        }


//        public FormValidation doCheckName(@QueryParameter String value)
//                throws IOException, ServletException {
//            if (value.length() == 0)
//                return FormValidation.error("Please set a name");
//            if (value.length() < 4)
//                return FormValidation.warning("Isn't the name too short?");
//            return FormValidation.ok();
//        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }


        public String getDisplayName() {
            return "Send issue to EasyQA";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
//            url = formData.getString("easyqa.url");
//            token = formData.getString("easyqa.token");
//            email = formData.getString("easyqa.email");
//            password = formData.getString("easyqa.password");
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req,formData);
        }


//        public String getURL() {
//            return url;
//        }
//        public String getProjectToken() {
//            return token;
//        }
//        public String getEmail() {
//            return email;
//        }
//        public String getPassword() {
//            return password;
//        }

    }
}
