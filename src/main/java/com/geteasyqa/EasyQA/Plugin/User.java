package com.geteasyqa.EasyQA.Plugin;

import lombok.NoArgsConstructor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * This object represents a user.
 */
@NoArgsConstructor
public class User {

    private String email;
    private String password;
    private String auth_token;

    private boolean loggedIn;

    private transient List<String> cookies = new ArrayList<String>();

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public List<String> getCookies() {
        return cookies;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token=auth_token;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public static class UserRefHandler extends DefaultHandler {

        private User user;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (user == null && qName.equals("user")) {
                user = new User();
                user.email = attributes.getValue("login");
            }
        }

        public User getUser() {
            return user;
        }
    }
}
