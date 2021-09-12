package mk.ukim.finki.photography.photoshoot;

import mk.ukim.finki.photography.model.Photoshoot;
import mk.ukim.finki.photography.model.Role;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.repository.PhotoshootRepository;
import mk.ukim.finki.photography.repository.UserRepository;
import mk.ukim.finki.photography.service.impl.PhotoshootServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.social.facebook.api.Photo;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class PhotoshootUnitTests {
/**
 * Here the functionality of the repository is mocked.
 * I am testing the service methods of Photoshoot Service class.
 * */

    @Mock
    private PhotoshootRepository photoshootRepository;

    @Mock
    private UserRepository userRepository;

    private PhotoshootServiceImpl service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        User user1 = new User("photographer", "password", "name", "surname", Role.ROLE_USER, "", "", "");
        User user2 = new User("user", "password", "name", "surname", Role.ROLE_USER, "", "", "");

        Mockito.when(userRepository.findByUsername("photographer")).thenReturn(Optional.of(user1));

        Photoshoot photoshoot1 = new Photoshoot(user1, user2, "08-09-2021", "10:00", "Afternoon");
        photoshoot1.setId((long) 12456);

        Photoshoot photoshoot2 = new Photoshoot(user1, user2, "08-09-2021", "10:00", "Afternoon");
        photoshoot2.setId((long) 12645);

        Mockito.when(this.photoshootRepository.findById((long) 154444444)).thenReturn(null);
        Mockito.when(this.photoshootRepository.findById((long) 12456)).thenReturn(Optional.of(photoshoot1));
        Mockito.when(this.photoshootRepository.save(Mockito.any(Photoshoot.class))).thenReturn(photoshoot1);
        Mockito.when(this.photoshootRepository.save(null)).thenReturn(null);
        Mockito.when(this.photoshootRepository.findAllByPhotographer(null)).thenReturn(new ArrayList<Photoshoot>());
        Mockito.when(this.photoshootRepository.findAllByPhotographer(user1)).thenReturn(Arrays.stream(new Photoshoot[]{photoshoot1, photoshoot2}).collect(Collectors.toList()));
        service = Mockito.spy(new PhotoshootServiceImpl(photoshootRepository));
    }

    /**
     * The method tested here is findById(Long Id) and I use ISP Interface based approach.
     * Characteristics:
     *      C1 - Value of Id
     *          b1 - Id < 0
     *          b2 - Id = 0
     *          b3 - Id > 0
     * */

    /**
     * b1 - Id < 0
     * */
    @Test
    public void testFindByIdT1(){
        // I assume that this photoshoot is not present in the database
        Assert.assertThrows("NoSuchElementException expected", NoSuchElementException.class, () -> this.service.findById((long) -145));
    }

    /**
     * b2 - Id = 0
     * */
    @Test
    public void testFindByIdT2() {
        Assert.assertThrows("NoSuchElementException expected", NoSuchElementException.class, () -> this.service.findById((long)0));
    }

    /**
     * b3 - Id > 0
     * */
    @Test
    public void testFindByIdT3() {
        Assert.assertThrows("NoSuchElementException expected", NoSuchElementException.class, () -> this.service.findById((long)1456));
    }

    @Test
    public void testFindByIdNotFound()
    {
        Assert.assertThrows("Null pointer exception expected", NullPointerException.class, () -> this.service.findById((long) 154444444));
    }

    @Test
    public void testFindByIdFound(){
        User user1 = new User("photographer", "password", "name", "surname", Role.ROLE_USER, "", "", "");
        User user2 = new User("user", "password", "name", "surname", Role.ROLE_USER, "", "", "");
        Photoshoot photoshoot = new Photoshoot(user1, user2, "08-09-2021", "10:00", "Afternoon");
        Photoshoot result = this.service.save(photoshoot);
        Assert.assertNotNull(this.service.findById(result.getId()));
    }

    /**
     * Tests for the save method.
     * Method definition: public Photoshoot save(Photoshoot photoshoot)
     * Characteristics:
     *          C1 - photoshoot is null
     *              b1 - true
     *              b2 - false
     * */

    /**
     * b1 - Photoshoot is null
     * */
    @Test
    public void testSaveT1() {
        Photoshoot res = service.save(null);
        Mockito.verify(service).save(null);
        Assert.assertNull(res);
    }

    /**
     * b2 - Photoshoot is not null
     * */
    @Test
    public void testSaveT2()
    {
        User user1 = new User("photographer", "password", "name", "surname", Role.ROLE_USER, "", "", "");
        User user2 = new User("user", "password", "name", "surname", Role.ROLE_USER, "", "", "");

        Photoshoot photoshoot = new Photoshoot(user1, user2, "08-09-2021", "10:00", "Afternoon");
        Photoshoot result = this.service.save(photoshoot);

        // check if all the properties are the same

        Assert.assertEquals(photoshoot.getUser().getUsername(),result.getUser().getUsername() );
        Assert.assertEquals(photoshoot.getPhotographer().getUsername(), result.getPhotographer().getUsername());
        Assert.assertEquals(photoshoot.getDate(), result.getDate());
        Assert.assertEquals(photoshoot.getHour(), result.getHour());
        Assert.assertEquals(photoshoot.getPartOfDay(), result.getPartOfDay());
    }

    /**
     * Tests for the method findByPhotographer(User photographer) with ISP interface method.
     * Characteristics:
     *      C1 - photographer is null
     *          b1 - true
     *          b2 - false
     * */

    @Test
    public void testFindByPhotographerT1() {
       List<Photoshoot> result = this.service.findByPhotographer(null);
       Assert.assertEquals(0, result.size());
    }

    @Test
    public void testFindByPhotographerT2(){
        Optional<User> user = this.userRepository.findByUsername("photographer");
        if(user.isPresent())
        {
            List<Photoshoot> result = this.service.findByPhotographer(user.get());
            Assert.assertEquals(2, result.size());
        }
        else{
            fail("The photographer is not found");
        }
    }
}
