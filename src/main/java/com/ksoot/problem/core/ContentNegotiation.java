package com.ksoot.problem.core;

import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

/** Utility for content negotiation. */
class ContentNegotiation {

  /** The default content negotiation strategy. */
  static final ContentNegotiationStrategy DEFAULT =
      new FallbackContentNegotiationStrategy(new HeaderContentNegotiationStrategy());

  private ContentNegotiation() {}
}
