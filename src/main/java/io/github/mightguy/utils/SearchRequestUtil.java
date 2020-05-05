package io.github.mightguy.utils;

import io.github.mightguy.commons.SolrBloomFilter;
import io.github.mightguy.component.SolrBloomFilterComponent;
import org.apache.solr.core.SolrCore;

public final class SearchRequestUtil {

  public static SolrBloomFilter getSolrBloomFilter(SolrCore core) {
    SolrBloomFilterComponent customConfigHoldingComponent = (SolrBloomFilterComponent) core
        .getSearchComponent(SolrBloomFilterComponent.COMPONENT_NAME);
    return customConfigHoldingComponent.getBloomFilter();
  }
}
