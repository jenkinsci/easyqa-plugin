//package com.geteasyqa.EasyQA;
//
//import hudson.Extension;
//import hudson.model.AbstractProject;
//import hudson.tasks.BuildStepDescriptor;
//import hudson.tasks.BuildStepMonitor;
//import hudson.tasks.Publisher;
//
///**
// * Created by yanagusti on 5/25/17.
// */
//public class EasyQACreateIssue extends Publisher {
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
//    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
//
//        @Override
//        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
//            return true;
//        }
//
//        @Override
//        public String getDisplayName() {
//            return "EasyQA Create Issue";
//        }
//    }
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
//        CreateIssue createIssue = new CreateIssue(url);
//        if (auth_token != null) {
//
//
//                if (currentBuildResult == Result.FAILURE) {
//
//                    Integer id = createIssue.createIssue(token, auth_token, "d123123");
//                    listener.getLogger().println("Issue was sent " + id);
//                } else if (currentBuildResult == Result.SUCCESS) {
//                    Integer id = createIssue.createIssue(token, auth_token, "testetstets");
//                    listener.getLogger().println("Issue was sent " + id);
//
//                } else {
//                    listener.getLogger().println("Sorry! ");
//                }
//
//        }else {
//                listener.getLogger().println("Pleas, authorize ");
//        }
//
//
//
//        return true;
//    }
//
//
//
//
//
//}
