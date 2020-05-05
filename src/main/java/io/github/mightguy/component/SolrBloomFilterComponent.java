
package io.github.mightguy.component;

import io.github.mightguy.commons.SolrBloomFilter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.search.SolrIndexSearcher;

@Slf4j
public class SolrBloomFilterComponent extends SearchComponentAdapter {

  public static final String COMPONENT_NAME = "searchConfigHoldingComponent";

  @Getter
  private SolrBloomFilter bloomFilter;
  private CustomBloomFilterListner customBloomFilterListner;
  private NamedList initParams;


  @Override
  public void init(NamedList args) {
    super.init(args);
    this.initParams = args;
  }

  @Override
  public void inform(SolrCore core) {
    log.info("Initialization Started");
    customBloomFilterListner = new CustomBloomFilterListner(core, bloomFilter, initParams);
    core.registerFirstSearcherListener(customBloomFilterListner);
    log.info("Initialization completed");
  }


  @Override
  public String getDescription() {
    return "This search component holds search configuration.";
  }



}
