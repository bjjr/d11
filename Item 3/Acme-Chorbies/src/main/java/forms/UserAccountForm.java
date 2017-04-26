
package forms;

import security.UserAccount;

public class UserAccountForm {

	private String	previousPassword;
	private String	password;
	private String	passwdConf;


	public UserAccountForm() {
		super();
	}

	public UserAccount getUserAccount() {
		UserAccount res;

		res = new UserAccount();

		res.setPassword(this.password);

		return res;
	}

	public String getPreviousPassword() {
		return this.previousPassword;
	}

	public void setPreviousPassword(final String previousPassword) {
		this.previousPassword = previousPassword;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getPasswdConf() {
		return this.passwdConf;
	}

	public void setPasswdConf(final String passwdConf) {
		this.passwdConf = passwdConf;
	}

}
