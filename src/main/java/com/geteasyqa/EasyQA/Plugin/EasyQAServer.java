package com.geteasyqa.EasyQA.Plugin;

import com.geteasyqa.EasyQA.Authorization.SignIn;
import com.geteasyqa.EasyQA.Issues.CreateIssue;
import com.geteasyqa.EasyQA.Issues.GetIssues;
import org.jvnet.hudson.test.Issue;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains methods for communication with a YouTrack server using the REST API for version 4 of YouTrack.
 */
public class EasyQAServer {

    private static final Logger LOGGER = Logger.getLogger(EasyQAServer.class.getName());

    private final String serverUrl;


    public EasyQAServer(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    private static String getErrorMessage(InputStream errorStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream, StandardCharsets.UTF_8))) {
            String l;
            while ((l = bufferedReader.readLine()) != null) {
                stringBuilder.append(l).append("\n");
            }
        }
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            ErrorHandler errorHandler = new ErrorHandler();
            saxParser.parse(new InputSource(new StringReader(stringBuilder.toString())), errorHandler);
            return errorHandler.errorMessage;
        } catch (ParserConfigurationException | SAXException e) {
            LOGGER.log(Level.WARNING, "Could not parse error response", e);
        }

        // If we couldn't parse the body, return the raw response.
        return stringBuilder.toString();
    }

    public Integer createIssue(String project_token, String auth_token, String title, String description, File attachment) throws IOException {
        ArrayList<File> files = new ArrayList<>();
        files.add(attachment);

        return new CreateIssue(serverUrl).createIssueWithAttachments(project_token, auth_token, title, files, "description", description);
    }

//    public List<Group> getGroups(User user) {
//        List<Group> groups = new ArrayList<Group>();
//        try {
//            URL url = new URL(serverUrl + "/rest/admin/group");
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//
//            for (String cookie : user.getCookies()) {
//
//                urlConnection.setRequestProperty("Cookie", cookie);
//            }
//
//            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//            try {
//                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    SAXParser saxParser = saxParserFactory.newSAXParser();
//                    Group.GroupListHandler dh = new Group.GroupListHandler();
//                    saxParser.parse(urlConnection.getInputStream(), dh);
//                    return dh.getGroups();
//                }
//            } catch (ParserConfigurationException e) {
//                LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//            } catch (SAXException e) {
//                LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//            }
//        } catch (MalformedURLException e) {
//            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//        } catch (IOException e) {
//            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//        }
//        return groups;
//    }

