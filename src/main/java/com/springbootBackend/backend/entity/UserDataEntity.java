package com.springbootBackend.backend.entity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_data",indexes = {
        @Index(name = "idx_email",columnList = "email"),
        @Index(name="idx_userName", columnList = "userName"),
        @Index(name = "idx_phoneNumber", columnList = "phone_number")
})
public class UserDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true)
    private String email;
    private String hashedEmail;
    @Column(unique = true, nullable = false)
    private String userName;
    @Column(nullable = false)
    private String hashedPassword;
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    private Gender gender;
    private Integer age;
    private LocalDate dateOfBirth;
    private String profilePicUrl;

    @Column(length = 10)
    private String countryCode;

    @Column(length = 20, unique = true)
    private String phoneNumber;
    public enum userStatus{
        ACTIVE,INACTIVE,BLOCKED,TEMP_BLOCKED
    };
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private userStatus currStatus=userStatus.ACTIVE;

    private LocalDateTime lastLogin;


    // blocked use case
    private int incorrectAttempts;
    private LocalDateTime incorrectAttemptTimeWindowStart;
    private int blockedCount;
    private LocalDateTime blockedStartTime;
    private LocalDateTime blockedEndTime;
    private long blockForMin;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedEmail() {
        return hashedEmail;
    }

    public void setHashedEmail(String hashedEmail) {
        this.hashedEmail = hashedEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public userStatus getCurrStatus() {
        return currStatus;
    }

    public void setCurrStatus(userStatus currStatus) {
        this.currStatus = currStatus;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setIncorrectAttempts(int incorrectAttempts){
        this.incorrectAttempts = incorrectAttempts;
    }
    public int getIncorrectAttempts(){
        return this.incorrectAttempts;
    }
    public void setIncorrectAttemptTimeWindowStart(LocalDateTime incorrectAttemptTimeWindowStart){
        this.incorrectAttemptTimeWindowStart = incorrectAttemptTimeWindowStart;
    }
    public LocalDateTime getIncorrectAttemptTimeWindowStart(){
        return this.incorrectAttemptTimeWindowStart;
    }
    public void setBlockedCount(int blockedCount){
        this.blockedCount = blockedCount;
    }
    public int getBlockedCount(){
        return this.blockedCount;
    }

    public void setBlockedStartTime(LocalDateTime blockedStartTime){
        this.blockedStartTime = blockedStartTime;
    }
    public LocalDateTime getBlockedStartTime(){
        return this.blockedStartTime;
    }

    public void setBlockForMin(long blockForMin){
          this.blockForMin = blockForMin;
    }
    public  long getBlockForMin(){
        return this.blockForMin;
    }
    public void setBlockedEndTime(LocalDateTime blockedEndTime){
        this.blockedEndTime = blockedEndTime;
    }
    public LocalDateTime getBlockedEndTime(){
         return this.blockedEndTime;
    }

}
