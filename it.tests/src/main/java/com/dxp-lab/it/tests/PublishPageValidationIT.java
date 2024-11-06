/*
 *  Copyright 2020 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.dxp-lab.it.tests;

import com.adobe.cq.cloud.testing.it.smoke.rules.CQCloudManagerBaseClass;
import com.adobe.cq.cloud.testing.it.smoke.rules.CQCloudManagerPublishClassRule;
import com.adobe.cq.cloud.testing.it.smoke.rules.CloudManagerRule;
import com.adobe.cq.cloud.testing.util.HtmlUnitClient;
import com.adobe.cq.cloud.testing.clients.CloudManagerClient;
import com.adobe.cq.cloud.testing.utility.CloudManagerAssert;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.sling.testing.clients.ClientException;
import org.apache.sling.testing.clients.SlingHttpResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Validates pages on publish and makes sure that the page renders completely and also
 * validates all linked resources (images, clientlibs etc).
 * 
 */
public class PublishPageValidationIT {

    // the page to test
    private static final String HOMEPAGE = "/";

    // list files which do return a zerobyte response body
    private static final List<String> ZEROBYTEFILES = Arrays.asList();

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(PublishPageValidationIT.class);

    @ClassRule
    public static final CQCloudManagerPublishClassRule cqBaseClassRule = new CQCloudManagerPublishClassRule();

    @Rule
    public CloudManagerRule cqBaseRule = new CloudManagerRule(cqBaseClassRule.publishRule);

    private static HtmlUnitClient adminPublish;

    @BeforeClass
    public static void beforeClass() throws ClientException {
        adminPublish = cqBaseClassRule.publishRule.getAdminClient(CloudManagerClient.class).adaptTo(HtmlUnitClient.class);
    }

    @AfterClass
    public static void afterClass() {
        closeQuietly(adminPublish);
    }

    @Test
    public void validateHomepage() throws ClientException, IOException, URISyntaxException {
        String path = HOMEPAGE;
        verifyPage(adminPublish, path);
        verifyLinkedResources(adminPublish, path);
    }

    private static void verifyPage(HtmlUnitClient client, String path) throws ClientProtocolException, IOException {
        URI baseURI = client.getBaseUrl();
        LOG.info("Using {} as baseURL", baseURI.toString());
        HttpGet get = new HttpGet(baseURI.toString() + path);
        org.apache.http.HttpResponse validationResponse = client.execute(get);
        assertEquals("Request to [" + get.getURI().toString() + "] does not return expected returncode 200",
                200, validationResponse.getStatusLine().getStatusCode());
    }

    private static void verifyLinkedResources(HtmlUnitClient client, String path) throws ClientException, IOException, URISyntaxException {
        List<URI> references = client.getResourceReferences(path);
        assertTrue(path + " does not contain any references!", references.size() > 0);
        for (URI ref : references) {
            if (isSameOrigin(client.getBaseUrl(), ref)) {
                LOG.info("verifying linked resource {}", ref.toString());
                SlingHttpResponse response = client.doGet(ref.getRawPath());
                int statusCode = response.getStatusLine().getStatusCode();
                int responseSize = response.getContent().length();
                assertEquals("Unexpected status returned from [" + ref + "]", 200, statusCode);
                if (!ZEROBYTEFILES.stream().anyMatch(s -> ref.getPath().startsWith(s))) {
                    assertTrue("Empty response body from [" + ref + "]", responseSize > 0);
                }
            } else {
                LOG.info("skipping linked resource from another domain {}", ref.toString());
            }
        }
    }

    /** Checks if two URIs have the same origin.
     *
     * @param uri1 first URI
     * @param uri2 second URI
     * @return true if two URI come from the same host, port and use the same scheme
     */
    private static boolean isSameOrigin(URI uri1, URI uri2) {
        if (!uri1.getScheme().equals(uri2.getScheme())) {
            return false;
        } else return uri1.getAuthority().equals(uri2.getAuthority());
    }
}