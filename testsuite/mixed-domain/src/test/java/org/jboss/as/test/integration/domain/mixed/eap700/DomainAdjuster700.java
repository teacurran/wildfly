/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.test.integration.domain.mixed.eap700;

import java.util.ArrayList;
import java.util.List;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.client.helpers.domain.DomainClient;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.test.integration.domain.mixed.DomainAdjuster;
import org.jboss.dmr.ModelNode;

/**
 * Does adjustments to the domain model for 7.0.0 legacy slaves.
 *
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
public class DomainAdjuster700 extends DomainAdjuster {

    @Override
    protected List<ModelNode> adjustForVersion(final DomainClient client, PathAddress profileAddress) throws Exception {
        final List<ModelNode> list = new ArrayList<>();

        removeHTTPSListener(profileAddress, list);

        return list;
    }
    /**
     *  EAP 7.0 and earlier required explicit SSL configuration. Wildfly 10.1 added support
     *  for SSL by default, which automatically generates certs.
     *
     *  This could be removed if all hosts were configured to contain a security domain with SSL
     *  enabled.
     */
    private void removeHTTPSListener(PathAddress profileAddress, List<ModelNode> ops) {

        PathAddress undertow = profileAddress
                .append(PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM, "undertow"))
                .append(PathElement.pathElement("server", "default-server"))
                .append(PathElement.pathElement("https-listener", "https"));
        ModelNode op = Util.getEmptyOperation(ModelDescriptionConstants.REMOVE, undertow.toModelNode());
        ops.add(op);
    }

}