//    public StateBundle getStateBundleWithName(User user, String stateBundleName) {
//        try {
//            String stateBundleUrl = serverUrl + "/rest/admin/customfield/stateBundle/" + stateBundleName;
//            URL url = new URL(stateBundleUrl);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//
//            for (String cookie : user.getCookies()) {
//
//                urlConnection.setRequestProperty("Cookie", cookie);
//            }
//
//            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//            try {
//                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    SAXParser saxParser = saxParserFactory.newSAXParser();
//                    StateBundle stateBundle = new StateBundle(stateBundleName, stateBundleUrl);
//                    StateBundle.StateBundleHandler dh = new StateBundle.StateBundleHandler(stateBundle);
//                    saxParser.parse(urlConnection.getInputStream(), dh);
//                    return stateBundle;
//
//                }
//            } catch (ParserConfigurationException e) {
//                LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//            } catch (SAXException e) {
//                LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//            }
//        } catch (MalformedURLException e) {
//            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//        } catch (IOException e) {
//            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//        }
//        return null;
//    }
//
//    public StateBundle getStateBundleForField(User user, String fieldName) {
//        try {
//            String fieldUrl = serverUrl + "/rest/admin/customfield/field/" + fieldName;
//            URL url = new URL(fieldUrl);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//
//            for (String cookie : user.getCookies()) {
//
//                urlConnection.setRequestProperty("Cookie", cookie);
//            }
//
//            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//            try {
//                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    SAXParser saxParser = saxParserFactory.newSAXParser();
//                    Field.FieldHandler dh = new Field.FieldHandler(fieldName, fieldUrl);
//                    saxParser.parse(urlConnection.getInputStream(), dh);
//                    Field field = dh.getField();
//
//                    if (field.getType().equals("state[1]")) {
//                        return getStateBundleWithName(user, field.getDefaultBundle());
//                    } else {
//                        return null;
//                    }
//                }
//            } catch (ParserConfigurationException e) {
//                LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//            } catch (SAXException e) {
//                LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//            }
//        } catch (MalformedURLException e) {
//            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//        } catch (IOException e) {
//            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//        }
//        return null;
//    }
//
//    public String getBuildBundleNameForField(User user, String projectId, String fieldName) {
//        try {
//
//            String encodedProjectId = URLEncoder.encode(projectId, "ISO-8859-1").replace("+", "%20");
//            String encodedFieldName = URLEncoder.encode(fieldName, "ISO-8859-1").replace("+", "%20");
//
//            String fieldUrl = serverUrl + "/rest/admin/project/" + encodedProjectId + "/customfield/" + encodedFieldName;
//            URL url = new URL(fieldUrl);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//
//            for (String cookie : user.getCookies()) {
//
//                urlConnection.setRequestProperty("Cookie", cookie);
//            }
//
//            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//            try {
//                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    SAXParser saxParser = saxParserFactory.newSAXParser();
//                    Field.FieldHandler dh = new Field.FieldHandler(fieldName, fieldUrl);
//                    saxParser.parse(urlConnection.getInputStream(), dh);
//                    Field field = dh.getField();
//
//                    if (field.getType().equals("build[1]")) {
//                        return field.getDefaultBundle();
//                    } else {
//                        return null;
//                    }
//                }
//            } catch (ParserConfigurationException e) {
//                LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//            } catch (SAXException e) {
//                LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//            }
//        } catch (MalformedURLException e) {
//            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//        } catch (IOException e) {
//            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//        }
//        return null;
//    }
//
//    public List<Field> getFields(User user) {
//        List<Field> fields = new ArrayList<Field>();
//        try {
//            URL url = new URL(serverUrl + "/rest/admin/customfield/field/");
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//
//            for (String cookie : user.getCookies()) {
//
//                urlConnection.setRequestProperty("Cookie", cookie);
//            }
//
//            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//            try {
//                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    SAXParser saxParser = saxParserFactory.newSAXParser();
//                    Field.FieldListHandler dh = new Field.FieldListHandler();
//                    saxParser.parse(urlConnection.getInputStream(), dh);
//                    return dh.getFields();
//                }
//            } catch (ParserConfigurationException e) {
//                LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//            } catch (SAXException e) {
//                LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//            }
//        } catch (MalformedURLException e) {
//            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//        } catch (IOException e) {
//            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
//        }
//        return fields;
//    }


    public void getProjects(User user) {
        try {
            URL url = new URL(serverUrl + "/rest/project/all");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


            for (String cookie : user.getCookies()) {

                urlConnection.setRequestProperty("Cookie", cookie);
            }



        } catch (MalformedURLException e) {
            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not get YouTrack Projects", e);
        }


    }


//    public Command comment(String siteName, User user, Issue issue, String comment, String group, boolean silent) {
//        Command command = new Command();
//        command.setSiteName(siteName);
//        command.setIssueId(issue.getId());
//        command.setComment(comment);
//        command.setDate(new Date());
//        command.setGroup(group);
//        command.setSilent(silent);
//        if (user == null || !user.isLoggedIn()) {
//            command.setStatus(Command.Status.NOT_LOGGED_IN);
//        } else {
//            command.setStatus(Command.Status.FAILED);
//        }
//        if (user != null) {
//            command.setUsername(user.getUsername());
//        }
//
//
//        try {
//            URL url = new URL(serverUrl + "/rest/issue/" + issue.getId() + "/execute");
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setDoOutput(true);
//            urlConnection.setDoInput(true);
//
//            if (user != null) {
//                for (String cookie : user.getCookies()) {
//                    urlConnection.setRequestProperty("Cookie", cookie);
//                }
//            }
//
//            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8)) {
//                outputStreamWriter.write("comment=" + URLEncoder.encode(comment, "UTF-8"));
//                if (group != null && !group.equals("")) {
//                    outputStreamWriter.write("&group=" + group);
//                }
//                if (silent) {
//                    outputStreamWriter.write("&disableNotifications=" + true);
//                }
//                outputStreamWriter.flush();
//            };
//
//            int responseCode = urlConnection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                command.setStatus(Command.Status.OK);
//                return command;
//            } else {
//                command.setStatus(Command.Status.FAILED);
//                command.setResponse(getErrorMessage(urlConnection.getErrorStream()));
//            }
//        } catch (IOException e) {
//            LOGGER.log(Level.WARNING, "Could not comment", e);
//            command.setResponse(e.getMessage());
//        }
//        return command;
//    }


