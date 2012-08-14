package org.mitre.openid.connect.web;

import static org.junit.Assert.assertEquals;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ExtendedModelMap;
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations={"classpath:org/mitre/openid/connect/web/test.xml"})

public class UserInfoEndpointTest {

	@Autowired
    private UserInfoEndpoint userInfoEndpoint;

	
	Principal p = new  Principal() {	
		@Override
		public String getName() {
			return "jricher";
		}
	};
	
//	@Test
//	public void testGetInfo() {
//		UserInfoEndpoint u = new UserInfoEndpoint();
//		String view = u.getInfo(p, "openid", new ExtendedModelMap());
//		assertEquals("openid",view);
//	}

	@Test
	public void testViewConfiguration(){
		String view = userInfoEndpoint.getInfo(p, "openid", new ExtendedModelMap());
		assertEquals("oidView",view);
	    view = userInfoEndpoint.getInfo(p, "simple", new ExtendedModelMap());
	    assertEquals("simpleView",view);
	}

	
}
