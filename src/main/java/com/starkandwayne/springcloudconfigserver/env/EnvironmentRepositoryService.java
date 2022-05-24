package com.starkandwayne.springcloudconfigserver.env;

import java.util.Iterator;
import java.util.List;
import org.springframework.cloud.config.server.environment.MultipleJGitEnvironmentRepository;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentRepositoryService {
   private List environmentRepositories;

   public EnvironmentRepositoryService(List environmentRepository) {
      this.environmentRepositories = environmentRepository;
   }

   public void refreshLocalGitClones() {
      Iterator var1 = this.environmentRepositories.iterator();

      while(var1.hasNext()) {
         MultipleJGitEnvironmentRepository repository = (MultipleJGitEnvironmentRepository)var1.next();
         repository.findOne("application", "default", (String)null);
      }

   }
}