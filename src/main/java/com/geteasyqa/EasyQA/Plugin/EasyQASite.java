package com.geteasyqa.EasyQA.Plugin;

import com.geteasyqa.EasyQA.EasyQAPluginProperties;
import com.geteasyqa.EasyQA.PrefixCommandPair;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Result;
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;

public class EasyQASite {
    private String name;
    private String url;
    private String email;
    private String password;
    private String token;
    private transient boolean pluginEnabled;
    private transient boolean runAsEnabled;
    private transient boolean commandsEnabled;
    private transient boolean commentEnabled;
    private transient String commentText;
    private transient SecureGroovyScript commentTextSecure;
    private transient boolean annotationsEnabled;
    private transient String linkVisibility;
    private transient String stateFieldName;
    private transient String fixedValues;
    private transient boolean silentCommands;
    private transient boolean silentLinks;
    private transient String project;
    private transient String executeProjectLimits;
    private transient List<PrefixCommandPair> prefixCommandPairs;
    private boolean trackCommits;
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

    public String getStateFieldName() {
        return stateFieldName;
    }


    public void setPluginEnabled(boolean pluginEnabled) {
        this.pluginEnabled = pluginEnabled;
    }

    public void setCommentEnabled(boolean commentEnabled) {
        this.commentEnabled = commentEnabled;
    }

    public void setCommandsEnabled(boolean commandsEnabled) {
        this.commandsEnabled = commandsEnabled;
    }

    public void setAnnotationsEnabled(boolean annotationsEnabled) {
        this.annotationsEnabled = annotationsEnabled;
    }

    public void setRunAsEnabled(boolean runAsEnabled) {
        this.runAsEnabled = runAsEnabled;
    }

    public void setLinkVisibility(String linkVisibility) {
        this.linkVisibility = linkVisibility;
    }

    public void setStateFieldName(String stateFieldName) {
        this.stateFieldName = stateFieldName;
    }

    public void setFixedValues(String fixedValues) {
        this.fixedValues = fixedValues;
    }

    public void setSilentCommands(boolean silentCommands) {
        this.silentCommands = silentCommands;
    }

    public void setSilentLinks(boolean silentLinks) {
        this.silentLinks = silentLinks;
    }

    public void setExecuteProjectLimits(String executeProjectLimits) {
        this.executeProjectLimits = executeProjectLimits;
    }

    public void setTrackCommits(boolean trackCommits) {
        this.trackCommits = trackCommits;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setPrefixCommandPairs(List<PrefixCommandPair> prefixCommandPairs) {
        this.prefixCommandPairs = prefixCommandPairs;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setCommentTextSecure(SecureGroovyScript commentTextSecure) {
        this.commentTextSecure = commentTextSecure;
    }

    public void setFailureMode(EasyQABuildFailureMode failureMode) {
        this.failureMode = failureMode;
    }
}
