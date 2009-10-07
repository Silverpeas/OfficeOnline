/**
 * Copyright (C) 2000 - 2009 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://repository.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
