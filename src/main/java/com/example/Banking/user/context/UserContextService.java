package com.example.Banking.user.context;

import com.example.Banking.user.User;

public interface UserContextService {
    User getCurrentUser();
    Long getCurrentUserId();
    String getCurrentUsername();
}
