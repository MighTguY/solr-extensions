
/*
 * Copyright (c) 2018 Walmart Co. All rights reserved.
 */

package io.github.mightguy.commons;

import com.sangupta.bloomfilter.impl.InMemoryBloomFilter;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.CharsRefBuilder;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.search.SolrIndexSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrBloomFilter {


  private static final Logger log = LoggerFactory.getLogger(SolrBloomFilter.class);
  private static final int EXPECTED_INSERTIONS_COUNT = 500000;
  private static final double EXPECTED_MAX_POSITIVE_RATE = 0.03D;
  private InMemoryBloomFilter<String> filter;

  public SolrBloomFilter() {
    this.filter = new InMemoryBloomFilter<>(0, 0);
  }

  public void reload(SolrIndexSearcher productsSearcher, NamedList args) {
    String fieldLisStr = args.get("bloomfilter_fields").toString();
    int insertionsCount = 0;
    long time = System.currentTimeMillis();
    InMemoryBloomFilter<String> newFilter = new InMemoryBloomFilter<>(EXPECTED_INSERTIONS_COUNT,
        EXPECTED_MAX_POSITIVE_RATE);
    try {
      DirectoryReader productsIndexReader = productsSearcher.getIndexReader();
      Fields fields = MultiFields.getFields(productsIndexReader);
      IndexSchema schema = productsSearcher.getCore().getLatestSchema();
      List<String> fieldList = Arrays.asList(fieldLisStr.split("\\s*,\\s*"));
      for (String field : fields) {
        if (!fieldList.contains(field)) {
          continue;
        }
        log.info("Field Name {}", field);
        FieldType type = schema.getField(field).getType();
        for (TermsEnum iterator = fields.terms(field).iterator(); iterator.next() != null; ) {
          BytesRef term = iterator.term();
          CharsRefBuilder charsRefBuilder = new CharsRefBuilder();
          type.indexedToReadable(term, charsRefBuilder);
          insertionsCount++;
          this.filter.add(field.trim() + ":" + charsRefBuilder.toString().trim());
        }
      }
      this.filter = newFilter;
    } catch (Exception e) {
      log.error("Error while populating data for bloomfilter filter", e);
    }
    log.info("Data for bloomfilter filter was populated. Time={} ms, insertionsCount={} ",
        (System.currentTimeMillis() - time), insertionsCount);
  }

  public boolean mightContain(String term) {
    return filter.contains(term);
  }

  public void clear() {
    filter = null;
  }

}
