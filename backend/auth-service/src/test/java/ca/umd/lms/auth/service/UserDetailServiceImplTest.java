package ca.umd.lms.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@TestPropertySource(locations = "classpath:test.yaml")
//@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDetailServiceImplTest {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsername() {
        String result = userDetailsService.loadUserByUsername("aanglish@umd.edu").getPassword();
        assert result.equals("$2a$10$E1YPtuH.jGsCw/45wnm3NuzrC2VFT14TdNf5i3kChoJd.Y8jSox6u");

        try{ result = userDetailsService.loadUserByUsername("aanglish").getPassword();}
        catch (UsernameNotFoundException e) {
            assert e.getMessage().equals("User not found");
        }

        try{ result = userDetailsService.loadUserByUsername("hsiveyer@umd.edu").getPassword();}
        catch (UsernameNotFoundException e) {
            assert e.getMessage().equals("User not found");
        }
        assert result.equals("$2a$10$E1YPtuH.jGsCw/45wnm3NuzrC2VFT14TdNf5i3kChoJd.Y8jSox6u");

        try{ result = userDetailsService.loadUserByUsername("admin@umd.edu").getPassword();}
        catch (UsernameNotFoundException e) {
            assert e.getMessage().equals("User not found");
        }
        assert result.equals("$2a$10$bAAnP0n1bnjnu/gYnsFqtuzg1IGggsVEW3d3yNHdxTLFolpTtnt6m");
    }

}
