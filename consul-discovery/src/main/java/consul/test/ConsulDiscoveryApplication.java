package consul.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ConsulDiscoveryApplication {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private Environment env;

	@RequestMapping("/")
    public String home() {
        return "Hello world";
    }

	public static void main(String[] args) {
    	new SpringApplicationBuilder(ConsulDiscoveryApplication.class).web(true).run(args);
    }

    @RequestMapping("/services")
    public List<String> serviceUrl() {
    	List<String> services = discoveryClient.getServices();
        List <String> res = new ArrayList<String>();
    	for (String service:services){
        	res.add(service);
        	List<ServiceInstance> instances = discoveryClient.getInstances(service);
        	for (ServiceInstance instance:instances){
        		res.add(instance.getHost() + ":" + instance.getPort());
        	}
        }
        return res;
    }
    
    @RequestMapping("/config")
    public String env() {
    	return env.getProperty("db.host");
    }
}
