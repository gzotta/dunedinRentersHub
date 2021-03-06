/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.Services;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author sarahaverill
 */
public class ServicesJdbcDAOTest {

    ServicesJdbcDAO s = new ServicesJdbcDAO("jdbc:h2:mem:tests;INIT=runscript from 'src/main/java/dao/databaseSchema.sql'");

    private Services s1;
    private Services s2;
    private Services s3;

    public ServicesJdbcDAOTest() {
    }

    @BeforeEach
    public void setUp() {
        s1 = new Services();
        s2 = new Services();
        s3 = new Services();
        s1.setServiceType("Type1");
        s1.setServicePassword("sp1");
        s1.setUsername("ser1user");
        s1.setServicePhone("ser1phon");
        s1.setServiceEmail("ser1email");

        s2.setServiceType("Type2");
        s2.setServicePassword("sp2");
        s2.setUsername("ser2user");
        s2.setServicePhone("ser2phon");
        s2.setServiceEmail("ser2email");

        s3.setServiceType("Type3");
        s3.setServicePassword("sp3");
        s3.setUsername("ser3user");
        s3.setServicePhone("ser3phon");
        s3.setServiceEmail("ser3email");
        s.saveService(s1);
        s.saveService(s2);
    }

    @AfterEach
    public void tearDown() {
        s.removeServices(s1);
        s.removeServices(s2);
        s.removeServices(s3);

    }

    @Test
    public void testSaveService() {
        s.saveService(s3);
        assertThat(s.getAllServices(), hasSize(3));
        assertThat(s.getServices(s3.getUsername()), samePropertyValuesAs(s3));
    }

    @Test
    public void testGetServices() {
        assertThat(s.getServices(s1.getUsername()), samePropertyValuesAs(s1));
        assertThat(s.getServices(s2.getUsername()), samePropertyValuesAs(s2));
    }

    @Test
    public void testFilterByType() {
        assertThat(s.filterByType(s1.getServiceType()), hasSize(1));

    }
}
