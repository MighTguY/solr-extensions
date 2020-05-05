package io.github.mightguy.filter;

import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.KStemFilter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ExtPhraseFilterTest extends BaseTokenStreamTestCase {

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();


  public void testDupsAndSorting() throws Exception {
    MockTokenizer tokenizer = whitespaceMockTokenizer("C A B E");
    TokenStream stream = new ConcatPhraseFilter(tokenizer, ' ');
    assertTokenStreamContents(stream, new String[]{"C A B E"});
  }

  public void testStemming() throws Exception {
    MockTokenizer tokenizer = whitespaceMockTokenizer("skimmed milk");
    TokenStream stream = new ConcatPhraseFilter(new KStemFilter(tokenizer), ' ');
    assertTokenStreamContents(stream, new String[]{"skim milk"});
  }


  public void testFactoryException() throws Exception {
    exceptionRule.expect(IllegalArgumentException.class);
    exceptionRule.expectMessage("tokenSeparator should be 1 char");
    Map<String, String> args = new HashMap<>();
    args.put("tokenSeparator", "");
    ConcatPhraseFilterFactory factory = new ConcatPhraseFilterFactory(args);
    MockTokenizer tokenizer = whitespaceMockTokenizer("C A B E");
    TokenStream extPhraseFilter = factory.create(tokenizer);
  }

  @Test
  public void testFactory() throws Exception {
    Map<String, String> args = new HashMap<>();
    ConcatPhraseFilterFactory factory = new ConcatPhraseFilterFactory(args);
    MockTokenizer tokenizer = whitespaceMockTokenizer("C A B E");
    TokenStream extPhraseFilter = factory.create(tokenizer);
    assertEquals(ConcatPhraseFilter.class, extPhraseFilter.getClass());
  }
}
