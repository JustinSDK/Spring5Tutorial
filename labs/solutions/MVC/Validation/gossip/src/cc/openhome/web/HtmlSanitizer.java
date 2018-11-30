package cc.openhome.web;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

@WebFilter("/new_message")
public class HtmlSanitizer extends HttpFilter {
    private PolicyFactory policy;
    
    @Override
    public void init() throws ServletException {
        policy = new HtmlPolicyBuilder()
                    .allowElements("a", "b", "i", "del", "pre", "code")
                    .allowUrlProtocols("http", "https")
                    .allowAttributes("href").onElements("a")
                    .requireRelNofollowOnLinks()
                    .toFactory();
    }

    private class SanitizerWrapper extends HttpServletRequestWrapper {
        public SanitizerWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            return Optional.ofNullable(getRequest().getParameter(name))
                           .map(policy::sanitize)
                           .orElse(null);
        }
    }    
    
    @Override
    protected void doFilter(HttpServletRequest request, 
            HttpServletResponse response, FilterChain chain)
                throws IOException, ServletException {

        chain.doFilter(new SanitizerWrapper(request), response);
    }
}
