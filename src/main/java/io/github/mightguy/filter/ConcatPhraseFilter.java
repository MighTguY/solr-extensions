package io.github.mightguy.filter;

import java.io.IOException;
import lombok.EqualsAndHashCode;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * Concatenate all tokens, separated by a provided character, defaulting to a single space. It
 * always produces exactly one token, and it's designed to be the last token filter in an analysis
 * chain.
 */

@EqualsAndHashCode
public class ConcatPhraseFilter extends TokenFilter {


  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
  private final PositionIncrementAttribute posIncrAtt = addAttribute(
      PositionIncrementAttribute.class);
  private final PositionLengthAttribute posLenAtt = addAttribute(PositionLengthAttribute.class);
  private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

  private char separator;
  private boolean done;
  private StringBuilder buf = new StringBuilder(128);
  private State finalState;

  /**
   * Construct a token stream filtering the given input.
   */
  public ConcatPhraseFilter(TokenStream input, char seperator) {
    super(input);
    this.separator = seperator;
  }

  @Override
  public void reset() throws IOException {
    input.reset();
    done = false;
  }

  @Override
  public final boolean incrementToken() throws IOException {
    if (done) {
      return false;
    }
    boolean result = buildSingleOutputToken();
    this.finalState = this.captureState();
    return result;
  }

  @Override
  public void end() throws IOException {
    if (!this.done) {
      this.input.end();
      this.done = true;
    }

    if (this.finalState != null) {
      this.restoreState(this.finalState);
    }
  }

  private final boolean buildSingleOutputToken() throws IOException {
    done = false;
    buf.setLength(0);
    boolean firstTerm = true;
    while (input.incrementToken()) {
      if (!firstTerm) {
        buf.append(separator);
      }
      //consider indexing special chars when posInc > 1 (stop words). We ignore for now. #13
      buf.append(termAtt);
      firstTerm = false;
    }
    //calling to see end of stream offsets
    input.end();
    termAtt.setEmpty().append(buf);
    done = true;
    //Setting the other attributes ultimately won't have much effect but lets be thorough
    offsetAtt.setOffset(0, offsetAtt.endOffset());
    posIncrAtt.setPositionIncrement(1);
    posLenAtt.setPositionLength(1);//or do we add up the positions?  Probably not used any way.
    typeAtt.setType(ShingleFilter.DEFAULT_TOKEN_TYPE);//"shingle"
    return true;
  }
}
