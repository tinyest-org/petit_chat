package org.tyniest.common.indexer.text.meili;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/** Search request query string builder */
@Getter
@Setter
@Accessors(chain = true)
public class CustomSearchRequest {
    private String q;
    private int offset;
    private int limit;
    // @JsonIgnore
    private String[] attributesToRetrieve;
    // @JsonIgnore
    private String[] attributesToCrop;
    // @JsonIgnore
    private int cropLength;
    // @JsonIgnore
    private String cropMarker;
    // @JsonIgnore
    private String highlightPreTag;
    // @JsonIgnore
    private String highlightPostTag;
    // @JsonIgnore
    private String[] attributesToHighlight;
    // @JsonIgnore
    private String[] filter;
    // @JsonIgnore
    private String[][] filterArray;
    // @JsonIgnore
    // private boolean matches;
    // @JsonIgnore
    private String[] facetsDistribution;
    // @JsonIgnore
    private String[] sort;

    /** Empty CustomSearchRequest constructor */
    public CustomSearchRequest() {}

    /**
     * Constructor for CustomSearchRequest for building search queries with the default values: offset: 0,
     * limit: 20, attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200,
     * attributesToHighlight: null, filter: null, matches: false, facetsDistribution: null, sort:
     * null
     *
     * @param q Query String
     */
    public CustomSearchRequest(String q) {
        this(q, 0);
    }

    /**
     * Constructor for CustomSearchRequest for building search queries with the default values: limit: 20,
     * attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200, attributesToHighlight:
     * null, filter: null, matches: false, facetsDistribution: null, sort: null
     *
     * @param q Query String
     * @param offset Number of documents to skip
     */
    public CustomSearchRequest(String q, int offset) {
        this(q, offset, 20);
    }

    /**
     * Constructor for CustomSearchRequest for building search queries with the default values:
     * attributesToRetrieve: ["*"], attributesToCrop: null, cropLength: 200, attributesToHighlight:
     * null, filter: null, matches: false, facetsDistribution: null, sort: null
     *
     * @param q Query String
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     */
    public CustomSearchRequest(String q, int offset, int limit) {
        this(q, offset, limit, new String[] {"*"});
    }

