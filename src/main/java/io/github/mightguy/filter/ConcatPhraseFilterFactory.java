package io.github.mightguy.filter;

import java.util.Map;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class ConcatPhraseFilterFactory extends TokenFilterFactory {

  private final String tokenSeparator;

  public ConcatPhraseFilterFactory(Map<String, String> args) {
    super(args);
    tokenSeparator = get(args, "tokenSeparator", " ");
    if (tokenSeparator.length() != 1) {
      throw new IllegalArgumentException("tokenSeparator should be 1 char: " + tokenSeparator);
    }
  }

  @Override
  public TokenStream create(TokenStream input) {
    return new ConcatPhraseFilter(input, tokenSeparator.charAt(0));
  }
}
