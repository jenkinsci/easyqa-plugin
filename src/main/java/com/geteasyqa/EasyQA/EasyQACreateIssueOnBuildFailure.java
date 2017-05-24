package com.geteasyqa.EasyQA;

import com.geteasyqa.EasyQA.Authorization.SignIn;
import com.geteasyqa.EasyQA.Issues.CreateIssue;
import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Post-build step to create an issue in Youtrack if the build fails.
 */
public class EasyQACreateIssueOnBuildFailure extends Notifier {
    public static final String FAILURE = "failure";
    public static final String FAILUREORUNSTABL = "failureOrUnstable";
    @Getter
    @Setter
    private String project;
    @Getter
    @Setter
    private String summary;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private String threshold;
    @Getter
    @Setter
    private String visibility;
    @Getter
    @Setter
    private String command;
    @Getter
    @Setter
    private boolean attachBuildLog;

    @DataBoundConstructor
    public EasyQACreateIssueOnBuildFailure(String project, String summary, String description, String threshold, String visibility, String command, boolean attachBuildLog) {
        this.project = project;
        this.summary = summary;
        this.description = description;
        this.threshold = threshold;
        this.visibility = visibility;
        this.command = command;
        this.attachBuildLog = attachBuildLog;
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {


        if (shouldCreateIssue(build)) {
            EasyQAPluginBuilder easyQAPluginBuilder = new EasyQAPluginBuilder();
            String url = easyQAPluginBuilder.getEasyQAURL();
            String token = easyQAPluginBuilder.getEasyQAProjectToken();
            String email = easyQAPluginBuilder.getEasyQAEmail();
            String password = easyQAPluginBuilder.getEasyQAPassword();

            SignIn signIn = new SignIn(url);
            signIn.signIn(token, email, password);
            String auth_token = signIn.getAuth_token_value();

            EnvVars environment = build.getEnvironment(listener);
            String title = environment.expand(this.summary);
            String description = environment.expand(this.description);

            if (title == null || "".equals(title)) {
                title = "Build failure in build " + build.getNumber();
            } else {
                title = environment.expand(title);
            }
            if (description == null || "".equals(description)) {
                description = getAbsoluteUrl(build);
            } else {
                description = environment.expand(description);
            }

            File buildLog = null;
            if (attachBuildLog) {
                buildLog = build.getLogFile();
            }

            ArrayList<File> files = new ArrayList<>();
            files.add(buildLog);
            CreateIssue createIssue = new CreateIssue(url);
            if (auth_token != null){
                Integer id = createIssue.createIssueWithAttachments(token, auth_token, title, files, "description", description);
                listener.getLogger().println("Issue was sent "+id);
            }
            else {
                listener.getLogger().println("Sorry! ");
            }

        }

        return true;

    }

    public String getAbsoluteUrl(AbstractBuild<?, ?> build) {
        return build.getAbsoluteUrl();
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }



    private boolean shouldCreateIssue(AbstractBuild<?, ?> build) {
        Result result = build.getResult();
        if (FAILURE.equals(threshold) && result.isBetterThan(Result.FAILURE)) {
            return false;
        } else if (FAILUREORUNSTABL.equals(threshold) && result.isBetterThan(Result.UNSTABLE)) {
            return false;
        }
        return true;
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Test send issue";
        }


        @Override
        public Publisher newInstance(final StaplerRequest req, final JSONObject formData) {
            return req.bindJSON(EasyQACreateIssueOnBuildFailure.class, formData);
        }


//        public FormValidation doCheckProject(@QueryParameter String value) {
//            return FormValidation.validateRequired(value);
//        }
//
//        public AutoCompletionCandidates doAutoCompleteProject(@AncestorInPath AbstractProject project, @QueryParameter String value) {
//            return YouTrackProjectProperty.getProjects(project, value);
//        }

    }
}
