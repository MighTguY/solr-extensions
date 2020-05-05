
package io.github.mightguy.component;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.SearchComponent;
import org.apache.solr.util.plugin.SolrCoreAware;

/**
 * The only value of this class is to provide default implementation for abstract methods of
 * SearchComponent.
 */
@Slf4j
public class SearchComponentAdapter extends SearchComponent implements SolrCoreAware {

  public void inform(SolrCore core) {
    log.error("Noting to do ");
    //Do nothing. Indented to be overridden in subclasses
  }

  @Override
  public void prepare(ResponseBuilder rb) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void process(ResponseBuilder rb) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getDescription() {
    return "";
  }


}