    /**
     * Constructor for CustomSearchRequest for building search queries with the default values:
     * attributesToCrop: null, cropLength: 200, attributesToHighlight: null, filter: null, matches:
     * false, facetsDistribution: null, sort: null
     *
     * @param q Query String
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     * @param attributesToRetrieve Attributes to display in the returned documents
     */
    public CustomSearchRequest(String q, int offset, int limit, String[] attributesToRetrieve) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                null,
                200,
                null,
                null,
                null,
                null,
                (String[]) null,
                false,
                null,
                null);
    }

    /**
     * Full CustomSearchRequest Constructor for building search queries
     *
     * @param q Query string
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     * @param attributesToRetrieve Attributes to display in the returned documents
     * @param attributesToCrop Attributes whose values have been cropped
     * @param cropLength Length used to crop field values
     * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
     * @param filter Filter queries by an attribute value
     * @param matches Defines whether an object that contains information about the matches should
     *     be returned or not
     * @param facetsDistribution Facets for which to retrieve the matching count
     * @param sort Sort queries by an attribute value
     */
    public CustomSearchRequest(
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String[] attributesToHighlight,
            String[] filter,
            boolean matches,
            String[] facetsDistribution,
            String[] sort) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                attributesToCrop,
                cropLength,
                null,
                null,
                null,
                attributesToHighlight,
                filter,
                null,
                matches,
                facetsDistribution,
                sort);
    }

    /**
     * Full CustomSearchRequest Constructor for building search queries
     *
     * @param q Query string
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     * @param attributesToRetrieve Attributes to display in the returned documents
     * @param attributesToCrop Attributes whose values have been cropped
     * @param cropLength Length used to crop field values
     * @param cropMarker String to customize default crop marker, default value: …
     * @param highlightPreTag String to customize highlight tag before every highlighted query terms
     * @param highlightPostTag String to customize highlight tag after every highlighted query terms
     * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
     * @param filter Filter queries by an attribute value
     * @param matches Defines whether an object that contains information about the matches should
     *     be returned or not
     * @param facetsDistribution Facets for which to retrieve the matching count
     * @param sort Sort queries by an attribute value
     */
    public CustomSearchRequest(
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String cropMarker,
            String highlightPreTag,
            String highlightPostTag,
            String[] attributesToHighlight,
            String[] filter,
            boolean matches,
            String[] facetsDistribution,
            String[] sort) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                attributesToCrop,
                cropLength,
                cropMarker,
                highlightPreTag,
                highlightPostTag,
                attributesToHighlight,
                filter,
                null,
                matches,
                facetsDistribution,
                sort);
    }

    /**
     * Full CustomSearchRequest Constructor for building search queries with 2D filter Array
     *
     * @param q Query string
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     * @param attributesToRetrieve Attributes to display in the returned documents
     * @param attributesToCrop Attributes whose values have been cropped
     * @param cropLength Length used to crop field values
     * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
     * @param filterArray String array that can take multiple nested filters
     * @param matches Defines whether an object that contains information about the matches should
     *     be returned or not
     * @param facetsDistribution Facets for which to retrieve the matching count
     * @param sort Sort queries by an attribute value
     */
    public CustomSearchRequest(
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String[] attributesToHighlight,
            String[][] filterArray,
            boolean matches,
            String[] facetsDistribution,
            String[] sort) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                attributesToCrop,
                cropLength,
                null,
                null,
                null,
                attributesToHighlight,
                null,
                filterArray,
                matches,
                facetsDistribution,
                sort);
    }

    /**
     * Full CustomSearchRequest Constructor for building search queries with 2D filter Array
     *
     * @param q Query string
     * @param offset Number of documents to skip
     * @param limit Maximum number of documents returned
     * @param attributesToRetrieve Attributes to display in the returned documents
     * @param attributesToCrop Attributes whose values have been cropped
     * @param cropLength Length used to crop field values
     * @param cropMarker String to customize default crop marker, default value: …
     * @param highlightPreTag String to customize highlight tag before every highlighted query terms
     * @param highlightPostTag String to customize highlight tag after every highlighted query terms
     * @param attributesToHighlight Attributes whose values will contain highlighted matching terms
     * @param filterArray String array that can take multiple nested filters
     * @param matches Defines whether an object that contains information about the matches should
     *     be returned or not
     * @param facetsDistribution Facets for which to retrieve the matching count
     * @param sort Sort queries by an attribute value
     */
    public CustomSearchRequest(
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String cropMarker,
            String highlightPreTag,
            String highlightPostTag,
            String[] attributesToHighlight,
            String[][] filterArray,
            boolean matches,
            String[] facetsDistribution,
            String[] sort) {
        this(
                q,
                offset,
                limit,
                attributesToRetrieve,
                attributesToCrop,
                cropLength,
                cropMarker,
                highlightPreTag,
                highlightPostTag,
                attributesToHighlight,
                null,
                filterArray,
                matches,
                facetsDistribution,
                sort);
    }

    private CustomSearchRequest(
            String q,
            int offset,
            int limit,
            String[] attributesToRetrieve,
            String[] attributesToCrop,
            int cropLength,
            String cropMarker,
            String highlightPreTag,
            String highlightPostTag,
            String[] attributesToHighlight,
            String[] filter,
            String[][] filterArray,
            boolean matches,
            String[] facetsDistribution,
            String[] sort) {
        this.q = q;
        this.offset = offset;
        this.limit = limit;
        this.attributesToRetrieve = attributesToRetrieve;
        this.attributesToCrop = attributesToCrop;
        this.cropLength = cropLength;
        this.cropMarker = cropMarker;
        this.highlightPreTag = highlightPreTag;
        this.highlightPostTag = highlightPostTag;
        this.attributesToHighlight = attributesToHighlight;
        this.setFilter(filter);
        this.setFilterArray(filterArray);
        // this.matches = matches;
        this.facetsDistribution = facetsDistribution;
        this.sort = sort;
    }

    /**
     * Method to set the Query String
     *
     * <p>This method is an alias of setQ()
     *
     * @param q Query String
     * @return CustomSearchRequest
     */
    public CustomSearchRequest setQuery(String q) {
        return setQ(q);
    }

}
