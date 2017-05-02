
package forms;

import java.util.ArrayList;
import java.util.List;

import security.Authority;
import security.UserAccount;
import domain.Manager;

public class ManagerForm {

	// Manager attributes

	private String		name;
	private String		surname;
	private String		email;
	private String		phone;
	private String		company;
	private String		vat;
	private UserAccount	userAccount;

	// Form attributes

	private String		passwdConfirmation;


	// Constructors

	public ManagerForm() {
		super();
	}

	/**
	 * ManagerForm constructor using a Manager object.
	 * 
	 * @param manager
	 *            The Manager object with data.
	 */

	public ManagerForm(final Manager manager) {
		this.name = manager.getName();
		this.surname = manager.getSurname();
		this.email = manager.getEmail();
		this.phone = manager.getPhone();
		this.company = manager.getCompany();
		this.vat = manager.getVat();
		this.userAccount = manager.getUserAccount();
		this.passwdConfirmation = "";
	}

	/**
	 * Retrieves the Manager object from the form.
	 * 
	 * @return A Manager object with data pending to be checked.
	 */

	public Manager getManager() {
		Manager res;
		Authority auth;
		final List<Authority> auths;

		res = new Manager();

		auth = new Authority();
		auths = new ArrayList<>();

		auth.setAuthority(Authority.MANAGER);
		auths.add(auth);

		res.setName(this.name);
		res.setSurname(this.surname);
		res.setEmail(this.email);
		res.setPhone(this.phone);
		res.setCompany(this.company);
		res.setVat(this.vat);
		this.userAccount.setAuthorities(auths);
		res.setUserAccount(this.userAccount);

		return res;
	}
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(final String company) {
		this.company = company;
	}

	public String getVat() {
		return this.vat;
	}

	public void setVat(final String vat) {
		this.vat = vat;
	}

	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public String getPasswdConfirmation() {
		return this.passwdConfirmation;
	}

	public void setPasswdConfirmation(final String passwdConfirmation) {
		this.passwdConfirmation = passwdConfirmation;
	}

}
