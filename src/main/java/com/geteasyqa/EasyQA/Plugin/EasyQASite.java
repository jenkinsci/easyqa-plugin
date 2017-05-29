package com.geteasyqa.EasyQA.Plugin;

import com.geteasyqa.EasyQA.EasyQAPluginProperties;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Result;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Created by yanagusti on 3/23/17.
 */
public class EasyQASite {
    private String name;
    private String url;
    private String email;
    private String password;
    private String token;
    private transient boolean pluginEnabled;
    private EasyQABuildFailureMode failureMode;


    @DataBoundConstructor
    public EasyQASite(String name, String email, String password, String url, String token) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.url = url;
        this.token = token;
    }

    public static EasyQASite get(AbstractProject<?, ?> project) {
        EasyQAPluginProperties ypp = project.getProperty(EasyQAPluginProperties.class);
        if (ypp != null) {
            EasyQASite site = ypp.getSite();
            if (site != null) {
                return site;
            }
        }
        EasyQASite[] sites = EasyQAPluginProperties.DESCRIPTOR.getSites();
        if (sites.length == 1) {
            return sites[0];
        }
        return null;
    }


    /**
     * Updates the result for build, depending on the failure mode.
     * @param build the build to update the result for.
     */
    public void failed(AbstractBuild<?, ?> build) {
        if (failureMode != null) {
            switch (failureMode) {
                case NONE:
                    break;
                case UNSTABLE:
                    build.setResult(Result.UNSTABLE);
                    break;
                case FAILURE:
                    build.setResult(Result.FAILURE);
                    break;
            }
        }
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getToken() {
        return token;
    }



    public void setPluginEnabled(boolean pluginEnabled) {
        this.pluginEnabled = pluginEnabled;
    }



    public void setFailureMode(EasyQABuildFailureMode failureMode) {
        this.failureMode = failureMode;
    }
}
