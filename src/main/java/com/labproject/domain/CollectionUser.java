package com.labproject.domain;

import com.labproject.application.dto.input.CollectionUserDTO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class CollectionUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long collectionUserID;

    private String name;
    private String secondName;
    @Column(nullable = false, unique = true)
    private String email;

    private String password;
    private LocalDate dateOfBirth;
    private LocalDate dateOfRegistration;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @ManyToMany
    @JoinTable(name = "user_forum", joinColumns = @JoinColumn(name = "collectionUserID"), inverseJoinColumns = @JoinColumn(name = "forumID"))
    private List<Forum> subscribedForums = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_blocked_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_user_id")
    )
    private Set<CollectionUser> blockedUsers = new HashSet<>();


    public CollectionUser() {
    }

    public CollectionUser(CollectionUserDTO collectionUserDTO) {
        this.name = collectionUserDTO.getName();
        this.secondName = collectionUserDTO.getSecondName();
        this.email = collectionUserDTO.getEmail();
        this.password = collectionUserDTO.getPassword();
        this.dateOfBirth = collectionUserDTO.getDateOfBirth();
    }

    public CollectionUser(Long collectionUserID, String name, String secondName, String email, String password, LocalDate dateOfBirth) {
        this.collectionUserID = collectionUserID;
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public void subscribeToForum(Forum forum) {
        subscribedForums.add(forum);
    }

    public void unSubscribeToForum(Forum forum) {
        subscribedForums.remove(forum);
    }

    public void updateProfile(String password, String name, String secondName, String email) {
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return collectionUserID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public List<Forum> getSubscribedForums() {
        return subscribedForums;
    }

    public void setSubscribedForums(List<Forum> subscribedForums) {
        this.subscribedForums = subscribedForums;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role roles) {
        this.role = roles;
    }

    public Set<CollectionUser> getBlockedUsers() {
        return blockedUsers;
    }

    public void setBlockedUsers(Set<CollectionUser> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public void addBlockUser(CollectionUser user) {
        this.blockedUsers.add(user);
    }
}
