package com.sport.integration;

import com.sport.config.DataServiceProperties;
import com.sport.data.SubRouteJson;
import com.sport.exception.CityServiceNotAvailableException;
import com.sport.util.SubRouteJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;

/**
 * Integration handler responsible for handling database service calls
 */
@Component
public class CityServiceHandler {

    private String endpointUrl;

    private String endpointPassword;

    private String endpointUsername;

    private String endpointPath;

    private DataServiceProperties dataServiceProperties;

    private DiscoveryClient discoveryClient;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setProperties(DataServiceProperties dataServiceProperties) {
        this.dataServiceProperties = dataServiceProperties;

        this.endpointUrl = this.dataServiceProperties.getHostname() + ":" + this.dataServiceProperties.getPort();

        if (!this.dataServiceProperties .getPath().startsWith("/")) {
            this.endpointPath = "/" + dataServiceProperties.getPath();
        } else {
            this.endpointPath = dataServiceProperties.getPath();
        }

        this.endpointPassword = this.dataServiceProperties.getPassword();
        this.endpointUsername = this.dataServiceProperties.getUsername();
    }

    @Autowired
    public void setDiscoveryClient(DiscoveryClient discoveryClient){
        this.discoveryClient = discoveryClient;
    }

    /**
     * Fetches all routes available by calling backend db service
     * @return List of routes available (called sub-routes since together they form complete routes)
     * @throws IOException
     */
    public List<SubRouteJson> fetchRoutes() {

        List<SubRouteJson> subRoutes;

        try {
            final StringBuffer urlString = new StringBuffer();

            String urlWithPath;

            // Let's see if service registry has the address, if not let's go with default
            final List<ServiceInstance> list = discoveryClient.getInstances(dataServiceProperties.getName());
            if (list != null && list.size() > 0 ) {
                urlWithPath = "" + list.get(0).getUri();
                log.info("DataService address found in registry");
            } else {
                urlWithPath = this.endpointUrl;
                log.info("DataService address not found in registry. Configured direct address used");
            }

            urlWithPath += this.endpointPath;

            urlString.append(urlWithPath);

            log.info("Calling url " + urlString);
            URL url = new URL(urlString.toString());

            String encoding = Base64.getEncoder().encodeToString((endpointUsername + ":" + endpointPassword).getBytes());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Basic " + encoding);

            InputStream content = connection.getInputStream();

            SubRouteJsonMapper mapper = new SubRouteJsonMapper();

            subRoutes = mapper.convertToSubRoutes(content);

        } catch (IOException e) {
            // Returning not available, we want to have this service shown unavailable not to reveal anything from below
            log.warn("Failed to fetch routes from database service ", e);
            throw new CityServiceNotAvailableException();
        }

        return subRoutes;
    }
}
