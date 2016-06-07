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

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.demo.model.UserTask.Status;

/**
 * JPA providing CRUD operations on {@link UserTask}.
 */
public class UserTaskServiceJPA implements UserTaskService
{
    private static UserTaskServiceJPA instance;

    private UserTaskServiceJPA()
    {
    }

    public static UserTaskServiceJPA getInstance()
    {
        if (instance == null)
        {
            instance = new UserTaskServiceJPA();
        }
        return instance;
    }

    private EntityManager em = createEntityManager();

    public Collection<UserTask> findAll()
    {
        final TypedQuery<UserTask> query = em.createNamedQuery("UserTask.findAll", UserTask.class);
        return query.getResultList();
    }

    @Override
    public void insert(UserTask userTask)
    {
        try
        {
            em.getTransaction().begin();
            em.persist(userTask);
            em.getTransaction().commit();
        }
        catch (Exception e)
        {
            em.getTransaction().rollback();
        }
    }

    @Override
    public void update(UserTask userTask)
    {
        if (assignable(userTask.getId(), userTask.getUserName()))
        {
            try
            {
                em.getTransaction().begin();
                em.merge(userTask);
                em.getTransaction().commit();
            }
            catch (Exception e)
            {
                em.getTransaction().rollback();
            }
        }
        else
        {
            throw new TaskAlreadyAssigned("Error. Task cannot be assigned.");
        }
    }

    public Collection<UserTask> findByUserName(String userName)
    {
        final TypedQuery<UserTask> query = em.createNamedQuery("UserTask.findByUserName", UserTask.class);
        query.setParameter("userName", userName);
        return query.getResultList();
    }

    public static void loadDefaults()
    {
        EntityManager em = null;
        try
        {
            em = getInstance().em;
            em.getTransaction().begin();
            em.persist(UserTask.create("Orange Festival", "John", "Hotel.", Status.PENDING, "Mid range. Breakfast included. Close to the festival."));
            em.persist(UserTask.create("Orange Festival", "Audrey", "Train tickets.", Status.PENDING, "Depart on Friday (we should be there at 16h), return on Sunday night or Monday morning."));
            em.persist(UserTask.create("Orange Festival", "-", "Snaks for the ride.", Status.PENDING, "Some chips and beer do the trick."));
            em.persist(UserTask.create("Orange Festival", "-", "Guide.", Status.PENDING, "Pick up some Warsaw city guide, or some printouts."));
            em.getTransaction().commit();
        }
        catch (Exception e)
        {
            if (em != null)
            {
                em.getTransaction().rollback();
            }
        }
    }

    private static EntityManager createEntityManager()
    {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("angularjsdemo");
        return emf.createEntityManager();
    }

    private boolean assignable(Long id, String userName)
    {
        try
        {
            em.getTransaction().begin();
            final UserTask ut = em.find(UserTask.class, id, LockModeType.PESSIMISTIC_WRITE);
            return userName.equals(ut.getUserName()) || "-".equals(ut.getUserName());
        }
        finally
        {
            em.getTransaction().rollback();
        }
    }

    public static class TaskAlreadyAssigned extends RuntimeException
    {
        private static final long serialVersionUID = 1L;

        public TaskAlreadyAssigned(String message)
        {
            super(message);
        }
    }
}
