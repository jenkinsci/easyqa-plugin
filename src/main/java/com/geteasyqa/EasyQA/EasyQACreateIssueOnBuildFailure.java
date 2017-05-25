package com.geteasyqa.EasyQA;

import com.geteasyqa.EasyQA.Issues.CreateIssue;
import com.geteasyqa.EasyQA.Plugin.EasyQAServer;
import com.geteasyqa.EasyQA.Plugin.EasyQASite;
import com.geteasyqa.EasyQA.Plugin.User;
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
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
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

    private String summary;
    private String description;
    private String threshold;

    private boolean attachBuildLog;

    @DataBoundConstructor
    public EasyQACreateIssueOnBuildFailure( String summary, String description, String threshold,  boolean attachBuildLog) {

        this.summary = summary;
        this.description = description;
        this.threshold = threshold;

        this.attachBuildLog = attachBuildLog;
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        EasyQASite easyQASite = getEasyQASite(build);
        if (easyQASite == null) {
            listener.getLogger().println("No EasyQA site configured");
            return true;
        }

        if (shouldCreateIssue(build)) {
            EasyQAServer server = getEasyQAServer(easyQASite);
            User user = server.login(easyQASite.getToken(), easyQASite.getEmail(), easyQASite.getPassword());
            if (user == null) {
                listener.getLogger().println("Could not login user to EasyQA");
                return true;
            }

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
            CreateIssue createIssue = new CreateIssue(easyQASite.getUrl());
            ArrayList<File> files = new ArrayList<>();
            files.add(buildLog);

            Integer id = createIssue.createIssueWithAttachments(easyQASite.getToken(), user.getAuth_token(), title
            , files, "description", description);

            listener.getLogger().println("Created new YouTrack issue " + id);
        }else {
            EasyQAServer server = getEasyQAServer(easyQASite);
            User user = server.login(easyQASite.getToken(), easyQASite.getEmail(), easyQASite.getPassword());
            if (user == null) {
                listener.getLogger().println("Could not login user to EasyQA");
                return true;
            }

            CreateIssue createIssue = new CreateIssue(easyQASite.getUrl());
            createIssue.createIssue(easyQASite.getToken(), user.getAuth_token(), "test123");
        }

        return true;

    }

    public String getAbsoluteUrl(AbstractBuild<?, ?> build) {
        return build.getAbsoluteUrl();
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    EasyQAServer getEasyQAServer(EasyQASite easyQASite) {
        return new EasyQAServer(easyQASite.getUrl());
    }

    EasyQASite getEasyQASite(AbstractBuild<?, ?> build) {
        return EasyQASite.get(build.getProject());
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
            return "test";
        }


        @Override
        public Publisher newInstance(final StaplerRequest req, final JSONObject formData) {
            return req.bindJSON(EasyQACreateIssueOnBuildFailure.class, formData);
        }


        public FormValidation doCheckProject(@QueryParameter String value) {
            return FormValidation.validateRequired(value);
        }
//
//        public AutoCompletionCandidates doAutoCompleteProject(@AncestorInPath AbstractProject project, @QueryParameter String value) {
//            return EasyQAPluginProperties.getProjects(project, value);
//        }

    }
}
