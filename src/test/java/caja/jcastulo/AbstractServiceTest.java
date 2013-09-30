/**
 * Created on Nov 1, 2011
 */
package caja.jcastulo;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Carlos Juarez
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:META-INF/spring/datasource.xml",
    "classpath:META-INF/spring/jpa-tx-config.xml",
    "classpath:META-INF/spring/jpa-service-context.xml",
    "classpath:META-INF/spring/root-context.xml"})
@ActiveProfiles
public abstract class AbstractServiceTest {

//    @Before
    public void setupLocale() {
        System.out.println("before method running");
//        ServletRequestAttributes mockedSra = mock(ServletRequestAttributes.class);
//        HttpServletRequest mockedRequest = mock(HttpServletRequest.class);
//        LocaleResolver mockedLocaleResolver = mock(LocaleResolver.class);
//        when(mockedLocaleResolver.resolveLocale(mockedRequest)).thenReturn(Locale.US);
//        System.out.println("after mockedLocaleResolver.resolveLocal");
////        when(request.getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE)).thenReturn(localeResolver);
//        when(mockedRequest.getAttribute("org.springframework.web.servlet.DispatcherServlet" + ".LOCALE_RESOLVER")).thenReturn(mockedLocaleResolver);
//        System.out.println("after mockedRequest.getAttribute");
//        when(mockedSra.getRequest()).thenReturn(mockedRequest);
//        System.out.println("after mockedSra.getRequest()");
//        RequestContextHolder.setRequestAttributes(mockedSra);
    }
}
