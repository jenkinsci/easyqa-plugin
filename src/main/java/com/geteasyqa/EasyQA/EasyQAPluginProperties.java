package com.geteasyqa.EasyQA;

import com.geteasyqa.EasyQA.Plugin.*;
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
import org.jenkinsci.plugins.scriptsecurity.sandbox.groovy.SecureGroovyScript;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.util.ArrayList;
import java.util.List;

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
     * If ping back comments is enabled.
     */
    @Getter @Setter private boolean commentsEnabled;
    /**
     * The text to use for ping back comments
     */
    @Getter @Setter private String commentText;
    /**
     * The text to use for ping back comments
     */
    @Getter @Setter private SecureGroovyScript commentTextSecure;
    /**
     * If executing commands is enabled.
     */
    @Getter @Setter private boolean commandsEnabled;
    /**
     * If the commands should be run as the vcs user.
     */
    @Getter @Setter private boolean runAsEnabled;

    /**
     * If ChangeLog annotations is enabled.
     */
    @Getter @Setter private boolean annotationsEnabled;

    /**
     * The name of the group comment links should be visible for.
     */
    @Getter @Setter private String linkVisibility;
    /**
     * Name of state field to check for weather an issue is selected.
     */
    @Getter @Setter private String stateFieldName;
    /**
     * Comma-separated list of values that are seen as fixed.
     */
    @Getter @Setter private String fixedValues;
    /**
     * Execute commands silently, i.e. do not notify watchers.
     */
    @Getter @Setter private boolean silentCommands;

    /**
     * Execute link comment silently.
     */
    @Getter @Setter private boolean silentLinks;
    /**
     * Limits the projects commands are applied to.
     */
    @Getter @Setter private String executeProjectLimits;
    /**
     * Tracks the processed commits.
     */
    @Getter @Setter private boolean trackCommits;
    /**
     * This is the default project for the integration, used for creating issues.
     */
    @Getter @Setter private String project;
    /**
     * Mapping from prefix words to corresponding commands.
     */
    @Getter @Setter private List<PrefixCommandPair> prefixCommandPairs;
    /**
     * How the build should fail if we can't apply the commands
     */
    @Getter @Setter private EasyQABuildFailureMode failureMode = EasyQABuildFailureMode.NONE;

    @Extension
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();


    @DataBoundConstructor
    public EasyQAPluginProperties(String siteName, boolean pluginEnabled, boolean commentsEnabled, boolean commandsEnabled, boolean runAsEnabled, boolean annotationsEnabled, String linkVisibility, String stateFieldName, String fixedValues, boolean silentCommands, boolean silentLinks, String executeProjectLimits, boolean trackCommits, String project, String commentText, EasyQABuildFailureMode failureMode, SecureGroovyScript commentTextSecure) {
        this.siteName = siteName;
        this.pluginEnabled = pluginEnabled;
        this.commentsEnabled = commentsEnabled;
        this.commandsEnabled = commandsEnabled;
        this.runAsEnabled = runAsEnabled;
        this.annotationsEnabled = annotationsEnabled;
        this.linkVisibility = linkVisibility;
        this.stateFieldName = stateFieldName;
        this.fixedValues = fixedValues;
        this.silentCommands = silentCommands;
        this.silentLinks = silentLinks;
        this.executeProjectLimits = executeProjectLimits;
        this.trackCommits = trackCommits;
        this.project = project;
        this.commentText = commentText;
        this.commentTextSecure = commentTextSecure;
        this.failureMode = failureMode;
        this.prefixCommandPairs = new ArrayList<PrefixCommandPair>();
    }



    @Override
    public JobPropertyDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    public void setPluginEnabled(boolean pluginEnabled) {
        this.pluginEnabled = pluginEnabled;
    }

    public void setPrefixCommandPairs(List<PrefixCommandPair> prefixCommandPairs) {
        this.prefixCommandPairs = prefixCommandPairs;
    }



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

                ypp.commentTextSecure.configuringWithKeyItem();

                ypp.setPluginEnabled(true);
                Object prefixCommandArray = pluginEnabled.get("prefixCommandPairs");
                List<PrefixCommandPair> commandPairs = req.bindJSONToList(PrefixCommandPair.class, prefixCommandArray);
                ypp.setPrefixCommandPairs(commandPairs);
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
            result.setCommentEnabled(commentsEnabled);
            result.setCommandsEnabled(commandsEnabled);
            result.setAnnotationsEnabled(annotationsEnabled);
            result.setRunAsEnabled(runAsEnabled);
            result.setLinkVisibility(linkVisibility);
            result.setStateFieldName(stateFieldName);
            result.setFixedValues(fixedValues);
            result.setSilentCommands(silentCommands);
            result.setSilentLinks(silentLinks);
            result.setExecuteProjectLimits(executeProjectLimits);
            result.setTrackCommits(trackCommits);
            result.setProject(project);
            result.setPrefixCommandPairs(prefixCommandPairs);
            result.setCommentText(commentText);
            result.setCommentTextSecure(commentTextSecure);
            result.setFailureMode(failureMode);
        }
        return result;
    }
}
