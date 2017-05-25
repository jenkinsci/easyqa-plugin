package com.geteasyqa.EasyQA;

import com.geteasyqa.EasyQA.Plugin.EasyQABuildFailureMode;
import com.geteasyqa.EasyQA.Plugin.EasyQAServer;
import com.geteasyqa.EasyQA.Plugin.EasyQASite;
import com.geteasyqa.EasyQA.Plugin.User;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.util.CopyOnWriteList;
import hudson.util.FormValidation;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Created by yanagusti on 5/25/17.
 */
public class EasyQAPluginProperties extends JobProperty<AbstractProject<?, ?>> {

    /**
     * The name of the site.
     */
    @Getter
    @Setter
    private String siteName;

    /**
     * If the YouTrack plugin is enabled.
     */
    @Getter @Setter private boolean pluginEnabled;

    /**
     * How the build should fail if we can't apply the commands
     */
    @Getter @Setter private EasyQABuildFailureMode failureMode = EasyQABuildFailureMode.NONE;

    @Extension
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();


    @DataBoundConstructor
    public EasyQAPluginProperties(String siteName, boolean pluginEnabled,  EasyQABuildFailureMode failureMode) {
        this.siteName = siteName;
        this.pluginEnabled = pluginEnabled;
        this.failureMode = failureMode;
    }



    @Override
    public JobPropertyDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    public void setPluginEnabled(boolean pluginEnabled) {
        this.pluginEnabled = pluginEnabled;
    }

//    public void setPrefixCommandPairs(List<PrefixCommandPair> prefixCommandPairs) {
//        this.prefixCommandPairs = prefixCommandPairs;
//    }



    public static final class DescriptorImpl extends JobPropertyDescriptor {
        private final CopyOnWriteList<EasyQASite> sites = new CopyOnWriteList();

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return AbstractProject.class.isAssignableFrom(jobType);
        }

        public DescriptorImpl() {
            super(EasyQAPluginProperties.class);
            load();

        }

        public void setSites(EasyQASite site) {
            sites.add(site);
        }

        public EasyQASite[] getSites() {
            return sites.toArray(new EasyQASite[0]);
        }

        @Override
        public JobProperty<?> newInstance(StaplerRequest req, JSONObject formData) throws FormException {

            JSONObject pluginEnabled = (JSONObject) formData.get("pluginEnabled");
            EasyQAPluginProperties ypp = null;
            if (pluginEnabled != null) {
                ypp = req.bindJSON(EasyQAPluginProperties.class, pluginEnabled);
                if (ypp.siteName == null) {
                    return null;
                }



                ypp.setPluginEnabled(true);



            }


            return ypp;
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) {

            sites.replaceBy(req.bindParametersToList(EasyQASite.class, "EasyQA."));
            save();
            return true;
        }


        @Override
        public String getDisplayName() {
            return "EasyQA Plugin";
        }



        @SuppressWarnings("UnusedDeclaration")
        public FormValidation doTestConnection(
                @QueryParameter("EasyQA.url") final String url,
                @QueryParameter("EasyQA.token") final String token,
                @QueryParameter("EasyQA.email") final String email,
                @QueryParameter("EasyQA.password") final String password) {

            EasyQAServer easyQAServer = new EasyQAServer(url);
            if (email != null && !email.equals("")) {
                User login = easyQAServer.login(token, email, password);
                if (login != null && login.isLoggedIn()) {
                    return FormValidation.ok("Connection ok!");
                } else {
                    return FormValidation.error("Could not login with given options");
                }
            } else {
                return FormValidation.error("Please, enter email");
            }
        }




    }

    public EasyQASite getSite() {
        EasyQASite result = null;
        EasyQASite[] sites = DESCRIPTOR.getSites();
        if (siteName == null && sites.length > 0) {
            result = sites[0];
        }

        for (EasyQASite site : sites) {
            if (site.getName() != null && site.getName().equals(siteName)) {
                result = site;
                break;
            }
        }
        if (result != null) {
            result.setPluginEnabled(pluginEnabled);
            result.setFailureMode(failureMode);
        }
        return result;
    }
}
