//package com.geteasyqa.EasyQA;
//
//import com.geteasyqa.EasyQA.Authorization.SignIn;
//import hudson.Extension;
//import hudson.Launcher;
//import hudson.model.AbstractBuild;
//import hudson.model.AbstractProject;
//import hudson.model.BuildListener;
//import hudson.model.Result;
//import hudson.tasks.BuildStepDescriptor;
//import hudson.tasks.BuildStepMonitor;
//import hudson.tasks.Notifier;
//import hudson.tasks.Publisher;
//import org.kohsuke.stapler.DataBoundConstructor;
//
//import java.io.IOException;
//
///**
// * Created by yanagusti on 5/25/17.
// */
//public class CreateIssue extends Notifier {
//
//    private String testDescription;
//
//    @DataBoundConstructor
//    public CreateIssue(String testDescription) {
//        this.testDescription = testDescription;
//    }
//
//    @Deprecated
//    public CreateIssue() {
//        this(null);
//    }
//
//    public String getTestDescription() {
//        return testDescription;
//    }
//
//    @Override
//    public BuildStepDescriptor<Publisher> getDescriptor() {
//        return DESCRIPTOR;
//    }
//
//    @Extension
//    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();
//
//    public BuildStepMonitor getRequiredMonitorService() {
//        return BuildStepMonitor.BUILD;
//    }
//
//
//    @Override
//    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws IOException {
//        Result currentBuildResult = build.getResult();
//
//        EasyQAPluginBuilder easyQAPluginBuilder = new EasyQAPluginBuilder();
//        String url = easyQAPluginBuilder.getEasyQAURL();
//        String token = easyQAPluginBuilder.getEasyQAProjectToken();
//        String email = easyQAPluginBuilder.getEasyQAEmail();
//        String password = easyQAPluginBuilder.getEasyQAPassword();
//
//
//        SignIn signIn = new SignIn(url);
//        signIn.signIn(token, email, password);
//        String auth_token = signIn.getAuth_token_value();
//        com.geteasyqa.EasyQA.Issues.CreateIssue createIssue = new com.geteasyqa.EasyQA.Issues.CreateIssue(url);
//        if (auth_token != null) {
//            Integer id = createIssue.createIssue(token, auth_token, "before cheking");
//            listener.getLogger().println("Issue was sent " + id);
//
//            if (currentBuildResult == Result.FAILURE) {
//
//                id = createIssue.createIssue(token, auth_token, "failure");
//                listener.getLogger().println("Issue was sent " + id);
//            } else if (currentBuildResult == Result.SUCCESS) {
//                id = createIssue.createIssue(token, auth_token, "success");
//                listener.getLogger().println("Issue was sent " + id);
//
//            } else {
//                listener.getLogger().println("Sorry! ");
//            }
//
//        }else {
//            listener.getLogger().println("Pleas, authorize ");
//        }
//
//
//
//        return true;
//    }
//
//    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
//
//        public DescriptorImpl() {
//            super(CreateIssue.class);
//        }
//
//        @Override
//        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
//            return true;
//        }
//
//        @Override
//        public String getDisplayName() {
//            return "test";
//        }
//    }
//
//
//
//
//
//
//}
