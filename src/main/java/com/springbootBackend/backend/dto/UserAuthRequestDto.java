package com.springbootBackend.backend.dto;

public class UserAuthRequestDto {

     private String email;
     private String userName;
     private String password;

   public void setEmail(String email){
    this.email = email;
   }
   public String getEmail(){
       return this.email;
   }

   public void setUserName(String userName){
       this.userName = userName;
   }

   public String getUserName(){
       return this.userName;
   }

   public void setPassword(String password){
       this.password  = password;
   }

   public String getPassword(){
       return password;
   }

}
