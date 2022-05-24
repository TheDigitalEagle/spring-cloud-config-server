package com.starkandwayne.springcloudconfigserver.env;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starkandwayne.springcloudconfigserver.util.Redactor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringCloudConfigServerConfigLoader {
   public static final String ENCRYPT_KEY = "encrypt.key";
   private final Map springCloudConfigServerConfiguration;

   SpringCloudConfigServerConfigLoader(Environment environment) {
      Map configMap = (Map)Binder.get(environment).bind("spring.cloud.config.server", Map.class).get();
      Map redact = (Map)Redactor.redact(configMap);
      if (environment.containsProperty("encrypt.key")) {
         Map encryptKey = Collections.singletonMap("encrypt", Collections.singletonMap("key", environment.getProperty("encrypt.key")));
         redact.putAll((Map)Redactor.redact(encryptKey));
      }

      if (environment.containsProperty("application-security-groups")) {
         redact.put("application-security-groups", this.applicationSecurityGroups(environment.getProperty("application-security-groups")));
      }

      this.springCloudConfigServerConfiguration = Collections.unmodifiableMap(redact);
   }

   public Map getRedactedConfig() {
      return this.springCloudConfigServerConfiguration;
   }

   private List applicationSecurityGroups(String applicationSecurityGroups) {
      if ("".equals(applicationSecurityGroups)) {
         return Collections.emptyList();
      } else {
         try {
            return (List)Arrays.stream((Object[])(new ObjectMapper()).readValue(applicationSecurityGroups, String[].class)).distinct().sorted().collect(Collectors.toList());
         } catch (JsonProcessingException var3) {
            throw new RuntimeException("Unable to parse application security groups", var3);
         } catch (IOException e) {
            throw new RuntimeException("Unable to parse application security groups", e);
        }
      }
   }
}