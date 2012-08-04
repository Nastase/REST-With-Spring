package org.rest.sec.util;

import org.apache.commons.lang3.tuple.Triple;
import org.rest.common.util.QueryUtil;
import org.rest.common.util.SearchCommonUtil;
import org.rest.common.web.ClientOperation;

public final class SearchUriBuilder {
    private StringBuilder uri;

    public SearchUriBuilder() {
        uri = new StringBuilder();
    }

    // API

    public final SearchUriBuilder consume(final Triple<String, ClientOperation, String> constraint) {
        final ClientOperation operation = (constraint == null) ? null : constraint.getMiddle();
        final boolean negated = (constraint == null) ? false : constraint.getMiddle().isNegated();
        final String key = (constraint == null) ? null : constraint.getLeft();
        final String value = (constraint == null) ? null : constraint.getRight();

        return consume(operation, key, value, negated);
    }

    public final SearchUriBuilder consume(final ClientOperation op, final String key, final String value, final boolean negated) {
        if (value != null) {
            addInterFragmentSeparatorIfNecessary();
            final String idFragment = constructFragment(op, negated, key, value);
            uri.append(idFragment);
        }

        return this;
    }

    // util

    private String constructFragment(final ClientOperation operation, final boolean negated, final String key, final String value) {
        final String op = constructOperationString(negated);
        final String fragment = key + op + constructStringQueryValue(value, operation);
        return fragment;
    }

    private String constructOperationString(final boolean negated) {
        return (negated) ? SearchCommonUtil.NEGATION + SearchCommonUtil.OP : SearchCommonUtil.OP;
    }

    private void addInterFragmentSeparatorIfNecessary() {
        if (uri.length() != 0) {
            uri.append(SearchCommonUtil.SEPARATOR);
        }
    }

    String constructStringQueryValue(final String name, final ClientOperation op) {
        switch (op) {
        case CONTAINS:
            return QueryUtil.ANY_CLIENT + name + QueryUtil.ANY_CLIENT;
        case NEG_CONTAINS:
            return QueryUtil.ANY_CLIENT + name + QueryUtil.ANY_CLIENT;

        case STARTS_WITH:
            return name + QueryUtil.ANY_CLIENT;
        case NEG_STARTS_WITH:
            return name + QueryUtil.ANY_CLIENT;

        case ENDS_WITH:
            return QueryUtil.ANY_CLIENT + name;
        case NEG_ENDS_WITH:
            return QueryUtil.ANY_CLIENT + name;

        default:
            break;
        }
        return name;
    }

    public final String build() {
        return uri.toString();
    }

}
