package jelog.server.main.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Model.Jwt.Authority;
import jelog.server.main.Repositories.DN_UserRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static jelog.server.main.Controller.BaseController.randomLetters;

/**
 * [Service]
 * DN_Users
 * */
//-----------------------------------------------------------------------------------------------------------------------------------------
@Slf4j
@Service
public class DN_UserService {

    /**
     * [Variables]
     * Repository
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private final DN_UserRepositories dn_userRepositories;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public DN_UserService(DN_UserRepositories dn_userRepositories, PasswordEncoder passwordEncoder){
        this.dn_userRepositories = dn_userRepositories;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * [User]
     * Validate
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    public void validateUser(final DN_UserModel _model){
        if(null == _model){
            log.warn("data is check for cannot be null.");
            throw new RuntimeException("data is check for cannot be null.");
        }
    }

    public void validateUserId(final DN_UserModel _model){
        boolean data = dn_userRepositories.existsByDaSignID(_model.getDaSignID());
        if(false == data){
            log.warn("sign data is check for cannot be null");
            throw new RuntimeException("sign data is check for cannot be null");
        }
    }

    public void validateExistUserId(final DN_UserModel _model){
        boolean data = dn_userRepositories.existsByDaSignID(_model.getDaSignID());
        if(true == data){
            log.warn("exist User SignId");
            throw  new RuntimeException("exist User SignId");
        }
    }

    public void validateSign(final String a, final String b){

        if(null != a & null != b){
            Optional<DN_UserModel> u = dn_userRepositories.findByDaSignID(a);
            if(u.isEmpty()) {
                log.warn("User not found with ID: {}", a);
                throw new RuntimeException("User not found");
            }
            
            // Use BCrypt to verify password
            if(!passwordEncoder.matches(b, u.get().getDnPasswd())){
                log.warn("Password does not match for user: {}", a);
                throw new RuntimeException("Password does not match");
            }

        }else{
            log.warn("Username or password is null");
            throw new RuntimeException("Username or password is null");
        }

    }

    /**
     * [User]
     * Issuing a user account
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    @Transactional
    public DN_UserModel createUser(final DN_UserModel entity){

        try{
            validateUser(entity);
            validateExistUserId(entity);

            if(null == entity.getDnName()){ entity.setDnName(randomLetters(8)); }

            // BCrypt Password Encoding
            entity.setDnPasswd(passwordEncoder.encode(entity.getDnPasswd()));
            entity.setDnSalt(null); // Not needed with BCrypt

            // 권한 설정
            assignRole(entity);

            // 사용자 생성
            DN_UserModel savedEntity = dn_userRepositories.save(entity);
            log.info("User: {} is saved.", savedEntity.getDnUid());

            return savedEntity;
        }catch(Exception e){
            log.error("Error creating user: {}", e.getMessage(), e);
            throw new RuntimeException("User creation failed.");
        }
    }

    /**
     * [User]
     * Issuing a user account
     * 권한설정
     * */
    private void assignRole(DN_UserModel entity) {
        String roleName;
        switch (entity.getDnUserAuthEnum().getTitleCode()) {
            case 24678:
                roleName = "ROLE_ADMIN";
                break;
            case 20000:
            case 20344:
            default:
                roleName = "ROLE_USER";
                break;
        }

        Authority authority = Authority.builder().name(roleName).build();
        authority.setDnUserModel(entity);
        entity.setRoles(Collections.singletonList(authority));
    }

    /**
     * [User]
     * Sign-In
     * */
    public DN_UserModel signUser(final String signUserID, final String dnPassword){
        try {
            validateSign(signUserID, dnPassword);
            Optional<DN_UserModel> user = dn_userRepositories.findByDaSignID(signUserID);
            if(user.isEmpty()) {
                throw new RuntimeException("User not found");
            }
            
            DN_UserModel authUser = user.get();
            validateUser(authUser);
            validateUserId(authUser);

            // Password masking for security
            authUser.setDnPasswd("*****");
            authUser.setDnSalt("*****");
            return authUser;
        }catch (Exception e){
            log.error("Error signing in user: {}", e.getMessage(), e);
            throw new RuntimeException("User sign-in failed. ", e);
        }
    }

    // Enhanced methods for admin functionality
    
    /**
     * Get total user count
     */
    @Cacheable(value = "userCount")
    public long getTotalUserCount() {
        return dn_userRepositories.count();
    }
    
    /**
     * Get all users with pagination
     */
    public Page<DN_UserModel> getAllUsers(Pageable pageable) {
        return dn_userRepositories.findAll(pageable);
    }
    
    /**
     * Search users by keyword
     */
    public Page<DN_UserModel> searchUsers(Pageable pageable, String keyword) {
        return dn_userRepositories.findByDaSignIDContainingIgnoreCaseOrDnNameContainingIgnoreCase(
            keyword, keyword, pageable);
    }
    
