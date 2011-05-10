package casclient

import org.apache.tools.ant.types.Assertions.EnabledAssertion;
import org.springframework.mock.web.MockHttpServletRequest
import grails.util.Environment

import casClient.MockRemoteUserRequestWrapper
import grails.test.*

class MockRemoteUserRequestWrapperTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetRemoteUser() {		
		MockHttpServletRequest requestMock = new MockHttpServletRequest()
		String usuario = 'USUARIO'
		requestMock.session.setAttribute('casUser', usuario)				
		MockRemoteUserRequestWrapper mockRemoteUserRequestWrapper = new MockRemoteUserRequestWrapper(requestMock)
		
		assertEquals usuario, mockRemoteUserRequestWrapper.getRemoteUser()
    }
}
