/*
 * RESTHeart Security
 * 
 * Copyright (C) SoftInstigate Srl
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.restheart.security.handlers;

import io.undertow.server.HttpServerExchange;
import java.nio.ByteBuffer;
import org.restheart.handlers.PipelinedHandler;
import org.restheart.handlers.exchange.ByteArrayResponse;

/**
 *
 * sends the response content attached to HttpServerExchange to the client
 *
 * @see ExchangeHelper.getResponseContentAsJson() and
 * ExchangeHelper.getResponseContent()
 * @author Andrea Di Cesare {@literal <andrea@softinstigate.com>}
 */
public class ResponseSender extends PipelinedHandler {

    /**
     *
     */
    public ResponseSender() {
        super(null);
    }

    /**
     * @param next
     */
    public ResponseSender(PipelinedHandler next) {
        super(next);
    }

    /**
     *
     * @param exchange
     * @throws Exception
     */
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        var response = ByteArrayResponse.wrap(exchange);

        if (!exchange.isResponseStarted() && response.getStatusCode() > 0) {
            exchange.setStatusCode(response.getStatusCode());
        }

        if (response.isContentAvailable()) {
            exchange.getResponseSender().send(
                    ByteBuffer.wrap(response.readContent()));
        }

        exchange.endExchange();

        next(exchange);
    }
}
