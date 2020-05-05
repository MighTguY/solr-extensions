/*
 * Copyright (c) 2018 Walmart Co. All rights reserved.
 */

package io.github.mightguy.handler;

import io.github.mightguy.utils.SearchRequestUtil;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class BloomFilterRequestHandler extends RequestHandlerBase {

  @Override
  public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
    String term = req.getParams().get(CommonParams.Q);
    if (SearchRequestUtil.getSolrBloomFilter(req.getCore()) != null) {
      rsp.add("might_contain",
          SearchRequestUtil.getSolrBloomFilter(req.getCore()).mightContain(term));
    }

  }

  @Override
  public String getDescription() {
    return "Checking in Bloom Filter for String";
  }
}
