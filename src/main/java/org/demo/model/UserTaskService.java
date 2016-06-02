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
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * JPA providing CRUD operations on {@link UserTask}.
 */
public class UserTaskService
{
    private static UserTaskService instance;

    private UserTaskService()
    {
    }

    public static UserTaskService getInstance()
    {
        if (instance == null)
        {
            instance = new UserTaskService();
        }
        return instance;
    }

    private EntityManager em = createEntityManager();

    public Collection<UserTask> findAll()
    {
        final TypedQuery<UserTask> query = em.createNamedQuery("UserTask.findAll", UserTask.class);
        return query.getResultList();
    }

    public Collection<UserTask> findByUserName(String userName)
    {
        final TypedQuery<UserTask> query = em.createNamedQuery("UserTask.findByUserName", UserTask.class);
        query.setParameter("userName", userName);
        return query.getResultList();
    }

    public static void loadDefaults()
    {
        final EntityManager em = getInstance().em;
        em.getTransaction().begin();
        em.persist(UserTask.create("Xavier Mires", "vert.x", "set up endpoints"));
        em.persist(UserTask.create("Xavier Mires", "angularjs", "bind rest services"));
        em.persist(UserTask.create("Xavier Mires", "angularjs", "create front-end"));
        em.persist(UserTask.create("unassigned", "angularjs", "css"));
        em.getTransaction().commit();
    }

    private static EntityManager createEntityManager()
    {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("angularjsdemo");
        return emf.createEntityManager();
    }
}