//    public Command applyCommand(String siteName, User user, Issue issue, String command, String comment, String group, User runAs, boolean notify) {
//        Command cmd = new Command();
//        cmd.setCommand(command);
//        cmd.setSilent(!notify);
//        cmd.setIssueId(issue.getId());
//        cmd.setSiteName(siteName);
//        cmd.setDate(new Date());
//        cmd.setStatus(Command.Status.FAILED);
//        cmd.setComment(comment);
//
//        if (user == null || !user.isLoggedIn()) {
//            cmd.setStatus(Command.Status.NOT_LOGGED_IN);
//            return cmd;
//        }
//        cmd.setUsername(user.getUsername());
//        try {
//
//
//            URL url = new URL(serverUrl + "/rest/issue/" + issue.getId() + "/execute");
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setDoOutput(true);
//            urlConnection.setDoInput(true);
//
//            for (String cookie : user.getCookies()) {
//                urlConnection.setRequestProperty("Cookie", cookie);
//            }
//
//            try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8)) {
//                String str = "command=" + URLEncoder.encode(command, "UTF-8");
//                if (comment != null) {
//                    str += "&comment=" + URLEncoder.encode(comment, "UTF-8");
//                }
//                if (runAs != null) {
//                    str += "&runAs=" + runAs.getUsername();
//                }
//                if (!notify) {
//                    str += "&disableNotifications=true";
//                }
//                if (group != null) {
//                    str += "&group=" + URLEncoder.encode(group, "UTF-8");
//                }
//                outputStreamWriter.write(str);
//                outputStreamWriter.flush();
//            }
//
//            int responseCode = urlConnection.getResponseCode();
//
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                cmd.setStatus(Command.Status.OK);
//                return cmd;
//            }
//
//            cmd.setStatus(Command.Status.FAILED);
//            cmd.setResponse(getErrorMessage(urlConnection.getErrorStream()));
//            LOGGER.log(Level.WARNING, "Could not apply command: " + cmd.getResponse());
//        } catch (IOException e) {
//            cmd.setResponse(e.getMessage());
//            LOGGER.log(Level.WARNING, "Could not apply command", e);
//        }
//        return cmd;
//    }


    public User getUserByEmail(User user, String email) {
        try {
            URL url = new URL(serverUrl + "/rest/admin/user?q=" + email);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            for (String cookie : user.getCookies()) {
                urlConnection.setRequestProperty("Cookie", cookie);
            }

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                User.UserRefHandler dh = new User.UserRefHandler();
                saxParser.parse(urlConnection.getInputStream(), dh);
                return dh.getUser();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not get user", e);
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.WARNING, "Could not get user", e);
        } catch (SAXException e) {
            LOGGER.log(Level.WARNING, "Could not get user", e);
        }
        return null;
    }


    public User login(String project_token, String email, String password) {

        try {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);

            SignIn signIn = new SignIn(serverUrl);
            signIn.signIn(project_token, email, password);
            user.setAuth_token(signIn.getAuth_token_value());


            if (user.getAuth_token()!=null) {

                user.setLoggedIn(true);
                return user;
            } else {

                return user;
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not login", e);
        }
        return null;
    }

//    public Command addBuildToBundle(String siteName, User user, String bundleName, String buildName) {
//        Command cmd = new Command();
//        cmd.setCommand("[Add '" + buildName + "' to " + " '" + bundleName + "']");
//        cmd.setDate(new Date());
//        cmd.setSiteName(siteName);
//
//        if (user == null || !user.isLoggedIn()) {
//            cmd.setStatus(Command.Status.NOT_LOGGED_IN);
//            return cmd;
//        } else {
//            cmd.setStatus(Command.Status.FAILED);
//        }
//        user.setUsername(user.getUsername());
//        try {
//
//            String encode = URLEncoder.encode(bundleName, "ISO-8859-1").replace("+", "%20");
//            String encode1 = URLEncoder.encode(buildName, "ISO-8859-1").replace("+", "%20");
//            URL url = new URL(serverUrl + "/rest/admin/customfield/buildBundle/" + encode + "/" + encode1);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("PUT");
//            for (String cookie : user.getCookies()) {
//                urlConnection.setRequestProperty("Cookie", cookie);
//            }
//            urlConnection.setDoOutput(true);
//            urlConnection.setDoInput(true);
//            try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8)) {
//                outputStreamWriter.flush();
//            };
//
//
//            int responseCode = urlConnection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_CREATED) {
//                cmd.setStatus(Command.Status.OK);
//                return cmd;
//            }
//
//            cmd.setStatus(Command.Status.FAILED);
//            cmd.setResponse(getErrorMessage(urlConnection.getErrorStream()));
//        } catch (IOException e) {
//            cmd.setResponse(e.getMessage());
//            LOGGER.log(Level.WARNING, "Could not add to bundle", e);
//        }
//        return cmd;
//    }


    public Issue getIssue(User user, String issueId, String project_token) {
        try {

            GetIssues getIssues = new GetIssues(serverUrl);
            getIssues.getIssueByProjectID(project_token, user.getAuth_token(), issueId);

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not get issue", e);
        }
        return null;
    }

