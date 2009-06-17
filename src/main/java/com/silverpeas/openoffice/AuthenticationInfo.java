package com.silverpeas.openoffice;

/**
 * Authentication information.
 * 
 * @author Ludovic Bertin
 *
 */
public class AuthenticationInfo {

  String login = null;
  String password = null;

  /**
   * @param login
   * @param password
   */
  public AuthenticationInfo(String login, String password) {
    this.login = login;
    this.password = password;
  }

  /**
   * @return the login
   */
  public String getLogin() {
    return login;
  }

  /**
   * @param login the login to set
   */
  public void setLogin(String login) {
    this.login = login;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 47 * hash + (this.login != null ? this.login.hashCode() : 0);
    hash = 47 * hash + (this.password != null ? this.password.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AuthenticationInfo other = (AuthenticationInfo) obj;
    if ((this.login == null) ? (other.login != null)
        : !this.login.equals(other.login)) {
      return false;
    }
    if ((this.password == null) ? (other.password != null)
        : !this.password.equals(other.password)) {
      return false;
    }
    return true;
  }
}
