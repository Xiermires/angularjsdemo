/*******************************************************************************
 * Copyright (c) 2016, Xavier Miret Andres <xavier.mires@gmail.com>
 *
 * Permission to use, copy, modify, and/or distribute this software for any 
 * purpose with or without fee is hereby granted, provided that the above 
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES 
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALLIMPLIED WARRANTIES OF 
 * MERCHANTABILITY  AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR 
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES 
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN 
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF 
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *******************************************************************************/
package org.demo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Straightforward table to keep all info.
 */
@Entity
@NamedQueries({ @NamedQuery(name = "UserTask.findAll", query = "SELECT ut FROM UserTask ut"),
        @NamedQuery(name = "UserTask.findByUserName", query = "SELECT ut FROM UserTask ut WHERE ut.userName = :userName"), })
public class UserTask implements Serializable
{
    private static final long serialVersionUID = 1L;

    public enum Status { DONE, PENDING }; 
    
    public static UserTask create(String eventName, String userName, String taskName, Status status, String description)
    {
        final UserTask ut = new UserTask();
        ut.setEventName(eventName);
        ut.setUserName(userName);
        ut.setTaskName(taskName);
        ut.setStatus(status);
        ut.setDescription(description);
        return ut;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String eventName;

    private String userName;

    private String taskName;

    private Status status;
    
    private String description;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }
    
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventName == null) ? 0 : eventName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        UserTask other = (UserTask) obj;
        if (eventName == null)
        {
            if (other.eventName != null) return false;
        }
        else if (!eventName.equals(other.eventName)) return false;
        if (id == null)
        {
            if (other.id != null) return false;
        }
        else if (!id.equals(other.id)) return false;
        if (taskName == null)
        {
            if (other.taskName != null) return false;
        }
        else if (!taskName.equals(other.taskName)) return false;
        if (userName == null)
        {
            if (other.userName != null) return false;
        }
        else if (!userName.equals(other.userName)) return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "org.demo.angularjs.model.UserTasks[ id=" + id + " ]";
    }
}
