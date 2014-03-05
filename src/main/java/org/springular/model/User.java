package org.springular.model;

import org.springular.ui.InputType;
import org.springular.ui.annotation.UiForm;
import org.springular.ui.annotation.UiFormField;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * User entity
 */
@UiForm(filterApiUrl = "/api/users", addApiUrl = "/api/create-user", editApiUrl = "/api/update-user")
@Entity
@Table(name="USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @UiFormField(label = "First Name", required = true)
    @Column(nullable = false)
    private String firstName;

    @UiFormField(label = "Last Name", required = true)
    @Column(nullable = false)
    private String lastName;

    @UiFormField(label = "User ID", required = true)
    @Column(unique = true, length = 8)
    private String userId;

    @UiFormField(label = "Email", required = true)
    @Column(unique = true, length = 50)
    private String email;

    @UiFormField(label = "Phone", required = true)
    @Column(unique = true, length = 12)// TODO: add support for ui-utils mask = "(999)999-9999")
    private String phone;

    @UiFormField(label = "Roles", editType = InputType.multiSelect, optionsUrl = "/api/roles", required = true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CFG_USER2ROLE", joinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "CATEGORY_ID", nullable = false, updatable = false) })
    private Set<Role> roles;

    @UiFormField(label = "Active", editType = InputType.checkbox, filterType = InputType.select, optionNames = {"Yes", "No"}, optionValues = {"true", "false"})
    private Boolean active;

    @UiFormField(label = "Update Date", editType = InputType.none, filterType = InputType.dateRange)
    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIMESTAMP")
    private Date updateDate;

    // used to filter the 'updateDate' as date-range
    @Transient private Date updateDate_from;
    @Transient private Date updateDate_to;

    @Version
    private Long RecordVersionNumber;

    // map of errors for form validation
    @Transient
    private Map<String,String> errors;

    public User(){
        super();
        errors = new HashMap<>();
    }

    public User(String fName, String lName,String cmtId,Boolean status, Set<Role> rls) {
        super();
        firstName = fName;
        lastName = lName;
        userId = cmtId;
        active = status;
        roles = rls;
        errors =new HashMap<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        firstName = fName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lName) {
        lastName = lName;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

    public Set<Role> getRoles() { return roles; }

    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public Date getUpdateDate(){ return updateDate; }

    public Date getUpdateDate_to() { return updateDate_to; }

    public void setUpdateDate_to(Date updateDate_to) { this.updateDate_to = updateDate_to; }

    public Date getUpdateDate_from() { return updateDate_from; }

    public void setUpdateDate_from(Date updateDate_from) { this.updateDate_from = updateDate_from; }

    public Long getRecordVersionNumber() {
        return RecordVersionNumber;
    }

    public void setRecordVersionNumber(Long recordVersionNumber) {
        RecordVersionNumber = recordVersionNumber;
    }

    public Map<String,String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String,String> errList) {
        errors = errList;
    }
}
