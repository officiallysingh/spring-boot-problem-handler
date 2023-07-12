//package com.pchf.common.problem.jackson;
//
//import org.springframework.http.HttpStatusCode;
//
//final class UnknownStatus implements HttpStatusCode {
//
//    private final int statusCode;
//
//    UnknownStatus(final int statusCode) {
//        this.statusCode = statusCode;
//    }
//
//    @Override
//    public int value() {
//        return this.statusCode;
//    }
//
//    @Override
//    public boolean is1xxInformational() {
//        throw new UnsupportedOperationException("Invalid status code:" + this.statusCode);
//    }
//
//    @Override
//    public boolean is2xxSuccessful() {
//        throw new UnsupportedOperationException("Invalid status code:" + this.statusCode);
//    }
//
//    @Override
//    public boolean is3xxRedirection() {
//        throw new UnsupportedOperationException("Invalid status code:" + this.statusCode);
//    }
//
//    @Override
//    public boolean is4xxClientError() {
//        throw new UnsupportedOperationException("Invalid status code:" + this.statusCode);
//    }
//
//    @Override
//    public boolean is5xxServerError() {
//        throw new UnsupportedOperationException("Invalid status code:" + this.statusCode);
//    }
//
//    @Override
//    public boolean isError() {
//        throw new UnsupportedOperationException("Invalid status code:" + this.statusCode);
//    }
//}
