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
package org.demo.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import org.demo.model.UserTaskService;
import org.demo.model.UserTaskServiceJPA;
import org.demo.util.Json;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Some REST endpoints.
 */
public class VertxServer extends AbstractVerticle
{
    final UserTaskService uts = UserTaskServiceJPA.getInstance();

    public static void init()
    {
        Vertx.vertx().deployVerticle(VertxServer.class.getName());
    }

    @Override
    public void start() throws Exception
    {
        final Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.get("/angularjsdemo/userTasks").handler(ctx ->
        {
            try
            {
                ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                ctx.response().end(Json.toJson(uts.findAll()));
            }
            catch (JsonProcessingException e)
            {
                e.printStackTrace(); // FIXME : log.
            }
        });

        router.get("/angularjsdemo/userTasks/:user").handler(ctx ->
        {
            try
            {
                final String userName = ctx.request().getParam("user");
                ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                ctx.response().end(Json.toJson(uts.findByUserName(userName)));
            }
            catch (JsonProcessingException e)
            {
                e.printStackTrace(); // FIXME : log.
            }
        });

        router.route().handler(StaticHandler.create());        
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}
