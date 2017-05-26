package com.geteasyqa.EasyQA;

import com.geteasyqa.EasyQA.Plugin.EasyQAServer;
import com.geteasyqa.EasyQA.Plugin.EasyQASite;
import com.geteasyqa.EasyQA.Plugin.User;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * Post-build step to create an issue in Youtrack if the build fails.
 */
public class EasyQACreateIssueOnBuildFailure extends Notifier {
    public static final String FAILURE = "failure";
    public static final String FAILUREORUNSTABL = "failureOrUnstable";
    @Getter
    @Setter
    private String threshold;
    @Getter
    @Setter
    private boolean attachBuildLog;

    @DataBoundConstructor
    public EasyQACreateIssueOnBuildFailure(String threshold, boolean attachBuildLog) {

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

        EasyQAServer server;
        User user;



        if (shouldCreateIssue(build)) {
            server = getEasyQAServer(easyQASite);
            user = server.login(easyQASite.getToken(), easyQASite.getEmail(), easyQASite.getPassword());
            if (user == null) {
                listener.getLogger().println("Could not login user to EasyQA");
                return true;
            }

            String title = "Failure in build " + build.getNumber();
            String description = "Build info\n: " + server.getErrorMessage(build.getLogInputStream());
            if (attachBuildLog) {

                File buildLog = stringToFile(build, description);
                listener.getLogger().println("Log file was created at " + buildLog.getAbsolutePath());
                ArrayList<File> files = new ArrayList<>();
                files.add(buildLog);
                Integer id = server.createIssueWithAttachment(easyQASite.getToken(), user.getAuth_token(), title, description, files);

                listener.getLogger().println("Created new EasyQA issue #" + id);

            } else {
                Integer id = server.createIssue(easyQASite.getToken(), user.getAuth_token(), title, description);

                listener.getLogger().println("Created new EasyQA issue #" + id);
            }

        }
        return true;

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

    private File stringToFile(AbstractBuild<?, ?> build, String text) throws IOException {
        File file = new File(build.getRootDir() + "/log1.txt");

        Path targetPath = Paths.get(file.getAbsolutePath());
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        Files.write(targetPath, bytes, StandardOpenOption.CREATE);

        return file;

    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Create an issue on Build failure on EasyQA";
        }


        @Override
        public Publisher newInstance(final StaplerRequest req, final JSONObject formData) {
            return req.bindJSON(EasyQACreateIssueOnBuildFailure.class, formData);
        }


        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) {


            save();
            return true;
        }

    }
}
