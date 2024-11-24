package fsts.rsi.secservices;

import fsts.rsi.secservices.Services.AccountService;
import fsts.rsi.secservices.entities.AppUser;
import fsts.rsi.secservices.entities.Approle;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SecServicesApplication  {

    public static void main(String[] args) {
        SpringApplication.run(SecServicesApplication.class, args);
    }

    @Bean
    CommandLineRunner start(AccountService accountService) {
        return args -> {
            accountService.addNewUser(new AppUser(null,"oussamaettaghy.it@gmail.com"
                    ,new BCryptPasswordEncoder().encode("admin2002"),new ArrayList<>()));



            accountService.addNewrole(new Approle(null,"ADMIN"));
            accountService.addNewrole(new Approle(null,"USER"));
            accountService.addNewrole(new Approle(null,"MANAGER"));
            accountService.addNewrole(new Approle(null,"CUSTOMER"));


            accountService.AddRoleToUser("ADMIN", "oussamaettaghy.it@gmail.com");



        };
    }

}



