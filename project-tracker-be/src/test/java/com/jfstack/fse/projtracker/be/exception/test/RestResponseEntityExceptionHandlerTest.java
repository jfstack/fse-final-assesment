package com.jfstack.fse.projtracker.be.exception.test;

import com.jfstack.fse.projtracker.be.exception.RestResponseEntityExceptionHandler;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import static org.assertj.core.api.Assertions.assertThat;

public class RestResponseEntityExceptionHandlerTest {

    private RestResponseEntityExceptionHandler exceptionHandlerSupport =
            new RestResponseEntityExceptionHandler();

    private DefaultHandlerExceptionResolver defaultExceptionResolver =
            new DefaultHandlerExceptionResolver();

    private MockHttpServletRequest servletRequest =
            new MockHttpServletRequest("GET", "/");

    private MockHttpServletResponse servletResponse =
            new MockHttpServletResponse();

    private WebRequest request =
            new ServletWebRequest(this.servletRequest, this.servletResponse);

    @Test
    public void testHandleErrorForIllegalException() {
        testException(new IllegalArgumentException("bad request"));
    }

    @Test
    public void testHandleErrorForRuntimeException() {
        testException(new RuntimeException("record not found"));
    }

    private ResponseEntity<Object> testException(IllegalArgumentException ex) {
        try {
            ResponseEntity<Object> responseEntity =
                    this.exceptionHandlerSupport.handleError(ex, this.request);

            this.defaultExceptionResolver.resolveException(this.servletRequest, this.servletResponse, null, ex);

            assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());

            return responseEntity;

        } catch (Exception ex2) {
            throw new IllegalStateException("handleException threw exception", ex2);
        }
    }


    private ResponseEntity<Object> testException(RuntimeException ex) {
        try {
            ResponseEntity<Object> responseEntity =
                    this.exceptionHandlerSupport.handleError(ex, this.request);

            this.defaultExceptionResolver.resolveException(this.servletRequest, this.servletResponse, null, ex);

            assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());

            return responseEntity;

        } catch (Exception ex2) {
            throw new IllegalStateException("handleException threw exception", ex2);
        }
    }
}
