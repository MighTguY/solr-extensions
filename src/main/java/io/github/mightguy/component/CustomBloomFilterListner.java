package io.github.mightguy.component;

import io.github.mightguy.commons.SolrBloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrEventListener;
import org.apache.solr.search.SolrIndexSearcher;

@Slf4j
public class CustomBloomFilterListner implements SolrEventListener {

  private final SolrCore core;
  private final SolrBloomFilter solrBloomFilter;
  private final NamedList initParams;

  /**
   * Constructor for listner
   */
  public CustomBloomFilterListner(SolrCore core,
      SolrBloomFilter solrBloomFilter, NamedList initParams) {
    this.core = core;
    this.solrBloomFilter = solrBloomFilter;
    this.initParams = initParams;
  }

  @Override
  public void postCommit() {
    // Nothing to do at init
  }

  @Override
  public void postSoftCommit() {
    // Nothing to do at init
  }

  @Override
  public void newSearcher(SolrIndexSearcher newSearcher, SolrIndexSearcher currentSearcher) {
    // firstSearcher event Or New Searcher Event
    log.info("Loading bloomfilter ");
    solrBloomFilter.reload(newSearcher, initParams);
  }

  @Override
  public void init(NamedList args) {
    // Nothing to do at init
  }
}
