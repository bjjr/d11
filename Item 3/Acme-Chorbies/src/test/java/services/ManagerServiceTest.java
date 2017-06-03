
package services;

import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Manager;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ManagerServiceTest extends AbstractTest {

	// System under test ----------------------------

	@Autowired
	private ManagerService	managerService;


	// Services -------------------------------------

	// Tests ----------------------------------------

	@Test
	public void testfindManagersSortedByNumberEvents() {
		this.authenticate("admin");

		List<Manager> managers;

		managers = new LinkedList<>(this.managerService.findManagersSortedByNumberEvents());

		Assert.isTrue(managers.get(0).equals(this.managerService.findOne(1812)));
		Assert.isTrue(managers.get(1).equals(this.managerService.findOne(1811)));

		this.unauthenticate();
	}

}