    /**
     * Update user status
     */
    @Transactional
    public void updateUserStatus(int userId, boolean active) {
        DN_UserModel user = dn_userRepositories.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        
        user.setStatus(active ? "ACTIVE" : "INACTIVE");
        dn_userRepositories.save(user);
        
        log.info("User {} status updated to: {}", userId, user.getStatus());
    }
    
    /**
     * Get recent users
     */
    public List<DN_UserModel> getRecentUsers(int limit) {
        return dn_userRepositories.findTop10ByOrderByInDateDesc();
    }
    
    /**
     * Get active users
     */
    public List<DN_UserModel> getActiveUsers(int limit) {
        return dn_userRepositories.findByStatusOrderByLastLoginDateDesc("ACTIVE");
    }
    
    /**
     * Update last login date
     */
    @Transactional
    public void updateLastLoginDate(String signId) {
        Optional<DN_UserModel> userOpt = dn_userRepositories.findByDaSignID(signId);
        if (userOpt.isPresent()) {
            DN_UserModel user = userOpt.get();
            user.setLastLoginDate(LocalDateTime.now());
            user.resetLoginAttempts(); // Reset failed login attempts on successful login
            dn_userRepositories.save(user);
        }
    }
    
    /**
     * Handle failed login attempt
     */
    @Transactional
    public void handleFailedLoginAttempt(String signId) {
        Optional<DN_UserModel> userOpt = dn_userRepositories.findByDaSignID(signId);
        if (userOpt.isPresent()) {
            DN_UserModel user = userOpt.get();
            user.incrementLoginAttempts();
            dn_userRepositories.save(user);
            
            if (user.isAccountLocked()) {
                log.warn("Account locked due to too many failed login attempts: {}", signId);
            }
        }
    }
    
    /**
     * Check if account is locked
     */
    public boolean isAccountLocked(String signId) {
        Optional<DN_UserModel> userOpt = dn_userRepositories.findByDaSignID(signId);
        return userOpt.map(user -> !user.isAccountNonLocked()).orElse(false);
    }
    
    /**
     * Update user profile
     */
    @Transactional
    public DN_UserModel updateUserProfile(int userId, DN_UserModel updatedUser) {
        DN_UserModel existingUser = dn_userRepositories.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        
        // Update allowed fields
        existingUser.setDnName(updatedUser.getDnName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setNickname(updatedUser.getNickname());
        existingUser.setBio(updatedUser.getBio());
        existingUser.setWebsite(updatedUser.getWebsite());
        existingUser.setLocation(updatedUser.getLocation());
        existingUser.setProfileImage(updatedUser.getProfileImage());
        
        return dn_userRepositories.save(existingUser);
    }
    
    /**
     * Change password
     */
    @Transactional
    public void changePassword(int userId, String currentPassword, String newPassword) {
        DN_UserModel user = dn_userRepositories.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getDnPasswd())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        // Update password
        user.setDnPasswd(passwordEncoder.encode(newPassword));
        user.setDnSalt(null); // Not needed with BCrypt
        dn_userRepositories.save(user);
        
        log.info("Password changed for user: {}", userId);
    }
    
    /**
     * Generate password reset token
     */
    @Transactional
    public String generatePasswordResetToken(String email) {
        Optional<DN_UserModel> userOpt = dn_userRepositories.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
        
        DN_UserModel user = userOpt.get();
        String token = java.util.UUID.randomUUID().toString();
        user.setPasswordResetToken(token);
        user.setPasswordResetExpiry(LocalDateTime.now().plusHours(24)); // 24 hours expiry
        dn_userRepositories.save(user);
        
        return token;
    }
    
    /**
     * Reset password with token
     */
    @Transactional
    public void resetPasswordWithToken(String token, String newPassword) {
        Optional<DN_UserModel> userOpt = dn_userRepositories.findByPasswordResetToken(token);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid password reset token");
        }
        
        DN_UserModel user = userOpt.get();
        if (user.getPasswordResetExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token has expired");
        }
        
        // Update password and clear reset token
        user.setDnPasswd(passwordEncoder.encode(newPassword));
        user.setDnSalt(null);
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiry(null);
        user.resetLoginAttempts(); // Reset failed attempts
        dn_userRepositories.save(user);
        
        log.info("Password reset completed for user: {}", user.getDaSignID());
    }
    
    /**
     * Delete user account
     */
    @Transactional
    public void deleteUser(int userId) {
        DN_UserModel user = dn_userRepositories.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        
        // Soft delete - just mark as deleted
        user.setStatus("DELETED");
        dn_userRepositories.save(user);
        
        log.info("User deleted: {}", userId);
    }

}
