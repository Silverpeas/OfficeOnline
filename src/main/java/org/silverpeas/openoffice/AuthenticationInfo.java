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
 * FLOSS exception.  You should have received a copy of the text describing
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

package org.silverpeas.openoffice;

import java.util.Arrays;

/**
 * Authentication information.
 * @author Ludovic Bertin
 */
public class AuthenticationInfo {

  private String login = null;
  private char[] password = new char[0];

  /**
   * @param login
   * @param pass
   */
  public AuthenticationInfo(String login, char[] pass) {
    this.login = login;
    this.password = Arrays.copyOf(pass, pass.length);
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
  public char[] getPassword() {
    return Arrays.copyOf(password, password.length);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 47 * hash + (this.login != null ? this.login.hashCode() : 0);
    hash = 47 * hash + Arrays.hashCode(this.password);
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
    if ((this.login == null) ? (other.login != null) : !this.login.equals(other.login)) {
      return false;
    }
    if (!Arrays.equals(this.password, other.password)) {
      return false;
    }
    return true;
  }

  
}
