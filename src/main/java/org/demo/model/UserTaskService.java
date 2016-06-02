package org.demo.model;

import java.util.Collection;

public interface UserTaskService
{
    Collection<UserTask> findAll();
    
    Collection<UserTask> findByUserName(String userName);    
}