//    public String[] getVersion() {
//        try {
//            URL url = new URL(serverUrl + "/rest/workflow/version");
//            try {
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//                    SAXParser saxParser = saxParserFactory.newSAXParser();
//                    VersionHandler versionHandler = new VersionHandler();
//                    saxParser.parse(urlConnection.getInputStream(), versionHandler);
//                    return versionHandler.version.split("\\.");
//                }
//            } catch (IOException | ParserConfigurationException | SAXException e) {
//                LOGGER.log(Level.WARNING, "Could not get version", e);
//            }
//        } catch (MalformedURLException e) {
//            LOGGER.log(Level.WARNING, "Wrong url", e);
//        }
//        return null;
//    }

//    public List<BuildBundle> getBuildBundles(User user) {
//        try {
//            URL url = new URL(serverUrl + "/rest/admin/customfield/buildBundle");
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            for (String cookie : user.getCookies()) {
//                urlConnection.setRequestProperty("Cookie", cookie);
//            }
//
//
//            int responseCode = urlConnection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                try {
//                    SAXParserFactory factory = SAXParserFactory.newInstance();
//                    SAXParser saxParser = factory.newSAXParser();
//                    BuildBundle.Handler issueHandler = new BuildBundle.Handler();
//                    saxParser.parse(urlConnection.getInputStream(), issueHandler);
//                    return issueHandler.getBundles();
//                } catch (ParserConfigurationException | SAXException e) {
//                    LOGGER.log(Level.WARNING, "Could not get issue", e);
//                }
//            }
//
//        } catch (IOException e) {
//            LOGGER.log(Level.WARNING, "Could not get issue", e);
//        }
//        return null;
//    }

//    public List<Issue> search(User user, String searchQuery) {
//        try {
//            URL url = new URL(serverUrl + "/rest/issue?filter=" + URLEncoder.encode(searchQuery, "UTF-8") + "&max=" + Integer.MAX_VALUE);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            for (String cookie : user.getCookies()) {
//                urlConnection.setRequestProperty("Cookie", cookie);
//            }
//
//
//            int responseCode = urlConnection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                try {
//                    SAXParserFactory factory = SAXParserFactory.newInstance();
//                    SAXParser saxParser = factory.newSAXParser();
//                    Issue.IssueSearchHandler issueSearchHandler = new Issue.IssueSearchHandler();
//                    saxParser.parse(urlConnection.getInputStream(), issueSearchHandler);
//                    return issueSearchHandler.getIssueList();
//                } catch (ParserConfigurationException | SAXException e) {
//                    LOGGER.log(Level.WARNING, "Could not find issues", e);
//                }
//            }
//
//        } catch (IOException e) {
//            LOGGER.log(Level.WARNING, "Could not find issues", e);
//        }
//        return null;
//    }
//
//    public List<Suggestion> searchSuggestions(User user, String current) {
//        try {
//            URL url = new URL(serverUrl + "/rest/issue/intellisense?filter=" + URLEncoder.encode(current, "UTF-8"));
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            for (String cookie : user.getCookies()) {
//                urlConnection.setRequestProperty("Cookie", cookie);
//            }
//
//
//            int responseCode = urlConnection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                try {
//                    SAXParserFactory factory = SAXParserFactory.newInstance();
//                    SAXParser saxParser = factory.newSAXParser();
//                    Issue.IssueSearchSuggestionHandler issueSearchHandler = new Issue.IssueSearchSuggestionHandler();
//                    saxParser.parse(urlConnection.getInputStream(), issueSearchHandler);
//                    return issueSearchHandler.getSuggestions();
//                } catch (ParserConfigurationException | SAXException e) {
//                    LOGGER.log(Level.WARNING, "Could not find issues", e);
//                }
//            }
//
//        } catch (IOException e) {
//            LOGGER.log(Level.WARNING, "Could not find issues", e);
//        }
//        return new ArrayList<>();
//    }
//
//    private Command createIssuePOST(String siteName, User user, String project, String title, String description, String command, File attachment) {
//        Command cmd = new Command();
//        cmd.setCommand("[Create issue]");
//        cmd.setDate(new Date());
//        cmd.setSiteName(siteName);
//
//        if (user == null || !user.isLoggedIn()) {
//            cmd.setStatus(Command.Status.NOT_LOGGED_IN);
//            return null;
//        }
//
//        cmd.setStatus(Command.Status.FAILED);
//        try {
//            String params = "project=" + URLEncoder.encode(project, "UTF-8") + "&summary=" + URLEncoder.encode(title, "UTF-8") + "&description=" + URLEncoder.encode(description, "UTF-8");
//
//            // Against documentation. This call is supposed to be PUT, but only POST is working.
//            PostMethod postMethod = new PostMethod(serverUrl + "/rest/issue");
//
//            for (String cookie : user.getCookies()) {
//                postMethod.addRequestHeader("Cookie", cookie);
//            }
//
//            List<Part> parts = new ArrayList<>();
//            parts.add(new StringPart("project", project, "UTF-8"));
//            parts.add(new StringPart("summary", title, "UTF-8"));
//            parts.add(new StringPart("description", description, "UTF-8"));
//            if (attachment != null) {
//                parts.add(new FilePart("attachment", attachment));
//            }
//            Part[] partsArray = {};
//            Part[] array = parts.toArray(partsArray);
//            postMethod.setRequestEntity(new MultipartRequestEntity(array, new HttpMethodParams()));
//
//            HttpClient httpClient = new HttpClient();
//            int responseCode = httpClient.executeMethod(postMethod);
//            // Because we're varying in the POST vs. PUT call, check for a couple possible
//            // success responses, though currently I'm only ever seeing 200 returned.
//            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
//                StringBuilder stringBuilder = new StringBuilder();
//                try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), StandardCharsets.UTF_8))) {
//                    for (String l = null; (l = bufferedReader.readLine()) != null; ) {
//                        stringBuilder.append(l).append("\n");
//                    }
//                };
//
//                try {
//                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//                    SAXParser saxParser = saxParserFactory.newSAXParser();
//                    CreateIssueHandler handler = new CreateIssueHandler();
//                    saxParser.parse(new InputSource(new StringReader(stringBuilder.toString())), handler);
//                    String issueId = handler.issueId;
//
//                    LOGGER.log(Level.INFO, "Created issue " + issueId);
//
//                    if (issueId != null) {
//                        Issue issue = new Issue(issueId);
//                        if (StringUtils.isNotBlank(command)) {
//                            applyCommand(siteName, user, issue, command, "", null, null, false);
//                            cmd.setCommand(command);
//                        }
//                        cmd.setIssueId(issueId);
//                    }
//                } catch (RuntimeException e) {
//                    cmd.setCommand("[Unable to apply command]");
//                } catch (Exception e) {
//                    cmd.setCommand("[Unable to apply command]");
//                }
//
//                cmd.setStatus(Command.Status.OK);
//
//                return cmd;
//            }
//
//            cmd.setResponse(getErrorMessage(postMethod.getResponseBodyAsStream()));
//            LOGGER.log(Level.WARNING, "Did not create issue: " + cmd.getResponse());
//        } catch (IOException e) {
//            cmd.setResponse(e.getMessage());
//            LOGGER.log(Level.WARNING, "Did not create issue", e);
//        }
//        return cmd;
//    }
//
//    private static class VersionHandler extends DefaultHandler {
//        boolean inVersion = false;
//        private StringBuilder stringBuilder = new StringBuilder();
//        private String version;
//
//        @Override
//        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//            super.startElement(uri, localName, qName, attributes);
//            if (qName.equals("version")) {
//                inVersion = true;
//            }
//        }
//
//        @Override
//        public void characters(char[] ch, int start, int length) throws SAXException {
//            super.characters(ch, start, length);
//            if (inVersion) {
//                stringBuilder.append(ch, start, length);
//            }
//        }
//
//        @Override
//        public void endElement(String uri, String localName, String qName) throws SAXException {
//            if (qName.equals("version")) {
//                inVersion = false;
//                version = stringBuilder.toString();
//            }
//            super.endElement(uri, localName, qName);
//        }
//
//
//    }

    private static class ErrorHandler extends DefaultHandler {
        private StringBuilder stringBuilder = new StringBuilder();
        private boolean inError;
        private String errorMessage;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (qName.equals("error")) {
                inError = true;
                stringBuilder.setLength(0);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (qName.equals("error")) {
                errorMessage = stringBuilder.toString();
                inError = false;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            if (inError) {
                stringBuilder.append(ch, start, length);
            }
        }
    }

    private static class CreateIssueHandler extends DefaultHandler {
//        public String issueId;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//            if (qName.equals("issue")) {
//                for (int i = 0; i < attributes.getLength(); ++i) {
//                    if (attributes.getQName(i).equals("id")) {
//                        issueId = attributes.getValue(i);
//                        break;
//                    }
//                }
//            }
        }
    }
}
