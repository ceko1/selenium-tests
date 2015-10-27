package com.wikia.webdriver.common.core.annotations;

import java.io.File;

import com.wikia.webdriver.common.core.XMLReader;
import com.wikia.webdriver.common.core.configuration.Configuration;

/**
 * Created by Ludwik on 2015-02-19. Added LANGUAGE2 Japanese. By P. Archbold on 2015-07-23
 */
public enum User {
  USER("ci.user.regular.username", "ci.user.regular.password", "ci.user.regular.access_token"), USER_2(
      "ci.user.regular2.username", "ci.user.regular2.password"), USER_3(
      "ci.user.regular3.username", "ci.user.regular3.password"), USER_5(
      "ci.user.regular5.username", "ci.user.regular5.password"), USER_9(
      "ci.user.regular9.username", "ci.user.regular9.password"), USER_12(
      "ci.user.regular12.username", "ci.user.regular12.password"), STAFF(
      "ci.user.wikiastaff2.username", "ci.user.wikiastaff2.password"), ANONYMOUS("anonymous",
      "anonymous"), REGULAR_USER_JAPAN("ci.user.language2.username", "ci.user.language2.password"), USER_GO_SEARCH_PREFERRED(
      "ci.user.goSearchPreferredUser.username", "ci.user.goSearchPreferredUser.password"), BLOCKED_USER(
      "ci.user.tooManyLoginAttempts.username", "ci.user.tooManyLoginAttempts.password");

  private final String userName;

  private final String password;
  private final String filePath = Configuration.getCredentialsFilePath();
  private String accessToken;

  User(String userNameKey, String passwordKey) {
    this.userName = XMLReader.getValue(new File(filePath), userNameKey);
    this.password = XMLReader.getValue(new File(filePath), passwordKey);
    this.accessToken = "";
  }

  User(String userNameKey, String passwordKey, String accessTokenKey) {
    this.userName = XMLReader.getValue(new File(filePath), userNameKey);
    this.password = XMLReader.getValue(new File(filePath), passwordKey);
    this.accessToken = XMLReader.getValue(new File(filePath), accessTokenKey);
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}
