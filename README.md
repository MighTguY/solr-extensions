# custom-solr-extensions
Custom Solr Extensions
This repository will hold the custom utility plugins for the SOlr/Lucene

Added Solr plugin for :

* **Solr Bloom Filter**: 


Bloom filter is a probabilistic data structure which tells that the element either definitely is not in the set or may be in the set.  It is a data structure designed to tell, rapidly and memory-efficiently, whether an element is present in a set. , It requires hashing of the data and storing it in a BitVector. 


In our case, it will be used to check whether the term is present in our set our not, And we will be achieving it by creating an InMemoryBloomFilter which will be storing the terms and fields from the index. It will be created like a warmup cache using firstSearcher and newSaercher events of solr. For ex: for all the fields will be storing the terms from the index in BloomFilter and whenever there is need of creating a query over a field,  we will select fields on the basis that whether the field might Contain the term or not. So this will help us on reducing the number of fields used for the search term, fields will be selected dynamically per search term. Since in our case, we have 20+ search fields, but not all of them would be valid for all the keywords, so instead of creating a query on all those fields will make a query on the relevant fields. This will help us in reducing the query tree, which leads to significantly reduce in time of query parsing and fetching results.

Since we are having 20+ search fields, so to filter out the relevant fields for search we will be using bloom filter, for example, ASDA skimmed milk is the search term, so there is no point of searching ASDA under nutrients field as there is no nutrient named ASDA.


This can be achieved by using Bloom Filter while creating a query on fields, this will help to select the fields relevant to the search query term.

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

 
