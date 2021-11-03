package net.maxmine.api.common.mysql;

public interface ResponseHandler<H, R> {
    R handleResponse(H handle) throws Exception;
}
