package com.productSearchService.dao;

import com.productSearchService.exception.ParseError;
import com.productSearchService.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LuceneDocs {

    StandardAnalyzer analyzer = new StandardAnalyzer();
    Directory index = new RAMDirectory();

    /**
     * This method performing indexing on product List and saving in the Lucene docs for further
     * processing
     * @param productList
     * @throws IOException
     */
    public void performIndex(List<Product> productList) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w = new IndexWriter(index, config);
        productList.forEach((product) -> {
            try {
                addDoc(w, product.getImageUrl(), product.getName(), product.getDescription(), product.getPrice());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        w.close();
    }

    /**
     * This method filters product data on the basis of search query ,offset and limit
     * @param keyword
     * @param offset
     * @param limit
     * @return
     * @throws IOException
     */
    public List<Product> luceneQuerySearch(String keyword, int offset, int limit) throws IOException {
        String queryString = keyword == null ? "*:*" : (keyword + "*");
        Query query = null;
        try {
            String[] fields = { "name", "description" };
            query = new MultiFieldQueryParser(fields, analyzer).parse(queryString);
        } catch (ParseException e) {
            throw new ParseError("Parse error occurred");
        }

        // search
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(limit + offset);
        log.info("Preparing to search for the keyword {}", keyword);
        searcher.search(query, collector);
        ScoreDoc[] hits = collector.topDocs(offset, limit).scoreDocs;

        List<Product> searchedProducts = new ArrayList<>();
        log.info("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            Product p = Product.builder().imageUrl(d.get("imageUrl"))
                    .name(d.get("name"))
                    .description(d.get("description"))
                    .price(d.get("price"))
                    .build();
            searchedProducts.add(p);
        }
        reader.close();
        return searchedProducts;
    }

    /**
     * This method
     * @param w
     * @param imageUrl
     * @param name
     * @param description
     * @param price
     * @throws IOException
     */
    private static void addDoc(IndexWriter w, String imageUrl, String name, String description, String price) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("imageUrl", imageUrl, Field.Store.YES));
        doc.add(new TextField("name", name, Field.Store.YES));
        doc.add(new TextField("description", description, Field.Store.YES));
        doc.add(new TextField("price", price, Field.Store.YES));
        w.addDocument(doc);
    }
}

