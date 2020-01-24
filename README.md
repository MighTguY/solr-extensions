# custom-solr-extensions
Custom Solr Extensions
This repository will hold the custom utility plugins for the SOlr/Lucene

Added Solr plugin for :

* **CustomPhraseFilter**: This is for cases where we need to produce exact token with the stemming applied to the term.


    ```
    washing liquid, washing liquids, washings liquid, <anyterm> washing liquid <anyterm>
    (but not washing, liquid, liquids) 
    ```

    With default analyzers present in SOLR, this was not possible, so we write our own custom Extended Phrase Filter, which will first tokenize the terms and then recombines them and store them as stemmed phrases.
    It Concatenate all tokens, separated by a provided character, defaulting to a single space. It always produces exactly one token, and it's designed to be the last token filter in an analysis chain.
    So If we have Stemmer applied before this analyzer these strings will produce 
    
    `wash liquid`


## How to use
* **CustomPhraseFilter**: Include this factory class while defining analyzer chain as shown below:
    ```
    <fieldType name="searchTermFieldAnalyzedType" class="solr.TextField">
             <analyzer>
                <tokenizer class="solr.WhitespaceTokenizerFactory"/>
                 <charFilter class="solr.PatternReplaceCharFilterFactory" pattern="\s+" replacement=" "/>
                 <charFilter class="solr.PatternReplaceCharFilterFactory" pattern="\s+$" replacement=""/>
                 <charFilter class="solr.PatternReplaceCharFilterFactory" pattern="^\s+" replacement=""/>
                 <filter class="solr.LowerCaseFilterFactory"/>
                 <filter class="solr.ASCIIFoldingFilterFactory"/>
                 <filter class="solr.PatternReplaceFilterFactory" pattern="[^a-z0-9@]" replacement="" replace="all"/>
                 <filter class="solr.LengthFilterFactory" min="1" max="1024" />
                 <filter class="solr.KStemFilterFactory"/>
                 <filter class="io.github.mightguy.filter.ConcatPhraseFilterFactory"/>
             </analyzer>
          </fieldType>
    ```
    make sure this should 

 
