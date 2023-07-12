package com.pchf.problem.core;

import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

class ContentNegotiation {

  static final ContentNegotiationStrategy DEFAULT = new FallbackContentNegotiationStrategy(new HeaderContentNegotiationStrategy());

  private ContentNegotiation() {

  }
}
